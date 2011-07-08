package test;

import javax.vecmath.Vector3f;

//import editor.window.ToolBox;
import engine.Engine;
import engine.entity.*;
import engine.importing.FileLoader;

import com.bulletphysics.collision.shapes.BoxShape;

public class Main {

	private Engine engine;


	private Entity model;
	private Camera camera;

	public static void main(String args[]) {
		Main m = new Main();
		m.run();
	}

	public Main() {
		engine = new Engine();

		model = new Entity(1f, new BoxShape(new Vector3f(1, 1, 1)), true);
		model.setModel(FileLoader.loadFile("resources/models/misc/box.xgl"));
		model.setProperty(Entity.NAME, "model");
		model.setPosition(new Vector3f(0, 0, -20));

		camera = new Camera(1d, new BoxShape(new Vector3f(1, 1, 1)), false, model);
		camera.setProperty(Entity.NAME, Camera.CAMERA_NAME);
		camera.setDistance(20f);

		camera.focusOn(model);
		
		engine.addEntity(model);
		engine.addEntity(camera);
		
		engine.addKeyMap("keymap.txt");
	}

	public void run() {
		engine.run();
	}
}
