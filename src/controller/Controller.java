/*
 * The thing that controls everything....
 */
package controller;

import java.applet.Applet;

import java.io.File;

import importing.Obj_Parser;
import importing.Parser;
import importing.XGL_Parser;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.Display;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

import entity.Camera;
import entity.Entity;
import entity.EntityList;
import entity.QueueItem;
import render.Renderer;

public class Controller extends Applet{
	private static final long serialVersionUID = 4458487765324323938L;

	// the game always runs (except when it doesn't)
	private volatile static boolean isRunning = true;
	
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
				if(objectList != null && objectList.queueSize() > 0)
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
				if(Display.isCloseRequested())
					isRunning=false;
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

		Camera cam = new Camera(0.0f, boxShape, false);
		objectList.enqueue(cam, QueueItem.ADD);
		
		//Load some stuff (I would only pick one of the following
		//two methods if I were you)
		loadTestShapes(cam);
		//pullModelFiles("resources/models/cathodes");
	}
	
	private void loadTestShapes(Camera cam) {
		physics.setGravity(new Vector3f(0,-10,0));
		
		//Legoman thing
		XGL_Parser xgl_parser = new XGL_Parser();
		try{
			//xgl_parser.readFile("resources/models/misc/legoman.xgl");
			//xgl_parser.readFile("resources/models/misc/10010260.xgl");
			xgl_parser.readFile("resources/models/misc/box2.xgl");
			//p.readFile("resources/models/misc/cath.xgl");
			//p.readFile("resources/models/misc/0335-CATHODE_ASSEMBLY.obj");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Model loading failed");
		}
		
		BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		Entity ent = new Entity(0.0f, boxShape, false);
		ent.setModel(xgl_parser.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,0.0f));
		//physics.reduceHull(ent);

		objectList.enqueue(ent, QueueItem.ADD);
		cam.setDistance(20.0f);

		cam.focusOn(ent);
		
		for(int i=10;i<15;i+=10) {
			ent = new Entity(10.0f, boxShape, false);
			ent.setModel(xgl_parser.createModel());
			ent.setPosition(new Vector3f(0.0f,(float)i,0.0f));
			//physics.reduceHull(ent);
			objectList.enqueue(ent, QueueItem.ADD);
		}

		boxShape = new BoxShape(new Vector3f(2, 2, 2));
		ent = new Entity(1.0f, boxShape, false);
		ent.setModel(xgl_parser.createModel());
		ent.setPosition(new Vector3f(-5.0f,0.0f,-10.0f));
		//physics.reduceHull(ent);
		objectList.enqueue(ent, QueueItem.ADD);

	}
	
	@SuppressWarnings("unused")
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
						Entity ent = new Entity(0.0f, boxShape, false);	
						ent.setModel(p.createModel());	
						ent.setPosition(new Vector3f(0.0f,0.0f,(float) Math.random()));
						ent.setProperty("name", f.getPath().substring(0,dotPos-1));
						objectList.enqueue(ent, QueueItem.ADD);
						ent.setShouldDraw(false);
					}
		        }
			}else{
				pullModelFiles(f.getPath());
			}
		}
	}
}
