package engine;



import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.matthiasmann.twl.ResizableFrame;

import engine.entity.Camera;
import engine.entity.EntityList;
import engine.physics.Physics;
import engine.render.Renderer;

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
  
  public void setupDisplay() throws LWJGLException{
    renderer.initGL();
  }
  
  public void run() throws LWJGLException{
    startPhysics();
    startRendering();
  }
  
  private void startRendering() throws LWJGLException {
    setupDisplay();
    render_thread = new Thread(){
      public void run(){
        
        
        render();
      }//run()
    };//new Thread()
    
    Display.releaseContext();
    render_thread.start();
  }

  private void startPhysics() {
    //TODO Make the physics actually run
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

  public void render() {
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
          logic();
          renderer.draw();
          Display.sync(FRAMERATE);
        } else {
          //   The window is not in the foreground, so we can allow other stuff to run and
          // infrequently update
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {}
          logic();
   
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
  
  public void logic(){
    //TODO it'd be nice if the user could pass in a ruby proc here >.>
  }
}
