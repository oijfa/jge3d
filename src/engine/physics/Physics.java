package engine.physics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import com.bulletphysics.collision.dispatch.GhostObject;

import engine.entity.Entity;
import engine.entity.Actor;
import engine.entity.EntityCallbackFunctions;
import engine.entity.QueueItem;
import engine.entity.Entity.ObjectType;
import engine.entity.EntityList;

public class Physics implements PhysicsInterface{
	// World Definitions
	private DefaultCollisionConfiguration collisionConfiguration;
	private CollisionDispatcher dispatcher;
	private Vector3f worldAabbMin;
	private Vector3f worldAabbMax;
	private BroadphaseInterface overlappingPairCache;
	//private DbvtBroadphase broadphase;
	private ConstraintSolver solver;
	private DynamicsWorld dynamicsWorld;

	// Used for mouse movement
	//private TypedConstraint pickedConstraint = null;
	//private CollisionObject pickedEntity = null;

	// private List<CollisionShape> collisionShapes = new
	// ArrayList<CollisionShape>();
	float deltaT;
	long frames = 0;

	// For holding the previous time in microseconds to calculate deltaT
	private long prev_time;
	
	//Keeping track of entities that need added/removed
	private ConcurrentLinkedQueue<QueueItem> physicsQueue;

	public Physics() {
		physicsQueue = new ConcurrentLinkedQueue<QueueItem>();
		
		// Default collision constructor
		collisionConfiguration = new DefaultCollisionConfiguration();

		// Creates a dispatcher thread for sending processing physics
		// calculations
		dispatcher = new CollisionDispatcher(collisionConfiguration);

		// Min and Max collision boundaries for world (needs changing)
		worldAabbMin = new Vector3f(-10000, -10000, -10000);
		worldAabbMax = new Vector3f(10000, 10000, 10000);

		// algorithm for finding collision proximity (there are better ones)
		overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		overlappingPairCache.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());
		//broadphase = new DbvtBroadphase();

		// Type of solver to be used for solving physics (look into threading
		// for parallel)
		solver = new SequentialImpulseConstraintSolver();

