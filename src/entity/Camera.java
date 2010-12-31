package entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;

public class Camera extends Entity{
	private float[] focus;
	public static String CAMERA_NAME = "camera";
	private Vector3f linVelocity = new Vector3f();
	private Entity focusEntity;
	private EntityList objectList;
	
	/* Constructors */
	public Camera(RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
	}
	
	public Camera(float f, MotionState m, CollisionShape c, boolean collide, EntityList objectList ) {
		super(f,m,c, collide);
		setProperty(Entity.NAME, "camera");
		setFocus(0.0f,0.0f,0.0f);
		this.objectList = objectList;
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
	
	// This Shit is ridiculous either Vector3f or float array but no both son.
	public void setFocus(float[] newFocus) throws Exception{
		if( newFocus.length == 3 ){
			focus = newFocus;
		}else{
			Exception e = new Exception();
			e.initCause(new Throwable("Invalid number of floats for camera setFocus"));
			throw e;
		}
	}
	
	public void setFocus(Vector3f newFocus) {
		focus[0] = newFocus.x;
		focus[1] = newFocus.y;
		focus[2] = newFocus.z;
	}
	
	public void setFocusEntity(String newFocus){
		focusEntity = objectList.getItem(newFocus);
		Vector3f EntPosition = focusEntity.getPosition();
		setFocus(EntPosition);
	}
	
	public float[] getFocus(){
		return focus;
	}
	
	public Vector3f getVelocity(){
		return this.getLinearVelocity(linVelocity);
	}
	public void setVelocity(Vector3f velo){
		this.setLinearVelocity(velo);
	}

}
