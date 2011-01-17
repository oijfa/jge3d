package input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.matthiasmann.twl.Event;

import physics.Physics;
import window.Window;

import entity.Camera;
import entity.EntityList;

public class Input {
	private Camera camera;
	private EntityList objectList;
	private Physics physics;
	private Window window;

	//private int deltaX;
	//private int deltaY;

	//Holds Y coordinate that is reversed for raycasting
	private int adjustY;
	
	// ANGLES
	private static final float LEFT_RIGHT_INC = 0.00001f;
	private static final float UP_DOWN_INC = 0.00001f;

	// DISTANCE
	private static final float IN_OUT_INC = 1f;

	public Input(EntityList objectList, Window window) {
		this.objectList = objectList;
		physics = objectList.getPhysics();
		this.window=window;
	}

	public void init() {
		try {
			Keyboard.create();
			// Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Keyboard.poll();
		Keyboard.enableRepeatEvents(false);
	}

	public boolean handleEvent(Event evt) {
		Keyboard.poll();
		if(camera != null) {
			//TODO: THIS MIGHT BE BROKEN
			handleMouse();
			if(Mouse.isInsideWindow()) {
				handleKeyboard();
			}
		} else {
			camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
		}
		
		System.out.println("doing");
		return true;
	}

	private void handleKeyboard() {
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.incrementRotation(LEFT_RIGHT_INC);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.incrementRotation(-LEFT_RIGHT_INC);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.incrementDeclination(UP_DOWN_INC);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.incrementDeclination(-UP_DOWN_INC);
		}
	}

	private void handleMouse(){
		while(Mouse.next())	{
			Mouse.poll();
			
			//update the changes in position
			//deltaX = Mouse.getEventDX();
			//deltaY = Mouse.getEventDY();
			
			//fix mouse coordinates
			adjustY = window.getHeight()-1-Mouse.getY();	
			
			switch(Mouse.getEventButton()) {
				case -1://Mouse Movement
					if(Mouse.isInsideWindow()) {
						//Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
						if(Mouse.isButtonDown(0)) {

						}
						
						if(Mouse.isButtonDown(1)) {
							//Change angle of camera
	
						}
						if(Mouse.isButtonDown(2)) {
							//Pan Z
						}
					}
					break;
				case 0://Left Button
					//if( Mouse.isButtonDown(0) )	{
						physics.drag(
							camera,
							Mouse.getEventButtonState()? 0 : 1,
							camera.getRayTo(Mouse.getX(),adjustY)
						);
					//} else {
						
					//}
					break;
				case 1://Right Button
					if( !(Mouse.isButtonDown(1)) ) {
					}
					break;
				case 2://Middle Button
					if( !(Mouse.isButtonDown(1)) ) {
					}
					break;
			}

			switch(Mouse.getDWheel()) {
				case -120: 
					camera.incrementDistance(IN_OUT_INC);
					break;
				case  120: 
					camera.incrementDistance(-IN_OUT_INC);
					break;
			}
			physics.motionFunc(camera,Mouse.getX(), adjustY);
		}
	}
}
