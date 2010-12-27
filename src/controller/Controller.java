/*
 * The thing that controls everything....
 */
package controller;

import importing.Parser;
import importing.XGL_Parser;

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
	private static Controller uniqueInstance = null;
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
		Entity ent;
		/*
		Entity ent1;
		Entity ent2;
		Entity ent3;
		*/
		
		Physics.getInstance().getDynamicsWorld().setGravity(new Vector3f(0.0f,-1.0f,0.0f));
		
		Parser p = new XGL_Parser();
		try{
			//p.readFile("./lib/legoman.xgl");
			p.readFile("./lib/10010260.xgl");
			//p.readFile("./lib/box.xgl");
			//p.readFile("./lib/cath.xgl");
		}catch(Exception e){
			//TODO:  What to do here?
		}
		
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		for(int i = 1; i < 100; i++){
			ent = new Entity(1.0f, new DefaultMotionState(), boxShape, false);
			ent.setModel(p.createModel());
			objectList.addItem(ent);
			objectList.getItem("ent" + String.valueOf(i)).setPosition(new Vector3f(0.0f,1.0f+i*100,0.0f));
			Physics.getInstance().addEntity(objectList.getItem("ent" + String.valueOf(i)));
			
			boxShape = new BoxShape(new Vector3f(1, 1, 1));
		}
		
		ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(p.createModel());
		objectList.addItem(ent);
		
		objectList.getItem("ent100").setPosition(new Vector3f(0.0f,0.0f,0.0f));
		
		Physics.getInstance().addEntity(objectList.getItem("ent100"));
	}
}
