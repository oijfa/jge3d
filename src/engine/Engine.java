package engine;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.matthiasmann.twl.ResizableFrame;

import engine.entity.Camera;
import engine.entity.Entity;
import engine.entity.EntityList;
import engine.importing.FileLoader;
import engine.physics.Physics;
import engine.render.Renderer;

import com.bulletphysics.collision.shapes.BoxShape;


public class Engine {
  
  public static final int FRAMERATE = 60; //fps
  AtomicBoolean finished;
  
  private final Physics physics;
  private final Renderer renderer;
  
  private Thread physics_thread;
  private Thread render_thread;
  
  @SuppressWarnings("unused")
  private Camera camera;
  private EntityList entity_list;
  
  public static void main(String args[]) throws LWJGLException{
    Engine r = new Engine();
    r.run();  
    
    r.addEntity("BLAH", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH").setPosition(new Vector3f(0,0,-5));
    
    r.addEntity("BLAH1", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH1").setPosition(new Vector3f(0,0,5));
    
    r.addEntity("BLAH2", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH2").setPosition(new Vector3f(0,-5,0));
    
    r.addEntity("BLAH3", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH3").setPosition(new Vector3f(0,5,0));
    
    r.addEntity("BLAH4", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH4").setPosition(new Vector3f(-5,0,0));
    
    r.addEntity("BLAH5", "resources/models/misc/box2.xgl");
    r.getEntity("BLAH5").setPosition(new Vector3f(5,0,0));
  }

  @SuppressWarnings("unused")
  private void addWindow(ResizableFrame window) {
    renderer.getWindow().addWindow(window);
  }

  public Engine() throws LWJGLException{
    finished = new AtomicBoolean(false);
    physics = new Physics();
    entity_list = new EntityList(physics);
    renderer = new Renderer(entity_list);
  }
  
  public void run() throws LWJGLException{
    startPhysics();
    startRendering();
  }
  
  /* Entity API */
  public void addEntity(String name, String model_location){
    try {
      Entity ent = new Entity(1, new BoxShape(new Vector3f(1f,1f,1f)), true );
      ent.setProperty(Entity.NAME, name);
      ent.setModel(FileLoader.loadFile(model_location));
      entity_list.addEntity(ent);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (LWJGLException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
  
  public Entity getEntity(String name){
    return entity_list.getItem(name);
  }
  
  public void removeEntity(String name){
    entity_list.removeEntity(name);
  }
  
  /* Private Methods */
  private void startRendering() throws LWJGLException {
    renderer.initGL();
    render_thread = new Thread(){
      public void run(){
        
        
        render();
      }//run()
    };//new Thread()
    
    Display.releaseContext();
    render_thread.start();
  }

  private void startPhysics() {
    physics_thread = new Thread() {
      public void run() {
        entity_list.parsePhysicsQueue();
        while (!finished.get()) {
          if(entity_list != null && entity_list.physicsQueueSize() > 0)
            entity_list.parsePhysicsQueue();
          else{
            physics.clientUpdate();
          }
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
        // Always call Window.update(), all the time - it does some behind the
        // scenes work, and also displays the rendered output
        Display.update();
        
        // Check for close requests
        if (Display.isCloseRequested()) {
          finished.set(true);
        } else if (entity_list != null && entity_list.renderQueueSize() > 0){
          entity_list.parseRenderQueue();
        }else if (Display.isActive()) {
          // The window is in the foreground, so we should play the game
          renderer.draw();
          Display.sync(FRAMERATE);
        } else {
          //   The window is not in the foreground, so we can allow other stuff to run and
          // infrequently update
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {}
          // Only bother rendering if the window is visible or dirty
          if (Display.isVisible() || Display.isDirty()) {
            renderer.draw();
          }
        }
      }
    } catch (LWJGLException e1) {
      e1.printStackTrace();
    }
  }
}
