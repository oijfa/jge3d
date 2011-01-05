package physics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.collision.shapes.ShapeHull;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

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
	
	public void reduceHull(Entity e) {
		int vertexcount=0;
		int trianglecount=0;
		CollisionShape meshshape;
		
		//get the total number of vertices so we can declare our stupid ass
		//directly allocated buffer
		for(int i=0;i<e.getModel().getMeshCount()-1;i++) {
			trianglecount += e.getModel().getMesh(i).getFaceCount()-1;
			for(int j=0;j<e.getModel().getMesh(i).getFaceCount()-1;j++) {
				vertexcount += e.getModel().getMesh(i).getFace(j).getVertexCount();
			}	
		}

		//Declare the shitty ass directly allocated buffer
		ByteBuffer vertexbuffer = ByteBuffer.allocateDirect(3*vertexcount*4);
		vertexbuffer.order(ByteOrder.nativeOrder());
		ByteBuffer indexbuffer = ByteBuffer.allocateDirect(3*vertexcount*4);
		indexbuffer.order(ByteOrder.nativeOrder());
		
		//Insert the vectors in to our fucking awful directly allocated buffer
		for(int i=0;i<e.getModel().getMeshCount();i++) {
			for(int j=0;j<e.getModel().getMesh(i).getFaceCount()-1;j++) {
				for(int k=0;k<e.getModel().getMesh(i).getFace(j).getVertexCount()-1;k++) {
					Vector3f v = e.getModel().getMesh(i).getFace(j).getVertex(k);
					vertexbuffer.asFloatBuffer().put(v.x);
					vertexbuffer.asFloatBuffer().put(v.y);
					vertexbuffer.asFloatBuffer().put(v.z);
					indexbuffer.asIntBuffer().put(i*3*j);
					indexbuffer.asIntBuffer().put(i*3*j+1);
					indexbuffer.asIntBuffer().put(i*3*j+2);
				}
			}	
		}
		
		//flip that shit dawg
		vertexbuffer.flip();
		
		//Oh great, let's throw data at this undocumented function and hope
		//something useful comes out the other side!
		TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray(
			trianglecount,
			indexbuffer,
			3*4,//indexStride,
			vertexcount,
			vertexbuffer,
			3*4//vertStride
		);

		//put vertices in triangle mesh array
		boolean useQuantizedAabbCompression = true;
		meshshape = new BvhTriangleMeshShape(indexVertexArrays, useQuantizedAabbCompression);
		
		//create a hull approximation
		ShapeHull hull = new ShapeHull((ConvexShape)meshshape);
		float margin = e.getCollisionShape().getMargin();
		hull.buildHull(margin);
		ConvexHullShape simplifiedConvexShape = new ConvexHullShape(hull.getVertexPointer());
		e.setCollisionShape(simplifiedConvexShape);
	}
}
