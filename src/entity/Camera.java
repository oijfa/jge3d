package entity;

import javax.vecmath.Vector3f;

import render.Renderer;

import window.Window;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;

public class Camera extends Entity {
	
	/*Static class variables*/
	//Don't flip over, its confusing.
	private static final float maximum_declination = (float) (Math.PI/2.0f) - 0.01f;
	private static final float minimum_declination = (float) ((float) -1.0f*((Math.PI/2.0f) - 0.01f));
	private static final float minimum_distance = 2.5f;
	private static final float maximum_distance = 50.0f;
	public static final String CAMERA_NAME = "camera";
	
	/*Class fields*/
	private float declination;					//Angle up and down
	private float rotation;						//Angle left and right
	private float distance;						//distance from focus
	private Vector3f up_vector;					//vector pointing up
	private Window window;
	
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
		distance = 5.0f;
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
		}else if(temp < minimum_distance){
			distance = minimum_distance;
		}else{
			distance = temp;
		}
	
		//Not needed because renderer always calls it
		//updatePosition();
	}
	
	public float getDistance(){
		return distance;
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
	
	public void setDeclination(float angle){
		this.declination = angle;
	}
	
	public void incrementRotation(float angle){
		rotation += angle;
		/*
		 * Checks if rotation is over 2*Pi and adjusts it accordingly.
		 * This way the camera's rotation doesn't lock up(Slow Down) at around 2*Pi + Pi/2
		 * of the rotation 
		*/
		if(Math.abs(rotation) > 6.28318531f){
			if(rotation > 0){
				rotation -= 6.28318531f;
			}else if(rotation < 0){
				rotation += 6.28318531f;
			}
		}
		//Not needed because renderer always calls it
		//updatePosition();
	}
	
	public void setRotation(float angle){
		this.rotation = angle;
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
	
	//TODO: This needs cleaned up and commented real bad
	public Vector3f getRayTo(int x, int y) {	
		float top = 1f;
		float bottom = -1f;
		float tanFov = (top - bottom) * 0.5f / Renderer.nearClipping;
		float fov = 2f * (float) Math.atan(tanFov);

		Vector3f rayFrom = new Vector3f(this.getPosition());
		Vector3f rayForward = new Vector3f();
		rayForward.sub(this.getFocusPosition(), this.getPosition());
		rayForward.normalize();

		//Scale by the far clipping plane
		rayForward.scale(Renderer.farClipping);

		Vector3f vertical = new Vector3f(this.getUp());

		Vector3f hor = new Vector3f();
		hor.cross(rayForward, vertical);
		hor.normalize();
		vertical.cross(hor, rayForward);
		vertical.normalize();

		float tanfov = (float) Math.tan(0.5f * fov);
		
		float aspect = window.getHeight() / (float)window.getWidth();
		
		hor.scale(Renderer.farClipping * tanfov);
		vertical.scale(Renderer.farClipping * tanfov);
		
		if (aspect < 1f) {
			hor.scale(1f / aspect);
		}
		else {
			vertical.scale(aspect);
		}
		
		Vector3f rayToCenter = new Vector3f();
		rayToCenter.add(rayFrom, rayForward);
		Vector3f dHor = new Vector3f(hor);
		dHor.scale(1f / (float) window.getWidth());
		Vector3f dVert = new Vector3f(vertical);
		dVert.scale(1.f / (float) window.getHeight());

		Vector3f tmp1 = new Vector3f();
		Vector3f tmp2 = new Vector3f();
		tmp1.scale(0.5f, hor);
		tmp2.scale(0.5f, vertical);

		Vector3f rayTo = new Vector3f();
		rayTo.sub(rayToCenter, tmp1);
		rayTo.add(tmp2);

		tmp1.scale(x, dHor);
		tmp2.scale(y, dVert);

		rayTo.add(tmp1);
		rayTo.sub(tmp2);
		
		return rayTo;
	}
	
	public void setWindowReference(Window window) {
		this.window = window;
	}
}
