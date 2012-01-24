package engine.render.ubos;

import java.nio.ByteBuffer;
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
	    "projection",
    	"lookat"
    };
    		
    private float projection[];
    private float lookat[];
    
	public TransformationMatrices(float fov, float aspect, float zNear, float zFar, Vector3f eye, Vector3f focus, Vector3f up) {
		projection = new float[size];
		lookat = new float[size];
		buildProjectionMatrix(fov, aspect, zNear, zFar);
		buildLookAtMatrix(eye,focus,up);
	}
	
	public FloatBuffer createBuffer() {
		FloatBuffer buf = BufferUtils.createFloatBuffer(projection.length+lookat.length);
		
		buf.put(projection);
		buf.put(lookat);
		buf.flip();
		
		return buf;
	}
	
	public void buildProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
		float f = (float) (1 / Math.tan(Math.toRadians(fov)/2));
		
		//Grouped by columns
		projection[0] = f/aspect;
		projection[1] = 0;
		projection[2] = 0;
		projection[3] = 0;
		
		projection[4] = 0;
		projection[5] = f;
		projection[6] = 0;
		projection[7] = 0;
		
		projection[8] = 0;
		projection[9] = 0;
		projection[10] = (zFar+zNear)/(zNear-zFar);
		projection[11] = -1;
		
		projection[12] = 0;
		projection[13] = 0;
		projection[14] = (2 * zFar * zNear) / (zNear - zFar);
		projection[15] = 0;
	}
	
	public void buildLookAtMatrix(Vector3f eye, Vector3f focus, Vector3f up) {
		Vector3f back = new Vector3f();
		Vector3f forward = new Vector3f();
		Vector3f right = new Vector3f();
		
		back.set(eye);
		back.sub(focus);
		back.normalize();
		
		forward.set(back);
		forward.scale(-1);
		forward.normalize();
		
		right.cross(forward, up);
		right.normalize();
		
		//Grouped by columns
		lookat[0] = right.x;
		lookat[1] = up.x;
		lookat[2] = back.x;
		lookat[3] = 0f;
		
		lookat[4] = right.y;
		lookat[5] = up.y;
		lookat[6] = back.y;
		lookat[7] = 0f;
		
		lookat[8] = right.z;
		lookat[9] = up.z;
		lookat[10] = back.z;
		lookat[11] = 0f;
		
		lookat[12] = eye.x;
		lookat[13] = eye.y;
		lookat[14] = eye.z;
		lookat[15] = 1.0f;
	}
	
	public int getSize() {
		return names.length;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public ByteBuffer getNamesAsBuffer() {
		int name_size=0;
		for(String name: names){
			name_size+=name.length();
		}
		ByteBuffer buf = BufferUtils.createByteBuffer(name_size);
		
		for(String name: names) {
			buf.put(name.getBytes());
		}		
		
		buf.flip();

		return buf;
	}
	
	public String getName() {
		return name;
	}
	
	public IntBuffer getIndices() {
		return IntBuffer.wrap(new int[size]);
	}
	
	public Type getType() {
		return Type.PERSPECTIVE;
	}
}