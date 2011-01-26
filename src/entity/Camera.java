package entity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import render.Renderer;

import com.bulletphysics.collision.shapes.CollisionShape;

public class Camera extends Entity {
	
	/*Static class variables*/
	//Don't flip over, its confusing.
	private static final float maximum_declination = (float) (Math.PI/2.0f) - 0.01f;
	private static final float minimum_declination = (float) ((float) -1.0f*((Math.PI/2.0f) - 0.01f));
	private static final float minimum_distance = 0.000001f;
	private static final float maximum_distance = 50.0f;
	public static final String CAMERA_NAME = "camera";
	
	/*Class fields*/
	private float declination;					//Angle up and down
	private float rotation;						//Angle left and right
	private float distance;						//distance from focus
	private Vector3f up_vector;					//vector pointing up
	
	private Entity focus;

	/* Constructors */
	public Camera(float f, CollisionShape c, boolean collide ) {
		super(f,c,collide);
		cameraInit();
	}
	public Camera(String _name,float f, CollisionShape c, boolean collide ) {
		super(f,c,collide);
		cameraInit();
	}

	private void cameraInit(){
		setProperty(Entity.NAME, "camera");
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
			temp = focus.getPosition();
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
		position = this.getPosition();
		
		Vector3f focpos = this.getFocusPosition();
		System.out.print("Camera = X:	" + position.x + "	Y:	" + position.y + "	Z:	" + position.z + "\n");
		System.out.print("Focus  = X:	" + focpos.x 	+ "	Y:	" + focpos.y 	+ "	Z:	" + focpos.z 	+ "\n");
		System.out.print("Up     = X:	" + up_vector.x + "	Y:	" + up_vector.y + "	Z:	" + up_vector.z + "\n\n");	
	}
	
	//TODO: This needs cleaned up and commented real bad
	public Vector3f getRayTo(int x, int y) { return getRayTo(x, y, (int)Renderer.farClipping); }
	public Vector3f getRayTo(int x, int y, int farDistance) {	
		//Create stupid floatbuffers for LWJGL
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		Vector3f pos = new Vector3f();
		
		//Get some information about the viewport, modelview, and projection matrix
		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

		//Find the depth to 
		GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);

		//get the position in 3d space by casting a ray from the mouse
		//coords to the first contacted point in space
		//GLU.gluUnProject(mouseX, mouseY, winZ.get(), modelview, projection, viewport, position);
		GLU.gluUnProject(x, y, winZ.get(), modelview, projection, viewport, position);

		//Make a vector out of the silly float buffer LWJGL forces us to use
		pos.set(position.get(0), position.get(1), position.get(2));

		return pos;
	}
}
