/*
 * The thing that controls everything....
 */
package controller;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import javax.vecmath.Vector3f;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import entity.Entity;
import entity.EntityList;
import render.Renderer;

public class Controller {
	// the game always runs (except when it doesn't)
	private static boolean isRunning = true;
	private static Controller uniqueInstance = new Controller();
	private Queue<Command> controller_queue = new LinkedList<Command>();
	private static Controller controller;
	private Renderer renderer;
	private long frames = 0;

	private EntityList objectList;
	
	public static void main(String[] args) {
		controller = Controller.getInstance();
		controller.start();
	}

	public static Controller getInstance() {
		return uniqueInstance;
	}
	
	private Controller() {
		objectList = new EntityList();
		loadLevel();
		// controller_thread.start();
		// input_thread.start();
		
	}

	private void start() {
		renderer = new Renderer();
		render_thread.start();
		physics_thread.start();
	}

	public void run_queue() {
		Command commandToRunCommand;
		try {
			for (int i = 0; i < controller_queue.size(); i++) {
				commandToRunCommand = controller_queue.poll();
				Method methodToInvoke = commandToRunCommand
						.getClassInstance()
						.getClass()
						.getDeclaredMethod(
								commandToRunCommand.getMethodToInvoke());
				methodToInvoke.invoke(commandToRunCommand.getClassInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		++frames;
	}

	//TODO:  MAYBE GET RID OF THIS SHIT D:!~!!!!!
	//===============================
	/*
	public void monitor() {}
	// Create the Controller Listening thread
	Thread controller_thread = new Thread() {
		public void run() {
			while (isRunning) {
				Controller.getInstance().monitor();
			}
		}
	};
	*/
	//=================================

	//TODO:  Reimplement this block
	/*
	// Create the Input Listening thread
	Thread input_thread = new Thread() {
		public void run() {
			while (isRunning) {
				// read keyboard and mouse
				// Input.getInstance().updateInput();
			}
		}
	};
	*/
	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			while (isRunning) {
				// Update the physics world
				Physics.getInstance().clientUpdate();

				// Rejoin the controller thread
			}
		}
	};
	// };
	

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.initGL();
			while (isRunning) {
				renderer.draw(objectList);
			}
		}
	};

	

	public void enqueue(Object classInstance, String methodToInvoke) {
		controller_queue.add(new Command(classInstance, methodToInvoke));
	}

	public Boolean hasQueuedItems() {
		if (controller_queue.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public long getFrames() {
		return frames;
	}

	public void resetFrames() {
		frames = 0;
	}

	public static void quit() {
		isRunning = false;
	}

	public static boolean getRunning() {
		return isRunning;
	}
	
	public void loadLevel(){
		//TODO: Make a spinny triforce
		boolean ent1;
		boolean ent2;
		boolean ent3;
		float temp = 0.3f;
		Physics.getInstance().getDynamicsWorld().setGravity(new Vector3f(0,0,0));
		
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent1 = objectList.addItem(new Entity(1.0f, new DefaultMotionState(), boxShape, false));
		objectList.getItem("ent1").setPosition(new Vector3f(0,0,5));
		//objectList.getItem("ent1").applyTorque(new Vector3f(temp,0,0));
		objectList.getItem("ent1").applyImpulse(new Vector3f(temp,0,0),new Vector3f(1,0,0));
		Physics.getInstance().addEntity(objectList.getItem("ent1"));
		
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent2 = objectList.addItem(new Entity(1.0f, new DefaultMotionState(), boxShape, false));
		objectList.getItem("ent2").setPosition(new Vector3f(0,5,0));
		//objectList.getItem("ent2").applyTorque(new Vector3f(temp,0,0));
		objectList.getItem("ent2").applyImpulse(new Vector3f(temp,0,0),new Vector3f(1,0,0));

		Physics.getInstance().addEntity(objectList.getItem("ent2"));
		
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent3 = objectList.addItem(new Entity(1.0f, new DefaultMotionState(), boxShape, false));
		objectList.getItem("ent3").setPosition(new Vector3f(5,0,0));
		//objectList.getItem("ent3").applyTorque(new Vector3f(temp,0,0));
		objectList.getItem("ent3").applyImpulse(new Vector3f(temp,0,0),new Vector3f(1,0,0));
		Physics.getInstance().addEntity(objectList.getItem("ent3"));
		
		System.out.println("ent1: " + String.valueOf(ent1));
		System.out.println("ent2: " + String.valueOf(ent2));
		System.out.println("ent3: " + String.valueOf(ent3));
		System.out.println("Size: " + String.valueOf(objectList.size()));
	}
}
