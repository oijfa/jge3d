package entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;

public class Camera extends Entity{
	public static final String CAMERA_NAME = "camera";
	
	private Entity focus;
	
	/* Constructors */
	public Camera(RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		cameraInit();
	}
	public Camera(float f, MotionState m, CollisionShape c, boolean collide, EntityList objectList ) {
		super(f,m,c, collide);
		cameraInit();
	}
	public Camera(float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v, collide);
		cameraInit();
	}
	public Camera(String _name, RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		cameraInit();
	}
	public Camera(String _name,float f, MotionState m, CollisionShape c, boolean collide ) {
		super(f,m,c, collide);
		cameraInit();
	}
	public Camera(String _name,float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v, collide);
		cameraInit();
	}
	private void cameraInit(){
		setProperty(Entity.NAME, "camera", this);
		setProperty("collidable", true, this);
	}
	
	public void focusOn(Entity newFocus){
		focus = newFocus;
	}
	
	public Vector3f getFocusPosition(){
		Vector3f temp = new Vector3f();
		if(focus != null){
			focus.getCenterOfMassPosition(temp);
		}else{
			temp.set(0, 0, 0);
		}
		return temp;
	}
	
	
}
