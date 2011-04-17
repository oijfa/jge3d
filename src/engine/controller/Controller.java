/*
 * The thing that controls everything....
 */
package engine.controller;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.Display;

import engine.physics.Physics;

import com.bulletphysics.collision.dispatch.CollisionFlags;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

import engine.entity.Camera;
import engine.entity.EntityList;
import engine.entity.QueueItem;
import engine.render.Renderer;

public class Controller extends Applet{		
	private static final long serialVersionUID = 4458487765324323938L;

	// the game always runs (except when it doesn't)
	private volatile static boolean isRunning = true;
	
	protected Renderer renderer;
	protected Physics physics;
	
	private long frames = 0;

	protected EntityList objectList;
	
	Runnable treeListener;
	
	private Canvas display_parent;
	
	Object renderlock = new Object();

	public Controller(){
		super();
	}

	public void startApp() {
		try {
			startThreads();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Tell game loop to stop running, after which the LWJGL Display will 
	// be destroyed. The main thread will wait for the Display.destroy().
	private void stopLWJGL() {
		isRunning = false;
		try {
			render_thread.join();
			physics_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		
	}

	public void stop() {
		
	}
	
	// Applet Destroy method will remove the canvas, 
	// before canvas is destroyed it will notify stopLWJGL()
	// to stop the main game loop and to destroy the Display
	public void destroy() {
		remove(display_parent);
		super.destroy();
	}
	
	public void init(){
		//Create a container to house the application
		//for applets this is important otherwise the app won't embed
		setLayout(new BorderLayout());
		try {
			display_parent = new Canvas() {
				private static final long serialVersionUID = 1L;
				public final void addNotify() {
					super.addNotify();
					startApp();
				}
				public final void removeNotify() {
					stopLWJGL();
					super.removeNotify();
				}
			};
			display_parent.setSize(getWidth(),getHeight());
			add(display_parent);
			display_parent.setFocusable(true);
			display_parent.requestFocus();
			display_parent.setIgnoreRepaint(true);
			setVisible(true);
		} catch (Exception e) {
			System.err.println(e);
			throw new RuntimeException("Unable to create display");
		}
	}

	private void startThreads() throws Exception {
		//Instantiate Physics first, as it depends on nothing
		physics = new Physics();
		
		//Next is the entity list, since it only depends on the physics
		objectList = new EntityList(physics);
		renderer = new Renderer(objectList, display_parent);
		
		//InitGL
		render_thread.start();
		
		//User overridable call to initialize any functions before the engine starts running
		initialize();
		
		physics_thread.start();
		render_thread.interrupt();
	}
	
	public void initialize() {
	}
	
	public void createCamera(){
		//Make a camera	
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		
		Camera cam = new Camera(0.0, boxShape, false, Config.getFullAssemblyFocus());
		cam.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);
		objectList.enqueuePhysics(cam, QueueItem.ADD);
		
		objectList.parsePhysicsQueue();
	}
	
	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			objectList.parsePhysicsQueue();
			while (isRunning) {
				if(objectList != null && objectList.physicsQueueSize() > 0)
					objectList.parsePhysicsQueue();
				else{
					physics.clientUpdate();
				}
			}
		}
	};

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.initGL();
			try {
				this.join();
			} catch (InterruptedException e) {}
			objectList.parseRenderQueue();
			while (isRunning) {
				if(Display.isCloseRequested())
					isRunning=false;
				if(objectList != null && objectList.renderQueueSize() > 0)
					objectList.parseRenderQueue();
				else
					renderer.draw();
			}
		}
	};
	
	public long getFrames() { return frames; }
	public void resetFrames() { frames = 0;	}

	public static void quit() { isRunning = false;	}
	
	/*
	public void loadLevel() throws Exception{
		//Load some stuff (I would only pick one of the following
		//two methods if I were you)
		loadTestShapes(cam);
		pullModelFiles("resources/models/cathodes/minixgl");
	}

	private void loadTestShapes(Camera cam) {
		physics.setGravity(new Vector3f(0,-10,0));
		
		//Legoman thing
		Parser p = new XGL_Parser();
		//Parser p = new Obj_Parser();
		try{
			//p.readFile("resources/models/misc/singlebox.obj");
			p.readFile("resources/models/misc/singlebox.xgl");
			//p.readFile("resources/models/misc/legoman.xgl");
			//p.readFile("resources/models/misc/10010260.xgl");
			//p.readFile("resources/models/misc/box2.xgl");
			//p.readFile("resources/models/misc/cath.xgl");
			//p.readFile("resources/models/cathodes/0335-CATHODE_ASSEMBLY.obj");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Model loading failed");
		}
		//p.createModel().saveXGL("resources/models/misc/singlebox.xgl");
		BoxShape boxShape = new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f));
		Entity ent = new Entity(0.0f, boxShape, true);
		ent.setModel(p.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,0.0f));
		ent.setCollisionFlags(CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
		//physics.reduceHull(ent);

		objectList.enqueuePhysics(ent, QueueItem.ADD);
		cam.setDistance(20.0f);
		
		cam.focusOn(ent);
		
		for(int i=10;i<100;i+=10) {
			ent = new Entity(5.0f, boxShape, true);
			ent.setModel(p.createModel());
			ent.setPosition(new Vector3f(0.0f,(float)i,0.0f));
			ent.setCollisionFlags(CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
			//physics.reduceHull(ent);
			//ent.setGravity(new Vector3f(0,-1,0));
			objectList.enqueuePhysics(ent, QueueItem.ADD);
		}
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
						Entity ent = new Entity(1.0f, boxShape, false);	
						ent.setModel(p.createModel());	
						//ent.setPosition(new Vector3f(0.0f,0.0f,(float) Math.random()));
						ent.setProperty("name", f.getPath().substring(0,dotPos-1));
						objectList.enqueuePhysics(ent, QueueItem.ADD);
						ent.setShouldDraw(true);
					}
		        }
			}else{
				pullModelFiles(f.getPath());
			}
		}
	}
	
	/* Config file reading */
	
}
