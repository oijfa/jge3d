package engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.matthiasmann.twl.ResizableFrame;

import engine.entity.Camera;
import engine.entity.Entity;
import engine.entity.EntityCallbackFunctions;
import engine.importing.FileLoader;
import engine.input.KeyMap;
import engine.input.components.KeyMapException;
import engine.entity.EntityList;
import engine.physics.Physics;
import engine.render.Renderer;

import com.bulletphysics.collision.dispatch.GhostObject;

public class Engine {
	public static final int FRAMERATE = 60; // fps
	AtomicBoolean finished;

	private final Physics physics;
	private final Renderer renderer;

	private Thread physics_thread;
	private Thread render_thread;

	@SuppressWarnings("unused")
	private Camera camera;
	private EntityList entity_list;

	/*
	 * public static void main(String args[]) throws LWJGLException{ Engine r =
	 * new Engine(); r.run();
	 * 
	 * r.addEntity("BLAH", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH").setPosition(new Vector3f(0,0,-5));
	 * 
	 * r.addEntity("BLAH1", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH1").setPosition(new Vector3f(0,0,5));
	 * 
	 * r.addEntity("BLAH2", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH2").setPosition(new Vector3f(0,-5,0));
	 * 
	 * r.addEntity("BLAH3", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH3").setPosition(new Vector3f(0,5,0));
	 * 
	 * r.addEntity("BLAH4", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH4").setPosition(new Vector3f(-5,0,0));
	 * 
	 * r.addEntity("BLAH5", "resources/models/misc/box2.xgl");
	 * r.getEntity("BLAH5").setPosition(new Vector3f(5,0,0)); }
	 */

	public void addWindow(ResizableFrame window, int width, int height) {
		renderer.getWindow().addWindow(window, width, height);
	}

	public Engine() {
		finished = new AtomicBoolean(false);
		physics = new Physics();
		entity_list = new EntityList(physics);
		renderer = new Renderer(entity_list);
		renderer.initGL();
		
		addKeyMap("default.xml");
	}

	public void run() {
		startPhysics();
		startRendering();
	}

	/* Entity API */
	public void addEntity(String name, String model_location) {
		Entity ent = new Entity(1, true, FileLoader.loadFile(model_location));
		ent.setProperty(Entity.NAME, name);
		this.addEntity(ent);
	}

	public void addEntity(Entity ent) {
		if( ent.getProperty(Entity.NAME).equals(Camera.CAMERA_NAME)){
			renderer.setCamera((Camera)ent);
		}
		entity_list.addEntity(ent);
	}

	public void updateEntity(Entity ent) {
		entity_list.updateEntity(ent);
	}

	public Entity getEntity(String name) {
		return entity_list.getItem(name);
	}

	public void removeEntity(String name) {
		entity_list.removeEntity(name);
	}
	
	public EntityList getEntityList() {
		return entity_list;
	}

	/* Private Methods */
	private void startRendering() {
		render_thread = new Thread() {
			public void run() {
				render();
			}// run()
		};// new Thread()

		try {
			Display.releaseContext();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		render_thread.start();
	}

	private void startPhysics() {
		physics_thread = new Thread() {
			public void run() {
				entity_list.parsePhysicsQueue();
				while (!finished.get()) {
					physicsOnce();
				}
			}
		};
		physics_thread.start();
	}

	private void render() {
		try {
			Display.makeCurrent();
			entity_list.parseRenderQueue();

			while (!finished.get()) {
				renderOnce();
			}
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void addKeyMap(String filename){
		try {
			renderer.getWindow().setKeyMap(new KeyMap(filename,entity_list));
		} catch (KeyMapException e) {
			// TODO Do something if fails?
			System.out.println("Setting KeyMap failed");
			e.printStackTrace();
		}
	}
	
	private void handleGhostCollisions() {
		for(Entity entity : entity_list.getEntities()){
			if( (Boolean)entity.getProperty(Entity.COLLIDABLE) == false){
				GhostObject ghost = (GhostObject) entity.getCollisionObject();
				for(int i=0;i<ghost.getNumOverlappingObjects();i++){
					entCollidedWith(entity, entity_list.getItem(ghost.getOverlappingObject(i)));
				}
			}
		}
	}
	
	private void entCollidedWith(Entity source, Entity collided_with){
		ArrayList<Method> methods = source.getCollisionFunctions();
		for(Method method : methods){
			try {
				method.invoke(EntityCallbackFunctions.class, source, collided_with, this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void renderOnce(){
		// Check for close requests
		if (Display.isCloseRequested()) {
			finished.set(true);
		} else if (entity_list != null && entity_list.renderQueueSize() > 0) {
			entity_list.parseRenderQueue();
		} else if (Display.isActive()) {
			// The window is in the foreground, so we should play the
			// game
			renderer.draw();
			// Display.sync(FRAMERATE);
		} else {
			// The window is not in the foreground, so we can allow
			// other stuff to run and
			// infrequently update
			/*
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
			*/
			// Only bother rendering if the window is visible or dirty
			if (Display.isVisible() || Display.isDirty()) {
				renderer.draw();
			}
		}
	}
	public void physicsOnce(){
		if (entity_list != null && entity_list.physicsQueueSize() > 0) 
			entity_list.parsePhysicsQueue();
		else {
			physics.clientUpdate();
			handleGhostCollisions();
		}
	}
}
