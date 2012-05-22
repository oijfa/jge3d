package engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import engine.entity.AIManager;
import engine.entity.Camera;
import engine.entity.Entity;
import engine.entity.EntityCallbackFunctions;
import engine.entity.Actor;
import engine.input.InputMap;
import engine.entity.EntityList;
import engine.physics.Physics;
import engine.render.FixedRenderer;
import engine.render.Model;
import engine.render.ProgrammableRenderer;
import engine.render.RendererInterface;
import engine.render.Shader;
import engine.resource.ResourceManager;
import engine.window.WindowManager;
import engine.window.components.Window;

import com.bulletphysics.collision.dispatch.GhostObject;

public class Engine {
	public static final int FRAMERATE = 60; // fps
	AtomicBoolean finished;

	private final Physics physics;
	private final RendererInterface renderer;

	private Thread physics_thread;
	private Thread render_thread;

	@SuppressWarnings("unused")
	private Camera camera;
	private EntityList entity_list;
	
	private AIManager ai_manager;
	public final ResourceManager resource_manager;

	public void addWindow(Window window, int width, int height) {
		renderer.getWindowManager().addWindow(window, width, height);
	}

	public Engine() {
		resource_manager = new ResourceManager();
		
		finished = new AtomicBoolean(false);
		physics = new Physics();
		System.out.println();
		entity_list = new EntityList(physics);
		
		//Find out what GL capabilities we have
		try {
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String gl_version = GL11.glGetString(GL11.GL_VERSION).split(" ")[0].substring(0, 1);
		Display.destroy();
		
		if(Float.valueOf(gl_version) >= 2.0)
			renderer = new ProgrammableRenderer(entity_list);
		else
			renderer = new FixedRenderer(entity_list);
		renderer.initGL();
		
		setKeyMap("default");
		
		ai_manager = new AIManager();
	}

	public void run() {
		startPhysics();
		startRendering();

		try {
			render_thread.join();
			physics_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* Entity API */
	public Entity addEntity(String name, float mass, boolean collidable, String model_name, String shader_name) {
		Entity ent = new Entity(
			mass, 
			collidable, 
			(Model) resource_manager.getResource(model_name,"models"), 
			(Shader) resource_manager.getResource(shader_name,"shaders")
		);
		ent.setProperty(Entity.NAME, name);
		this.addEntity(ent);
		
		return ent;
	}

	public void addEntity(Entity ent) {
		if( ent.getProperty(Entity.NAME).equals(Camera.CAMERA_NAME)){
			renderer.setCamera((Camera)ent);
			ent.setShouldDraw(false);
		}
		entity_list.addEntity(ent);
	}

	public void updateEntity(Entity ent) {
		entity_list.updateEntity(ent);
	}

	public Entity getEntity(String name) {
		return entity_list.getItem(name);
	}
	
	public Actor getActor(String name) {
		return (Actor)getEntity(name);
	}
	
	public Camera getCamera() {
	    return (Camera) getEntity(Camera.CAMERA_NAME);
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
	
	public boolean setKeyMap(String name){
		boolean ret = false;
		InputMap new_map = (InputMap) resource_manager.getResource(name, "inputmaps");
		new_map.setEntityList(entity_list);
		
		renderer.getWindowManager().setKeyMap(new_map);
		
		ret = true;
		
		return ret;
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
		ai_manager.invokeAllMethodsForAllEnts(this);
	}
	public void physicsOnce(){
		if (entity_list != null && entity_list.physicsQueueSize() > 0) 
			entity_list.parsePhysicsQueue();
		else {
			physics.clientUpdate();
			handleGhostCollisions();
		}
	}

	public Camera addCamera(float mass, boolean collidable, String model_name) {
		Camera camera = new Camera(mass, collidable, (Model) resource_manager.getResource(model_name,"models"));
		addEntity(camera);
		return camera;
	}

	public Actor addActor(String name, float mass, float step_height, String model_name, String shader_name) {
		Actor actor = new Actor(
			mass, 
			step_height, 
			(Model) resource_manager.getResource(model_name,"models"), 
			(Shader) resource_manager.getResource(shader_name,"shaders")
		);
		actor.setProperty(Entity.NAME, name);
		addEntity(actor);
		return actor;
	}
  
  	public Actor addActor(String name, float mass, String model_name, String shader_name) {
		Actor actor = new Actor(
			name, 
			mass, 
			(Model) resource_manager.getResource(model_name,"models"), 
			(Shader) resource_manager.getResource(shader_name, "shaders")
		);
		actor.setProperty(Entity.NAME, name);
		addEntity(actor);
		return actor;
  	}

	public WindowManager getWindowManager() {
		return renderer.getWindowManager();
	}
	  
	public void addAIRoutine(String ent_name, String... script_names) {
		ai_manager.assignScript(ent_name, script_names);
	}
	  
	public void removeAIRoutine(String ent_name, String script_name) {
		ai_manager.unassignScript(ent_name, script_name);
	}
}
