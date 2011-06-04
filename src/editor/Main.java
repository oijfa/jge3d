package editor;
import javax.vecmath.Vector3f;

import editor.window.GridWindow;
import engine.controller.*;
import engine.entity.*;

import com.bulletphysics.collision.shapes.BoxShape;

public class Main extends Controller {
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize() {
		try {
			ParseConfig config = new ParseConfig(objectList);
			config.readConfigFile("resources/models/config_test.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Testing the terrain stuff here
		//Terrain terrain = new Terrain(objectList);
		//terrain.createTerrain(75);
		
		Player player1 = new Player(1.0f, new BoxShape(new Vector3f(1, 1, 1)),0.5f);
		player1.setProperty("name", "player1");
		objectList.enqueuePhysics(player1, QueueItem.ADD);
		objectList.parsePhysicsQueue();

		//Adding windows back in (pretty fucked)
		while(!renderer.isInitialized()) {}
		
		renderer.getWindow().addWindow(new GridWindow(5));
		
		createCamera();
		((Camera)objectList.getItem(Camera.CAMERA_NAME)).focusOn(player1);
	}
}
