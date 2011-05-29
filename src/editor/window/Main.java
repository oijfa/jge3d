package editor.window;
import javax.vecmath.Vector3f;

import engine.controller.*;
import engine.entity.*;
import com.bulletphysics.collision.shapes.BoxShape;

public class Main extends Controller {
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize() {
		Player player1 = new Player(1.0f, new BoxShape(new Vector3f(1, 1, 1)),0.5f);
		player1.setProperty("name", "grid");
		objectList.enqueuePhysics(player1, QueueItem.ADD);
		objectList.parsePhysicsQueue();

		//Adding windows back in (pretty fucked)
		//while(!renderer.isInitialized()) {}
		
		try {
			renderer.getWindow().addWindow(new GridWindow(5));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createCamera();
		((Camera)objectList.getItem(Camera.CAMERA_NAME)).focusOn(player1);
	}
}


