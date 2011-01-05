package input;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import entity.Camera;
import entity.EntityList;

public class Input {
	private Camera camera;
	private EntityList objectList;
	
	public Input (EntityList objectList){
		this.objectList = objectList;
	}
	
	public void init(){
		try {
			Keyboard.create();
			//Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Keyboard.poll();
		Keyboard.enableRepeatEvents(false);
	}
	
	public void run(){
		Keyboard.poll();
		if(camera != null) {
			// read keyboard and mouse
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				camera.applyCentralImpulse(new Vector3f(-0.001f,0,0));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				camera.applyCentralImpulse(new Vector3f(0.001f,0,0));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				camera.applyCentralImpulse(new Vector3f(0,0.001f,0));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				camera.applyCentralImpulse(new Vector3f(0,-0.001f,0));
			}
	
			//TODO: unfuck this scenario what with all the damn 
			//cam_velocity unknown behavior nonsense
			//asdf
			camera.activate();
			//checks the speed of the camera so it doesn't fly off.
			Vector3f cam_velocity = new Vector3f();
			camera.getLinearVelocity(cam_velocity);
			if(cam_velocity.x > 15 || cam_velocity.x < -15){
				cam_velocity.x = 0;
			}
			if(cam_velocity.y > 15 || cam_velocity.y < -15){
				cam_velocity.y = 0;
			}
			if(cam_velocity.z > 15 || cam_velocity.z < -15){
				cam_velocity.z = 0;
			}
			
			//assigns the camera a reasonable speed.
			//(cam_velocity);
			
			
			// Input.getInstance().updateInput();
		} else {
			camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
		}
			
	}
}
