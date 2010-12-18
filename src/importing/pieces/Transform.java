package importing.pieces;

import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Transform {
	private Vector3f forward;
	private Vector3f up;
	private Vector3f position;
	Vector3f side = new Vector3f(); 
	private float scale;
	
	public Transform()
	{
		forward = null;
		up = null;
		position = null;
	}
	
	public Transform(Vector3f _forward, Vector3f _up, Vector3f _position) {
		forward = _forward;
		up = _up;
		position = _position;
	}
	
	public Transform(Vector3f _forward, Vector3f _up, Vector3f _position, float _scale) {
		forward = _forward;
		up = _up;
		position = _position;
		scale = _scale;
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public Vector3f getUp() {
		return up;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getScale() {
		return scale;
	}

	FloatBuffer buf = BufferUtils.createFloatBuffer(16);
	public void draw() {
		//Create a matrix for transforming the object
		float[] transform_matrix = new float[16];
		
		//we have forward and up so we take the cross product
		//to produce a side(x-axis) vector
		side.cross(forward, up);
		
		//Create a 4x4 transformation matrix
		//row 1
		transform_matrix[0] = scale * side.x;
		transform_matrix[1] = scale * up.y;
		transform_matrix[2] = scale * forward.z;
		transform_matrix[3] = position.x;
		
		//row 2
		transform_matrix[4] = scale * side.x;
		transform_matrix[5] = scale * up.x;
		transform_matrix[6] = scale * forward.x;
		transform_matrix[7] = position.y;
		
		//row 3
		transform_matrix[8] = scale * side.x;
		transform_matrix[9] = scale * up.x;
		transform_matrix[10] = scale * forward.x;
		transform_matrix[11] = position.z;
		
		//row 4
		transform_matrix[12] = 0;
		transform_matrix[13] = 0;
		transform_matrix[14] = 0;
		transform_matrix[15] = 1;
	
		//Create a float buffer for lwjgl
		buf.put(transform_matrix).flip();

        //multiply by the newly created scaled matrix
		GL11.glMultMatrix(buf);
	}
	/*
	public void draw()
	{
		GL11.glTranslatef(position.x, position.y, position.z);
		
		//Bad as well
		//GL11.glRotatef(forward.angle(new Vector3f(0,0,1)), 0, 1, 0);
		
		Vector3f temp = new Vector3f( 0, up.y, up.z);
		GL11.glRotatef(temp.angle(new Vector3f(0,1,0)), 1, 0, 0);
		
		Vector3f temp2 = new Vector3f( up.x, up.y, 0);
		temp2 = new Vector3f( up.x, up.y, 0);
		//This one
		//GL11.glRotatef(temp2.angle(new Vector3f(0,1,0)), 0, 0, 1);
	}
	*/
}
