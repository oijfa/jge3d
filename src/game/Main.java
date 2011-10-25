package game;

import javax.vecmath.Vector3f;

import engine.Engine;
import engine.entity.Camera;
import engine.entity.Actor;
import engine.importing.FileLoader;

import game.gui.RotationMenu;

public class Main {
	private Engine engine;

	public static void main(String args[]) {
		Main m = new Main();
		m.run();
	}

	public Main() {
		engine = new Engine();

		// Testing the terrain stuff here
		//Terrain terrain = new Terrain(engine);
		//terrain.createTerrain(75);

		engine.addActor("player1", 1.0f, 0.5f, "resources/models/misc/box.xgl", "default");
		Actor player1 = (Actor) engine.getEntity("player1");

		Camera camera = new Camera(1f, false, FileLoader.loadFile("resources/models/misc/box.xgl"), player1);
		camera.setProperty("name", "camera");
		camera.setPosition(new Vector3f(0, 0, 0));
		camera.setDistance(20f);

		try {
			engine.addWindow(new RotationMenu(), 400, 400);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run() {
		engine.run();
	}
}
