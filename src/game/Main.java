package game;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;

import engine.controller.Controller;
import engine.controller.ParseConfig;
import engine.entity.Camera;
import engine.entity.Player;
import engine.entity.QueueItem;
import engine.terrain.Terrain;
import game.gui.RotationMenu;

public class Main extends Controller {
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize() {
		//Load Roberts weird config file deal
		
		try {
			ParseConfig config = new ParseConfig(objectList);
			config.readConfigFile("resources/models/config_test.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Testing the terrain stuff here
		Terrain terrain = new Terrain(objectList);
		terrain.createTerrain(75);

		createCamera();

		Player player1 = new Player(1.0f,  new BoxShape(new Vector3f(1, 1, 1)),0.5f);
		player1.setProperty("name", "player1");
		objectList.enqueuePhysics(player1, QueueItem.ADD);
		objectList.parsePhysicsQueue();
		((Camera)objectList.getItem(Camera.CAMERA_NAME)).focusOn(player1);
		
		//Adding windows back in (pretty fucked)
		while(!renderer.isInitialized()) {}
		System.out.println("hurr");
		System.out.println(renderer.isInitialized());
		try {
			renderer.getWindow().addWindow(new RotationMenu(),400,400);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


