/*
 * The thing that controls everything....
 */
package controller;

import java.applet.Applet;

import java.io.File;

import importing.Obj_Parser;
import importing.Parser;
import importing.XGL_Parser;
import input.Input;

import javax.vecmath.Vector3f;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.DefaultMotionState;

import entity.Camera;
import entity.Entity;
import entity.EntityList;
import render.Renderer;

@SuppressWarnings("serial")
public class Controller extends Applet{
	// the game always runs (except when it doesn't)
	private static boolean isRunning = true;
	
	private Renderer renderer;
	private Physics physics;
	
	private long frames = 0;
	private Input input;

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
		
		//Input has to be after entity list and after the render thread has been started (Display must be created)
		//input = new Input(objectList);
		//input_thread.start();
	}
	
	/* THREAD DEFINITIONS */
	// Create the Input Listening thread
	Thread input_thread = new Thread() {
		public void run() {
			//Wait for display to be created
			try {
				render_thread.join();
			} catch (InterruptedException e) {/*Nothing to do, render thread is telling us its done creating display*/}
			input.init();
			while(isRunning){
				input.run();
			}
		}
	};
	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			while (isRunning) {
				try{
					physics.clientUpdate();
				}catch(Exception e){
					System.out.println("-");
				}
			}
		}
	};

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.initGL();
			input_thread.interrupt(); //If input thread is waiting (it should be) let it go
			while (isRunning) {
				renderer.draw();
			}
		}
	};
	
	public long getFrames() { return frames; }
	public void resetFrames() {	frames = 0;	}
	
	public static void quit() { isRunning = false;	}
	
	public void loadLevel() throws Exception{
		//Make a camera	
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		Camera cam = new Camera(0.0f, new DefaultMotionState(), boxShape, false);
		objectList.addItem(cam, cam);
		
		pullModelFiles("resources/Models");
	}
	
	private void pullModelFiles(String filename) throws Exception{
		File dir = new File(filename);
		File[] subFiles;
		subFiles = dir.listFiles();
		
		for( File f: subFiles){
			if(!f.isDirectory()){
				//create model
				int dotPos = f.getPath().lastIndexOf(".");
		        String extension = f.getPath().substring(dotPos);
		        if( extension.equals(".xgl") || extension.equals(".obj")){
		        	Parser p;
		        	XGL_Parser xparse = new XGL_Parser();
		    		Obj_Parser oparse = new Obj_Parser();
					if( extension.equals(".xgl")){
						xparse.readFile(f.getPath());
						p = xparse;
					} else {
						oparse.readFile(f.getPath());
						p = oparse;
					}
					if( p != null){
						//Make a cathode	
						BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
						Entity ent = new Entity(0.0f, new DefaultMotionState(), boxShape, false);	
						ent.setModel(p.createModel());	
						ent.setPosition(new Vector3f(0.0f,0.0f,(float) Math.random()));
						ent.setProperty("name", f.getPath().substring(0,dotPos-1), ent);
						objectList.addItem(ent, ent);
						ent.setShouldDraw(false);
					}
		        }
			}else{
				pullModelFiles(f.getPath());
			}
		}
	}
}
