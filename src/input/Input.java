package input;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import entity.Camera;
import entity.Entity;
import entity.EntityList;

public class Input {
	private Camera camera;
	private EntityList objectList;
	private int deltaX;
	private int deltaY;
	
	//ANGLES
	private static final float LEFT_RIGHT_INC = 0.00001f;
	private static final float UP_DOWN_INC = 0.00001f;
	
	//DISTANCE
	private static final float IN_OUT_INC = 0.1f;
	
	public Input (EntityList objectList){
		this.objectList = objectList;
	}
	
	public void init(){
		try {
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Keyboard.poll();
		Keyboard.enableRepeatEvents(false);
	}
	
	public void run(){
		Keyboard.poll();
		if(camera != null) {
			handleMouse();
			if(Mouse.isInsideWindow()) {
				handleKeyboard();
			}
		} else {
			camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
		}
			
	}
	
	private void handleKeyboard(){
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.incrementRotation(LEFT_RIGHT_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			camera.incrementRotation(-LEFT_RIGHT_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			camera.incrementDeclination(UP_DOWN_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.incrementDeclination(-UP_DOWN_INC);
		}
	}
	
	private void handleMouse(){
		while(Mouse.next())	{
			Mouse.poll();
			
			//update the changes in position
			deltaX = Mouse.getEventDX();
			deltaY = Mouse.getEventDY();

			switch(Mouse.getEventButton()) {
				case -1://Mouse Movement
					if(Mouse.isInsideWindow()) {
						//Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
						if(Mouse.isButtonDown(0)) {
							//Pan camera Z
							camera.incrementDistance(-1.0f*deltaY);	
						}
						
						if(Mouse.isButtonDown(1)) {
							//Change angle of camera
	
						}
						
						if(Mouse.isButtonDown(2)) {
							//Change Perspective
							camera.incrementDeclination(-deltaY*.01f);
							camera.incrementRotation(-deltaX*.01f);
						}
					}
					break;
				case 0://Left Button
					if( Mouse.isButtonDown(0) )	{
					} else {
					}
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
		}
	}
}