		// Create the dynamics world and set default options
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3f(0, 0, 0));
		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0.1f;


		// Preset the previous time so deltaT isn't enormous on first run
		prev_time = System.nanoTime();
	}

	public DynamicsWorld getDynamicsWorld() {
		return dynamicsWorld;
	}

	public void clientUpdate() {
		// simple dynamics world doesn't handle fixed-time-stepping
		deltaT = (System.nanoTime() - prev_time);
		prev_time = System.nanoTime();
		frames++;
		// step the simulation
		if (dynamicsWorld != null) {
			//TODO: This should be configurable
			dynamicsWorld.stepSimulation(deltaT / 1000000000f);
		}
	}

	public long getFrames() {
		return frames;
	}

	public void resetFrames() {
		frames = 0;
	}

	public float getDeltaT() {
		return deltaT;
	}

	public void setGravity(Vector3f gravity) {
		dynamicsWorld.setGravity(gravity);
	}
	
	public boolean addEntity(Entity e) {
		boolean ret = false;

		if (e.keyExists("name")) {
			if (e.getCollisionObject() != null) {
				dynamicsWorld.addCollisionObject(e.getCollisionObject());
				if(e.getObjectType() == ObjectType.actor) {
					//TODO: I'm not sure why this line isn't necessary; leave it until we figure that out
					dynamicsWorld.addAction(((Actor)e).getActor());
				}
			}
			ret = true;
		}
		return ret;
	}

	public void removeEntity(Entity e) {
		if (e.getObjectType() == ObjectType.rigidbody){
			dynamicsWorld.removeRigidBody((RigidBody) e.getCollisionObject());
		} else {
			dynamicsWorld.removeCollisionObject(e.getCollisionObject());
		}
		if(e.getObjectType() == ObjectType.actor) {
			dynamicsWorld.removeAction(((Actor)e).getActor());
		}
	}
	
	public void handleGhostCollisions(EntityList entity_list) {
		for(Entity entity : entity_list.getEntities()){
			if( (Boolean)entity.getProperty(Entity.COLLIDABLE) == false){
				com.bulletphysics.collision.dispatch.GhostObject ghost = (GhostObject) entity.getCollisionObject();
				for(int i=0;i<ghost.getNumOverlappingObjects();i++){
					entCollidedWith(entity, entity_list.getItem(ghost.getOverlappingObject(i)));
				}
			}
		}
	}
	private void entCollidedWith(Entity source, Entity collided_with){
		ArrayList<Method> methods = source.getCollisionFunctions();
		for(Method method : methods){
			try {
				method.invoke(EntityCallbackFunctions.class, source, collided_with, this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Entity pickEntityWithRay(Vector3f ray_from, Vector3f ray_to, EntityList entity_list) {
		ClosestRayResultCallback resultCallback = new ClosestRayResultCallback(ray_from, ray_to);
		
		dynamicsWorld.rayTest(ray_from, ray_to, resultCallback);
		
		if(resultCallback.collisionObject != null){
			for(Entity ent : entity_list){
				if(resultCallback.collisionObject == ent.getCollisionObject()){
					return ent;
				}
			}
		}
		return null;
	}

	@Override
	public void entityAdded(Entity ent) {
		physicsQueue.add(new QueueItem(ent, QueueItem.ADD));
	}

	@Override
	public void entityRemoved(Entity ent) {
		physicsQueue.add(new QueueItem(ent, QueueItem.REMOVE));
	}
	
	public void parsePhysicsQueue() {
		Object[] itemArray = (Object[]) physicsQueue.toArray();
		for (Object obj_item : itemArray) {
			QueueItem item = (QueueItem)obj_item;
			if (QueueItem.ADD == ((QueueItem) item).getAction()) {
				if (item.getEnt().getCollisionObject() != null) {
					dynamicsWorld.addCollisionObject(item.getEnt().getCollisionObject());
					if(item.getEnt().getObjectType() == ObjectType.actor) {
						//TODO: I'm not sure why this line isn't necessary; leave it until we figure that out
						dynamicsWorld.addAction(((Actor)item.getEnt()).getActor());
					}
				}
			} else if (QueueItem.REMOVE == ((QueueItem) item).getAction()) {
				if (item.getEnt().getObjectType() == ObjectType.rigidbody){
					dynamicsWorld.removeRigidBody((RigidBody) item.getEnt().getCollisionObject());
				} else {
					dynamicsWorld.removeCollisionObject(item.getEnt().getCollisionObject());
				}
				if(item.getEnt().getObjectType() == ObjectType.actor) {
					dynamicsWorld.removeAction(((Actor)item.getEnt()).getActor());
				}
			}
			physicsQueue.remove(item);
		}
	}

	@Override
	public void entityModelChanged(Entity ent) {/*Don't care*/}
	
	/*
	public void reduceHull(Entity e) {
		int vertexcount = 0;
		int trianglecount = 0;
		BvhTriangleMeshShape meshshape;
		int index = 0;

		// get the total number of vertices so we can declare our stupid ass
		// directly allocated buffer
		for (int i = 0; i < e.getModel().getMeshCount(); i++) {
			trianglecount += e.getModel().getMesh(i).getFaceCount();
			for (int j = 0; j < e.getModel().getMesh(i).getFaceCount(); j++) {
				vertexcount += e.getModel().getMesh(i).getFace(j).getVertexCount();
			}
		}

		// Declare the shitty ass directly allocated buffer
		ByteBuffer vertexbuffer = ByteBuffer.allocateDirect(3 * vertexcount * 4);
		vertexbuffer.order(ByteOrder.nativeOrder());
		ByteBuffer indexbuffer = ByteBuffer.allocateDirect(3 * vertexcount * 4);
		indexbuffer.order(ByteOrder.nativeOrder());

		indexbuffer.clear();

		// Insert the vectors in to our fucking awful directly allocated buffer

		// for every mesh
		for (int i = 0; i < e.getModel().getMeshCount(); i++) {
			// for every face
			for (int j = 0; j < e.getModel().getMesh(i).getFaceCount(); j++) {
				// for every vertex
				for (int k = 0; k < e.getModel().getMesh(i).getFace(j)
					.getVertexCount(); k++) {
					// get current vertex
					Vector3f v = e.getModel().getMesh(i).getFace(j)
						.getVertex(k);

					// shove x,y,z into buffer
					vertexbuffer.putFloat(v.x);
					vertexbuffer.putFloat(v.y);
					vertexbuffer.putFloat(v.z);

					// push indices for this vertex
					indexbuffer.putInt(index);
					index++;
					indexbuffer.putInt(index);
					index++;
					indexbuffer.putInt(index);
					index++;
				}
			}
		}

		// flip that shit dawg; Just the index buffer, not the vertex buffer
		// I mean, because that makes shit loads of sense...
		indexbuffer.flip();
		// vertexbuffer.flip();

		// Oh great, let's throw data at this undocumented function and hope
		// something useful comes out the other side!
		// *Clearly you are frustrated --Robert
		// ** I'M PISSED --Adam
		TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray(
			trianglecount, indexbuffer, 3 * 4,// indexStride,
			vertexcount, vertexbuffer, 3 * 4// vertStride
		);

		boolean useQuantizedAabbCompression = true;
		// Put vertices in triangle mesh array
		meshshape = new BvhTriangleMeshShape(indexVertexArrays,
			useQuantizedAabbCompression);

		// Create an optimized Bvh to reduce the vertex set of the original mesh
		OptimizedBvh optimizedmesh = new OptimizedBvh();
		optimizedmesh.build(
			meshshape.getMeshInterface(),
			useQuantizedAabbCompression, worldAabbMin, worldAabbMax
		);

		// Set the reduced set back to the mesh
		meshshape.setOptimizedBvh(optimizedmesh);

		// Override the original shape with the new one
		e.getCollisionObject().setCollisionShape(meshshape); 
	}
	
	public void drag(Camera camera, int state, Vector3f rayTo) {
		if (state == 0) {
			if (dynamicsWorld != null) {
				CollisionWorld.ClosestRayResultCallback rayCallback = 
					new CollisionWorld.ClosestRayResultCallback(
						camera.getPosition(), rayTo
					);
				dynamicsWorld.rayTest(camera.getPosition(), rayTo, rayCallback);
				if (rayCallback.hasHit()) {
					RigidBody hitBody = RigidBody.upcast(rayCallback.collisionObject);
					if (hitBody != null) {
						// other exclusions?
						if (!(hitBody.isStaticObject() || hitBody.isKinematicObject())) {
							pickedEntity = hitBody;
							pickedEntity.setActivationState(CollisionObject.DISABLE_DEACTIVATION);

							Vector3f pickPos = new Vector3f(rayCallback.hitPointWorld);

							Transform tmpTrans = hitBody.getCenterOfMassTransform(new Transform());
							tmpTrans.inverse();

							hitBody.getCenterOfMassPosition(pickPos);
							Vector3f localPivot = new Vector3f(pickPos);
							tmpTrans.transform(localPivot);

							Point2PointConstraint p2p = new Point2PointConstraint(hitBody, localPivot);
							// SliderConstraint p2p = new SliderConstraint();
							p2p.setting.impulseClamp = 1f;

							dynamicsWorld.addConstraint(p2p);
							pickedConstraint = p2p;

							// save mouse position for dragging
							BulletStats.gOldPickingPos.set(rayTo);
							Vector3f tmp = new Vector3f();
							tmp.sub(pickPos, camera.getPosition());
							BulletStats.gOldPickingDist = tmp.length();
							// very weak constraint for picking
							p2p.setting.tau = 0.1f;
						}
					}
				}
			}
		} else if (state == 1) {
			if (pickedConstraint != null && dynamicsWorld != null) {
				dynamicsWorld.removeConstraint(pickedConstraint);
				// delete m_pickConstraint;
				// printf("removed constraint %i",gPickingConstraintId);
				pickedConstraint = null;
				pickedEntity.forceActivationState(CollisionObject.ACTIVE_TAG);
				pickedEntity.setDeactivationTime(0f);
				pickedEntity = null;
			}
		}
	}
	 
	public void motionFunc(Camera camera, int x, int y) {
		if (pickedConstraint != null) {
			// move the constraint pivot
			Point2PointConstraint p2p = (Point2PointConstraint) pickedConstraint;
			if (p2p != null) {
				// keep it at the same picking distance
				Vector3f newRayTo = new Vector3f(camera.getRayTo(x, y));
				Vector3f eyePos = new Vector3f(camera.getPosition());
				Vector3f dir = new Vector3f();
				dir.sub(newRayTo, eyePos);
				dir.normalize();
				dir.scale(BulletStats.gOldPickingDist);

				Vector3f newPos = new Vector3f();
				newPos.add(eyePos, dir);
				p2p.setPivotB(newPos);
			}
		}
	}
 */
}
