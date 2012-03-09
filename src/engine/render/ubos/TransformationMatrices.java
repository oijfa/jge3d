package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
		
		float result[] = matmult(lookat, projection);
		for(int i=0; i<result.length;i++) {
			buf.put(result[i]);
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
		Vector3f x = new Vector3f();
		Vector3f y = new Vector3f();
		Vector3f z = new Vector3f(focus);
		
		z.sub(eye);
		z.normalize();
		
		x.cross(z,up);
		x.normalize();
		
		y.cross(x, z);
		
		//Grouped by columns
		lookat[0] = x.x;
		lookat[1] = y.x;
		lookat[2] = -z.x;
		lookat[3] = 0f;
		
		lookat[4] = x.y;
		lookat[5] = y.y;
		lookat[6] = -z.y;
		lookat[7] = 0f;
		
		lookat[8] = x.z;
		lookat[9] = y.z;
		lookat[10] = -z.z;
		lookat[11] = 0f;
		
		Vector3f translated_eye = new Vector3f(eye);
		lookat[12] = -x.dot(translated_eye);
		lookat[13] = -y.dot(translated_eye);
		lookat[14] = z.dot(translated_eye);
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
	
	private float[] matmult(float one[], float two[]) {
		float R[] = { 
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		};
    
		R[0] = one[0]*two[0] + one[1]*two[4] + one[2]*two[8] + one[3]*two[12];
		R[1] = one[0]*two[1] + one[1]*two[5] + one[2]*two[9] + one[3]*two[13];
		R[2] = one[0]*two[2] + one[1]*two[6] + one[2]*two[10] + one[3]*two[14];
		R[3] = one[0]*two[3] + one[1]*two[7] + one[2]*two[11] + one[3]*two[15];
		
		R[4] = one[4]*two[0] + one[5]*two[4] + one[6]*two[8] + one[7]*two[12];
		R[5] = one[4]*two[1] + one[5]*two[5] + one[6]*two[9] + one[7]*two[13];
		R[6] = one[4]*two[2] + one[5]*two[6] + one[6]*two[10] + one[7]*two[14];
		R[7] = one[4]*two[3] + one[5]*two[7] + one[6]*two[11] + one[7]*two[15];
		
		R[8] = one[8]*two[0] + one[9]*two[4] + one[10]*two[8] + one[11]*two[12];
		R[9] = one[8]*two[1] + one[9]*two[5] + one[10]*two[9] + one[11]*two[13];
		R[10] = one[8]*two[2] + one[9]*two[6] + one[10]*two[10] + one[11]*two[14];
		R[11] = one[8]*two[3] + one[9]*two[7] + one[10]*two[11] + one[11]*two[15];
		
		R[12] = one[12]*two[0] + one[13]*two[4] + one[14]*two[8] + one[15]*two[12];
		R[13] = one[12]*two[1] + one[13]*two[5] + one[14]*two[9] + one[15]*two[13];
		R[14] = one[12]*two[2] + one[13]*two[6] + one[14]*two[10] + one[15]*two[14];
		R[15] = one[12]*two[3] + one[13]*two[7] + one[14]*two[11] + one[15]*two[15];
    
	    return R;
	}
}