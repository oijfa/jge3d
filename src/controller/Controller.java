/*
 * The thing that controls everything....
 */
package controller;

import java.applet.Applet;

import importing.Obj_Parser;
import importing.Parser;

import importing.XGL_Parser;

import javax.vecmath.Vector3f;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DefaultMotionState;

import entity.Camera;
import entity.Entity;
import entity.EntityList;
import render.Renderer;

public class Controller extends Applet{
	private static final long serialVersionUID = 4458487765324323938L;

	// the game always runs (except when it doesn't)
	private static boolean isRunning = true;
	
	private Renderer renderer;
	private Physics physics;
	
	private long frames = 0;

	private EntityList objectList;
	
	public static void main(String[] args) throws Exception {
		Applet app = new Controller();
		app.init();
	}
	
	public void init(){
		try {
			startThreads();
			loadLevel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Controller(){	}

	private void startThreads() {
		//Instantiate Physics first, as it depends on nothing
		physics = new Physics();
		physics_thread.start();
		
		//Next is the entity list, since it only depends on the physics
		objectList = new EntityList(physics);
		
		//Renderer has to be after entity list
		renderer = new Renderer(objectList);
		render_thread.start();
	}

	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			while (isRunning) {
				if(objectList != null && objectList.requiresLock())
					objectList.parseQueue();
				else
					physics.clientUpdate();
			}
		}
	};

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.initGL();
			while (isRunning) {
				renderer.draw();
			}
		}
	};
	
	public long getFrames() { return frames; }
	public void resetFrames() {	frames = 0;	}
	
	public static void quit() { isRunning = false;	}
	
	public void loadLevel() throws Exception{
		Entity ent;
		Camera cam;

		//Physics.getInstance().getDynamicsWorld().setGravity(new Vector3f(0.0f,-10.0f,0.0f));
		
		//Make a camera
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		cam = new Camera(0.0f, new DefaultMotionState(), boxShape, false);
		//ent.setLinearVelocity(new Vector3f(10,10,10));
		objectList.enqueue(cam);
		//ent.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));

		//Make a cathode
		Parser obj_parser = new Obj_Parser();
		try{
			//p.readFile("./lib/legoman.xgl");
			//p.readFile("./lib/10010260.xgl");
			//p.readFile("./lib/box2.xgl");
			
			//p.readFile("./lib/cath.xgl");
			//p.readFile("resources/Models/0335-CATHODE_ASSEMBLY.obj");
			obj_parser.readUrl("http://192.168.143.17/ivec/lib/Models/0335-CATHODE_ASSEMBLY.obj");
			//p.readFile("resources/Models/radar.obj");
		}catch(Exception e){
			//TODO:  What to do here?
			e.printStackTrace();
		}
		boxShape = new BoxShape(new Vector3f(1, 1, 1));
		ent = new Entity(1.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(obj_parser.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,-20.0f));
		//physics.reduceHull(ent);
		objectList.enqueue(ent);
		cam.setDistance(25.0f);
		cam.focusOn(ent);

		//Box thing
		XGL_Parser xgl_parser = new XGL_Parser();
		try{
			//p.readFile("./lib/legoman.xgl");
			//p.readFile("./lib/10010260.xgl");
			xgl_parser.readFile("./lib/box2.xgl");
			//p.readFile("./lib/cath.xgl");
			//p.readFile("resources/Models/0335-CATHODE_ASSEMBLY.obj");
		}catch(Exception e){
			//TODO:  What to do here?
		}
		boxShape = new BoxShape(new Vector3f(2, 2, 2));
		ent = new Entity(1.0f, new DefaultMotionState(), boxShape, false);
		ent.setModel(xgl_parser.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,0.0f));
		objectList.enqueue(ent);
		physics.reduceHull(ent);
	}
}
