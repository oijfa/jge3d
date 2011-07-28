package test;

import javax.vecmath.Vector3f;

import engine.Engine;
import engine.entity.*;
import engine.terrain.Terrain;

public class Main {

	private Engine engine;

	private Player player;
	private Entity model;
	private Entity model2;
	private Entity model3;
	private Entity model4;
	private Camera camera;

	public static void main(String args[]) {
		boolean multiThreaded = false;
		
		Main m = new Main();
		if(multiThreaded == true)
			m.runMultiThread();
		else
			m.runSingleThread();
	}

	public Main() {
		engine = new Engine();
		
		engine.addModel("box", "resources/models/misc/box.xgl");
		engine.addModel("test", "resources/models/misc/test.xgl");
		engine.addModel("export", "resources/models/misc/export.xgl");
		engine.addModel("singlebox", "resources/models/misc/singlebox.xgl");
		
		Terrain terrain = new Terrain(engine);
		terrain.createTerrain(20);
		terrain.lower(10);
		
		engine.addPlayer("player", 1.0f, 0.5f, "box", "default");
		player = (Player) engine.getEntity("player");
		player.setPosition(new Vector3f(5, 3, 0));
		player.setDamping(0.5f, 0.2f);
		
		engine.addEntity("model1", 1.0f, true, "box", "default");
		model = engine.getEntity("model1");
		model.setProperty(Entity.NAME, "model1");
		model.setPosition(new Vector3f(6, 0, 0));

		engine.addEntity("model2", 1f, true, "export", "default");
		model2 = engine.getEntity("model2");
		model2.setPosition(new Vector3f(-6, 0, 0));
		model2.applyImpulse(new Vector3f(2,0,0), new Vector3f(0,0,0));
		
		engine.addEntity("model3", 1f, false, "singlebox", "default");
		model3 = engine.getEntity("model3");
		model3.setPosition(new Vector3f(0, 0, 0));
		model3.addCollisionFunctions("explode");
		//model.getModel().setTransparent();

		engine.addEntity("model4", 1f, true, "test", "default");
		model4 = engine.getEntity("model4");
		model4.setProperty(Entity.NAME, "model4");
		model4.setPosition(new Vector3f(0, 0, -6));
				
		engine.addCamera(1f, false, "box");
		camera = engine.getCamera();
		camera.setDistance(25f);
		camera.setPosition(new Vector3f(0, 0, 6));
		camera.focusOn(player);
	}

	public void runMultiThread() {
		engine.run();
	}
	
	public void runSingleThread() {
		while(true){
			engine.renderOnce();
			engine.physicsOnce();
		}
	}
}
