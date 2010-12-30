/*
 * The thing that controls everything....
 */
package controller;

import importing.Parser;
import importing.XGL_Parser;
import input.Input;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DefaultMotionState;

import entity.Camera;
import entity.Entity;
import entity.EntityList;
import render.Renderer;

public class Controller {
	// the game always runs (except when it doesn't)
	private static boolean isRunning = true;
	private static Controller uniqueInstance = null;
	private Queue<Command> controller_queue = new LinkedList<Command>();
	private static Controller controller;
	
	private Renderer renderer;
	private Physics physics;
	
	private long frames = 0;
	private Input input;

	private EntityList objectList;
	
	public static void main(String[] args) {
		controller = Controller.getInstance();
	}

	public static Controller getInstance() {
		if( uniqueInstance == null)
			uniqueInstance = new Controller();
		
		return uniqueInstance;
	}
	
	private Controller() {
		objectList = new EntityList();
		loadLevel();
		// controller_thread.start();
		// input_thread.start();
		
	}

	private void start() {
		input_thread.start();
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

	
	// Create the Input Listening thread
	Thread input_thread = new Thread() {
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			System.out.println("SLEEP");
			input = new Input(objectList);
			while(isRunning){
				input.run();
			}
		}
	};
	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			physics = new Physics();
			while (isRunning) {
				// Update the physics world
				physics.clientUpdate();
				// Rejoin the controller thread
			}
		}
	};

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer = new Renderer(objectList);
			input_thread.interrupt();
			while (isRunning) {
				renderer.draw();
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
		Entity ent;

		//Physics.getInstance().getDynamicsWorld().setGravity(new Vector3f(0.0f,-10.0f,0.0f));
		
		Parser p = new XGL_Parser();
		try{
			//p.readFile("./lib/legoman.xgl");
			p.readFile("./lib/10010260.xgl");
			//p.readFile("./lib/box2.xgl");
			//p.readFile("./lib/cath.xgl");
		}catch(Exception e){
			//TODO:  What to do here?
		}
		
		//Make a camera
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Camera(1.0f, new DefaultMotionState(), boxShape, false, objectList);
		ent.setPosition(new Vector3f(0,0,-15));
		//ent.setLinearVelocity(new Vector3f(10,10,10));
		
		objectList.addItem(ent);
		//ent.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));
		
		//Make a cathode
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(1.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,0.0f));
		objectList.addItem(ent);
		System.out.println(ent.getProperty("name"));
		//ent.applyImpulse(new Vector3f(100,0,0), new Vector3f(0,-10,0));
		//objectList.getItem("ent2").applyCentralImpulse(new Vector3f(1.0f, 1.0f, 0.5f));
		/*
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(10.0f,25.0f,0.0f));
		objectList.addItem(ent);
		System.out.println(ent.getProperty("name"));
		
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(-10.0f,25.0f,0.0f));
		objectList.addItem(ent);
		System.out.println(ent.getProperty("name"));
		
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(0.0f,25.0f,10.0f));
		objectList.addItem(ent);
		System.out.println(ent.getProperty("name"));
		
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(0.0f,25.0f,-10.0f));
		objectList.addItem(ent);
		System.out.println(ent.getProperty("name"));
		*/
	}
	
	public Physics getPhysics() {
		return physics;
	}
}
