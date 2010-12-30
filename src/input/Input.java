package input;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import entity.Camera;
import entity.Entity;
import entity.EntityList;

public class Input {
	private Camera camera;
	private Vector3f cam_velocity;

	public Input (EntityList objectList){
		try {
			Keyboard.create();
			//Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	cam_velocity = new Vector3f();
	Keyboard.poll();
	Keyboard.enableRepeatEvents(false);
	camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
	camera.setFocusEntity("ent2");
	}
	
	public void run(){
		Keyboard.poll();
		// read keyboard and mouse
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.applyCentralImpulse(new Vector3f(-100,0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			camera.applyCentralImpulse(new Vector3f(100,0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			camera.applyCentralImpulse(new Vector3f(0,100,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.applyCentralImpulse(new Vector3f(0,-100,0));
		}


		camera.activate();
		//checks the speed of the camera so it doesn't fly off.
		if(camera.getVelocity().x > 15 || camera.getVelocity().x < -15){
			cam_velocity.x = 0;
		}
		if(camera.getVelocity().y > 15 || camera.getVelocity().y < -15){
			cam_velocity.y = 0;
		}
		if(camera.getVelocity().z > 15 || camera.getVelocity().z < -15){
			cam_velocity.z = 0;
		}
		//assigns the camera a reasonable speed.
		camera.setVelocity(cam_velocity);
		// Input.getInstance().updateInput();
	}
}
