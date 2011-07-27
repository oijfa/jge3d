package test;

import javax.vecmath.Vector3f;

import engine.Engine;
import engine.entity.*;
import engine.importing.FileLoader;

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
		
		/*
		Terrain terrain = new Terrain(engine);
		terrain.createTerrain(20);
		terrain.lower(5);
		*/
		player = new Player(1.0f, 0.5f,FileLoader.loadFile("resources/models/misc/box.xgl"));
		player.setProperty(Entity.NAME, "player");
		player.setPosition(new Vector3f(5, 3, 0));
		
		model = new Entity(1f, true,FileLoader.loadFile("resources/models/misc/box.xgl"));
		model.setProperty(Entity.NAME, "model1");
		model.setPosition(new Vector3f(6, 0, 0));

		model2 = new Entity(1f, true,FileLoader.loadFile("resources/models/misc/export.xgl"));
		model2.setProperty(Entity.NAME, "model2");
		model2.setPosition(new Vector3f(-6, 0, 0));
		model2.applyImpulse(new Vector3f(2,0,0), new Vector3f(0,0,0));
		
		model3 = new Entity(1f, false,FileLoader.loadFile("resources/models/misc/singlebox.xgl"));
		model3.setProperty(Entity.NAME, "model3");
		model3.setPosition(new Vector3f(0, 0, 0));
		model3.addCollisionFunctions("explode");
		//model.getModel().setTransparent();

		model4 = new Entity(1f, true,FileLoader.loadFile("resources/models/misc/test.xgl"));
		model4.setProperty(Entity.NAME, "model4");
		model4.setPosition(new Vector3f(0, 0, -6));
				
		camera = new Camera(1f, false, FileLoader.loadFile("resources/models/misc/box.xgl"), model);
		camera.setProperty(Entity.NAME, Camera.CAMERA_NAME);
		camera.setDistance(25f);
		camera.setPosition(new Vector3f(0, 0, 6));

		engine.addEntity(model);
		engine.addEntity(model2);
		engine.addEntity(model3);
		engine.addEntity(model4);
		engine.addEntity(camera);
		engine.addEntity(player);
		
		camera.focusOn(model);
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
