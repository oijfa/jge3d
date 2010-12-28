package entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;

public class Camera extends Entity{
	private float[] focus;
	
	/* Constructors */
	public Camera(RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	
	public Camera(float f, MotionState m, CollisionShape c, boolean collide ) {
		super(f,m,c, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	public Camera(float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	public Camera(String _name, RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	public Camera(String _name,float f, MotionState m, CollisionShape c, boolean collide ) {
		super(f,m,c, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	public Camera(String _name,float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	private void setFocus(float x, float y, float z) {
		focus = new float[3];
		focus[0] = x;
		focus[1] = y;
		focus[2] = z;
	}
	public void setFocus(float[] newFocus) throws Exception{
		if( newFocus.length == 3 ){
			focus = newFocus;
		}else{
			Exception e = new Exception();
			e.initCause(new Throwable("Invalid number of floats for camera setFocus"));
			throw e;
		}
	}
	public float[] getFocus(){
		return focus;
	}
}
