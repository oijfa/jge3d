package physics;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
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
	private List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
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

		dynamicsWorld.setGravity(new Vector3f(0,-100,0));

		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0f;
		
		//Preset the previous time so deltaT isn't enormous on first run
		prev_time = System.nanoTime();
	}
		
	public RigidBody addLevelBlock(float x, float y, float z, float cube_size) {
		//Levels are static since we don't want them moving due to interactions with
		//other physics objects; Static objects must have a mass of 0 and no inertia
		float mass=0.0f;
		
		//Collision shape is a box since a level is just a bunch of boxes
		CollisionShape colShape = new BoxShape(new Vector3f(cube_size, cube_size, cube_size));
		collisionShapes.add(colShape);
	
		// Create level transformation and default position
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(cube_size*x, cube_size*y, cube_size*z);

		//Create a rigid body to represent the object
		RigidBody body = createRigidBody(mass, startTransform, colShape);
	
		dynamicsWorld.addRigidBody(body);
		
		return body;
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
}
