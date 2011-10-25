package engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import engine.entity.Camera;
import engine.entity.Entity;
import engine.entity.EntityCallbackFunctions;
import engine.entity.Actor;
import engine.importing.FileLoader;
import engine.input.InputMap;
import engine.input.components.KeyMapException;
import engine.entity.EntityList;
import engine.physics.Physics;
import engine.render.Model;
import engine.render.Renderer;
import engine.render.Shader;
import engine.window.WindowManager;
import engine.window.components.Window;

import com.bulletphysics.collision.dispatch.GhostObject;

public class Engine {
	public static final int FRAMERATE = 60; // fps
	AtomicBoolean finished;
	
	private final HashMap<String,Shader> shaders;
	private final HashMap<String,Model> models;

	private final Physics physics;
	private final Renderer renderer;

	private Thread physics_thread;
	private Thread render_thread;

	@SuppressWarnings("unused")
	private Camera camera;
	private EntityList entity_list;

	public void addWindow(Window window, int width, int height) {
		renderer.getWindowManager().addWindow(window, width, height);
	}

	public Engine() {
		finished = new AtomicBoolean(false);
		physics = new Physics();
		entity_list = new EntityList(physics);
		renderer = new Renderer(entity_list);
		renderer.initGL();
		
		models = new HashMap<String,Model>();
		shaders = new HashMap<String,Shader>();
		
		shaders.put(
		    "default",
		    new Shader()
		);
		
		addKeyMap("default.xml");
	}

	public void run() {
		startPhysics();
		startRendering();
	}

	/* Entity API */
	public Entity addEntity(String name, float mass, boolean collidable, String model_name, String shader_name) {
		Entity ent = new Entity(mass, collidable, models.get(model_name), shaders.get(shader_name));
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
	
	public Shader getShaderByName(String name) {
		return shaders.get(name);
	}
	
	public Camera getCamera() {
    // TODO Auto-generated method stub
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
	
	public boolean addKeyMap(String filename){
	  boolean ret = false;
		try {
			renderer.getWindowManager().setKeyMap(new InputMap(filename,entity_list));
			ret = true;
		} catch (KeyMapException e) {
			// TODO Do something if fails?
			System.out.println("Setting KeyMap failed");
			e.printStackTrace();
		}
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
	Camera camera = new Camera(mass, collidable, models.get(model_name));
	addEntity(camera);
    return camera;
  }

  public Actor addActor(String name, float mass, float step_height, String model_name, String shader_name) {
    Actor actor = new Actor(mass, step_height, models.get(model_name), shaders.get(shader_name));
    actor.setProperty(Entity.NAME, name);
    addEntity(actor);
    return actor;
  }
  
  public boolean addModel(String name, String location, Shader shader){
    boolean ret = false;
    if(!models.keySet().contains(name)){
      models.put(name, FileLoader.loadFile(location));
      models.get(name).setShader(shader);
      models.get(name).createVBO();
      ret = true;
    }
    return ret;
  }
  
  public boolean addModel(String name, String location){
    boolean ret = false;
    if(!models.keySet().contains(name)){
      models.put(name, FileLoader.loadFile(location));
      models.get(name).setShader(shaders.get("default"));
      models.get(name).createVBO();
      ret = true;
    }
    return ret;
  }
  
  public boolean addShader(String name, String location){
    boolean ret = false;
    if(!shaders.keySet().contains(name)){
      shaders.put(name, new Shader(location));
      ret = true;
    }
    return ret;
  }
  
  public Model getModelByName(String name)  {
	  return models.get(name);	  
  }

  public WindowManager getWindowManager() {
	  return renderer.getWindowManager();
  }
}
