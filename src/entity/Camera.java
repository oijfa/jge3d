package entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;

public class Camera extends Entity {
	
	/*Static class variables*/
	//Don't flip over, its confusing.
	private static final float maximum_declination = (float) (Math.PI/2.0f) - 0.01f;
	private static final float minimum_declination = (float) ((float) -1.0f*((Math.PI/2.0f) - 0.01f));
	private static final float minimum_distance = 0.001f;
	private static final float maximum_distance = 100.0f;
	public static final String CAMERA_NAME = "camera";
	
	/*Class fields*/
	private float declination;					//Angle up and down
	private float rotation;						//Angle left and right
	private float distance;						//distance from focus
	private Vector3f up_vector;					//vector pointing up
	
	private Entity focus;

	/* Constructors */
	public Camera(RigidBodyConstructionInfo r, boolean collide) {
		super(r, collide);
		cameraInit();
	}
	public Camera(float f, MotionState m, CollisionShape c, boolean collide ) {
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
		setPosition(new Vector3f(0,0,0));
		declination = 0;
		rotation = 0;
		distance = 15.0f;
		setUpVector( new Vector3f(0, 1, 0) );
		updatePosition();
	}
	
	public void focusOn(Entity newFocus){
		focus = newFocus;
		/*
		parentList.removeJoint(FOCUS_JOINT);
		parentList.addBallJoint(FOCUS_JOINT, 
				newFocus, new Vector3f(0.0f,0.0f,0.0f),
				this, new Vector3f(0.0f, 0.0f, -15.0f)
		);
		*/
	}
	
	/*Accessors*/
	public Vector3f getUp(){
		return up_vector;
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
	
	/*Mutators*/
	private void setUpVector( Vector3f newUp ){
		up_vector = newUp;
	}
	public void setDistance(float f) {
		if( f > maximum_distance)
		{
			distance = maximum_distance;
		}else
		{
			if(f < minimum_distance)
			{
				distance = minimum_distance;
			}else
			{
				distance = f;
			}
		}
		//Not needed because renderer always calls it
		//updatePosition();
	}
	
	public void incrementDistance( float change ){
		float temp = distance + change;
		if( temp > maximum_distance)
		{
			distance = maximum_distance;
		}else
		{
			if(temp < minimum_distance)
			{
				distance = minimum_distance;
			}else
			{
				distance = temp;
			}
		}
		//Not needed because renderer always calls it
		//updatePosition();
	}
	public void incrementDeclination(float angle){
		declination += angle;
		if( declination > maximum_declination ){
			declination = maximum_declination;
		}
		if( declination < minimum_declination ){
			declination = minimum_declination;
		}
		//Not needed because renderer always calls it
		//updatePosition();
	}
	public void incrementRotation(float angle){
		rotation += angle;
		//Not needed because renderer always calls it
		//updatePosition();
	}
	public void updatePosition()
	{
		 float a = 0;
		 Vector3f position = new Vector3f();
		 Vector3f focPos = getFocusPosition();
		 
		 //calculate positions from angles as if focus were (0,0,0)
		 position.y = (float) ((distance * Math.sin(declination)));
		 a = (float) ((distance * Math.cos(declination)));
		 position.x = (float) (a*Math.sin(rotation));
		 position.z = (float) (a*Math.cos(rotation));
		 position.add(focPos);
		 setPosition(position);
	}
	
	/*debug functions*/
	public void debug() {
		//Debug the camera
		//System.out.print("Height:		" + height 	+ "	Width:	" + width + "\n");
		Vector3f position = new Vector3f();
		this.getCenterOfMassPosition(position);
		
		Vector3f focpos = this.getFocusPosition();
		System.out.print("Camera = X:	" + position.x + "	Y:	" + position.y + "	Z:	" + position.z + "\n");
		System.out.print("Focus  = X:	" + focpos.x 	+ "	Y:	" + focpos.y 	+ "	Z:	" + focpos.z 	+ "\n");
		System.out.print("Up     = X:	" + up_vector.x + "	Y:	" + up_vector.y + "	Z:	" + up_vector.z + "\n\n");	
	}
}
