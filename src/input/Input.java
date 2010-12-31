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
	
			//TODO: unfuck this scenario what with all the damn 
			//cam_velocity unknown behavior nonsense
			//asdf
			camera.activate();
			//checks the speed of the camera so it doesn't fly off.
			Vector3f cam_velocity = camera.getVelocity();
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
}
