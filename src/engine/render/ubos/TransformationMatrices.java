package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class TransformationMatrices implements UBOInterface {
	//private Matrix4f perspective;
    private static final int size = 16;
    private static final String name = "TransformationMatrices";
    private static final String names[] = {
	    "MVP"
    };
    		
    private float projection[];
    private float lookat[];
    
	public TransformationMatrices(float fov, float aspect, float zNear, float zFar, Vector3f eye, Vector3f focus, Vector3f up) {
		projection = new float[size];
		lookat = new float[size];
		buildProjectionMatrix(fov, aspect, zNear, zFar);
		buildLookAtMatrix(eye,focus,up);
	}

	public FloatBuffer createBuffer(int block_size, IntBuffer offsets) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(projection.length+lookat.length);
		
		Matrix4f proj = new Matrix4f(projection);
		Matrix4f look = new Matrix4f(lookat);
		
		look.mul(proj);
		for(int i=0; i<projection.length/4;i++) {
			for(int j=0; j<lookat.length/4;j++) {
				buf.put(look.getElement(i, j));
			}
		}
		buf.flip();

		return buf;
	}
	
	public void buildProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
		float f = (float) (1 / Math.tan(Math.toRadians(fov)/2));
		
		//Grouped by columns
		projection[0] = f;
		projection[1] = 0;
		projection[2] = 0;
		projection[3] = 0;
		
		projection[4] = 0;
		projection[5] = f/aspect;
		projection[6] = 0;
		projection[7] = 0;
		
		projection[8] = 0;
		projection[9] = 0;
		projection[10] = -(zFar+zNear)/(zFar-zNear);
		projection[11] = -1;
		
		projection[12] = 0;
		projection[13] = 0;
		projection[14] = -(2*zFar*zNear)/(zFar-zNear);
		projection[15] = 0;
	}
	
	public void buildLookAtMatrix(Vector3f eye, Vector3f focus, Vector3f up) {
		Vector3f x = new Vector3f(focus);
		Vector3f y = new Vector3f();
		Vector3f z = new Vector3f();
		
		z.sub(eye);
		z.normalize();
		
		x.cross(up,z);
		x.normalize();
		
		y.cross(z, x);
		
		//Grouped by columns
		lookat[0] = x.x;
		lookat[1] = x.y;
		lookat[2] = x.z;
		lookat[3] = 0f;
		
		lookat[4] = y.x;
		lookat[5] = y.y;
		lookat[6] = y.z;
		lookat[7] = 0f;
		
		lookat[8] = z.x;
		lookat[9] = z.y;
		lookat[10] = z.z;
		lookat[11] = 0f;
		
		Vector3f translated_eye = new Vector3f(eye);
		//translated_eye.negate();
		
		lookat[12] = translated_eye.dot(x);
		lookat[13] = translated_eye.dot(y);
		lookat[14] = translated_eye.dot(z);
		lookat[15] = 1.0f;
	}
	
	public int getSize() {
		return names.length;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return Type.PERSPECTIVE;
	}
}