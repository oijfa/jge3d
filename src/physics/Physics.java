package physics;

import input.Point2PointConstraint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.OptimizedBvh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import entity.Camera;
import entity.Entity;

public class Physics {
	//World Definitions
	private DefaultCollisionConfiguration collisionConfiguration;
	private CollisionDispatcher dispatcher;
	private Vector3f worldAabbMin;
	private Vector3f worldAabbMax;
	private BroadphaseInterface overlappingPairCache;
	private ConstraintSolver solver;
	private DynamicsWorld dynamicsWorld;
	//private List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
	float deltaT;
	long frames=0;

	//For holding the previous time in microseconds to calculate deltaT
	private long prev_time;
	
	public Physics() {		
		//Default collision constructor
		collisionConfiguration = new DefaultCollisionConfiguration();
		
		//Creates a dispatcher thread for sending processing physics calculations
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		
		//Min and Max collision boundaries for world (needs changing)
		worldAabbMin = new Vector3f(-100,-100,-100);
		worldAabbMax = new Vector3f(100,100,100);
		
		//algorithm for finding collision proximity (there are better ones)
		overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		
		//Type of solver to be used for solving physics (look into threading for parallel)
		solver = new SequentialImpulseConstraintSolver();
		
		//Create the dynamics world and set default options
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3f(0,0,0));

		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0f;
		
		//Preset the previous time so deltaT isn't enormous on first run
		prev_time = System.nanoTime();
	}

	public RigidBody createRigidBody(float mass, Transform startTransform, CollisionShape shape) {
		// rigid body is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(255f, 255f, 255f);
		if (isDynamic) {
			shape.calculateLocalInertia(mass, localInertia);
		}

		//set motion state (keeps track of objects motion, durr)
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
		RigidBody body = new RigidBody(cInfo);

		dynamicsWorld.addRigidBody(body);

		return body;
	}
	
	public DynamicsWorld getDynamicsWorld() {
		return dynamicsWorld;
	}
	
	public void clientUpdate() {
		// simple dynamics world doesn't handle fixed-time-stepping
		deltaT = (System.nanoTime()-prev_time);
		prev_time = System.nanoTime();
		frames++;
		// step the simulation
		if (dynamicsWorld != null) {
			dynamicsWorld.stepSimulation(deltaT / 1000000000f);
		}
	}
	
	public long getFrames() {
		return frames;
	}
	
	public void resetFrames() {
		frames=0;
	}
	
	public float getDeltaT() {
		return deltaT;
	}
	
	public void addEntity(Entity e){ dynamicsWorld.addRigidBody(e); }
	
	public void reduceHull(Entity e){
		int vertexcount=0;
		int trianglecount=0;
		BvhTriangleMeshShape meshshape;
		int index=0;
		
		//get the total number of vertices so we can declare our stupid ass
		//directly allocated buffer
		for(int i=0;i<e.getModel().getMeshCount();i++) {
			trianglecount += e.getModel().getMesh(i).getFaceCount();
			for(int j=0;j<e.getModel().getMesh(i).getFaceCount();j++) {
				vertexcount += e.getModel().getMesh(i).getFace(j).getVertexCount();
			}	
		}

		//Declare the shitty ass directly allocated buffer
		ByteBuffer vertexbuffer = ByteBuffer.allocateDirect(3*vertexcount*4);
		vertexbuffer.order(ByteOrder.nativeOrder());
		ByteBuffer indexbuffer = ByteBuffer.allocateDirect(3*vertexcount*4);
		indexbuffer.order(ByteOrder.nativeOrder());
		
		indexbuffer.clear();
		
		//Insert the vectors in to our fucking awful directly allocated buffer
		
		//for every mesh
		for(int i=0;i<e.getModel().getMeshCount();i++) {
			//for every face
			for(int j=0;j<e.getModel().getMesh(i).getFaceCount();j++) {
				//for every vertex
				for(int k=0;k<e.getModel().getMesh(i).getFace(j).getVertexCount();k++) {
					//get current vertex
					Vector3f v = e.getModel().getMesh(i).getFace(j).getVertex(k);
					
					//shove x,y,z into buffer
					vertexbuffer.putFloat(v.x);
					vertexbuffer.putFloat(v.y);
					vertexbuffer.putFloat(v.z);
					
					//push indices for this vertex 
					indexbuffer.putInt(index);
					index++;
					indexbuffer.putInt(index);
					index++;
					indexbuffer.putInt(index);
					index++;
				}
			}	
		}
		
		//flip that shit dawg; Just the index buffer, not the vertex buffer
		//I mean, because that makes shit loads of sense...
		indexbuffer.flip();
		//vertexbuffer.flip();
		
		//Oh great, let's throw data at this undocumented function and hope
		//something useful comes out the other side!
			//*Clearly you are frustrated --Robert
			//** I'M PISSED --Adam
		TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray(
			trianglecount,
			indexbuffer,
			3*4,//indexStride,
			vertexcount,
			vertexbuffer,
			3*4//vertStride
		);
		
		
		boolean useQuantizedAabbCompression = true;
		//Put vertices in triangle mesh array
		meshshape = new BvhTriangleMeshShape(indexVertexArrays, useQuantizedAabbCompression);
		
		//Create an optimized Bvh to reduce the vertex set of the original mesh
		OptimizedBvh optimizedmesh = new OptimizedBvh();
		optimizedmesh.build(
			meshshape.getMeshInterface(),
			useQuantizedAabbCompression,
			worldAabbMin,
			worldAabbMax
		);
		
		//Set the reduced set back to the mesh
		meshshape.setOptimizedBvh(optimizedmesh);

		//Override the original shape with the new one
		e.setCollisionShape(meshshape);
	}
	
	public void drag(Camera camera) {
		if (dynamicsWorld != null) {
			CollisionWorld.ClosestRayResultCallback rayCallback = new CollisionWorld.ClosestRayResultCallback(cameraPosition, rayTo);
			dynamicsWorld.rayTest(camera.getPosition(), camera.getFocusPosition(), rayCallback);
			if (rayCallback.hasHit()) {
				RigidBody body = RigidBody.upcast(rayCallback.collisionObject);
				if (body != null) {
					// other exclusions?
					if (!(body.isStaticObject() || body.isKinematicObject())) {
						pickedBody = body;
						pickedBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
	
						Vector3f pickPos = new Vector3f(rayCallback.hitPointWorld);
	
						Transform tmpTrans = body.getCenterOfMassTransform(new Transform());
						tmpTrans.inverse();
						Vector3f localPivot = new Vector3f(pickPos);
						tmpTrans.transform(localPivot);
	
						Point2PointConstraint p2p = new Point2PointConstraint(body, localPivot);
						p2p.setting.impulseClamp = mousePickClamping;
	
						dynamicsWorld.addConstraint(p2p);
						pickConstraint = p2p;
						// save mouse position for dragging
						BulletStats.gOldPickingPos.set(rayTo);
						Vector3f eyePos = new Vector3f(cameraPosition);
						Vector3f tmp = new Vector3f();
						tmp.sub(pickPos, eyePos);
						BulletStats.gOldPickingDist = tmp.length();
						// very weak constraint for picking
						p2p.setting.tau = 0.1f;
					}
				}
			}
		}
	}
}
