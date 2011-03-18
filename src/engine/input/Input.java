package engine.input;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.matthiasmann.twl.Event;

import engine.entity.Camera;
import engine.entity.EntityList;
import engine.physics.Physics;

public abstract class Input {
	protected Camera camera;
	protected EntityList objectList;
	protected Physics physics;
	
	protected Input(EntityList objectList) {
		this.objectList = objectList;
		physics = this.objectList.getPhysics();
	}

	protected void init() {
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
		//Keyboard.poll();
		//Mouse.poll();
		if(camera != null && Mouse.isInsideWindow()) {
			handleMouse(evt);
			handleKeyboard(evt);
		} else {
			camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
		}
		return true;
	}

	protected abstract boolean handleMouse(Event evt);
	protected abstract boolean handleKeyboard(Event evt);
}
