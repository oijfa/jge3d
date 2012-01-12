package engine.render.ubos;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class ProjectionMatrix implements UBOInterface {
	//private Matrix4f perspective;
    private static final int size = 16;
    private static final String name = "projection";
    private static final String names[] = {
	    "projection"
    };
    		
    private float matrix[];
    
	public ProjectionMatrix(float fov, float aspect, float zNear, float zFar) {
		matrix = new float[size];
		buildMatrix(matrix, fov, aspect, zNear, zFar);
	}
	
	public FloatBuffer createBuffer() {
		FloatBuffer buf = BufferUtils.createFloatBuffer(matrix.length);
		
		buf.put(matrix);
		buf.flip();
		
		return buf;
	}
	
	public void buildMatrix(float m[], float fov, float aspect, float zNear, float zFar) {
		final float h = 1.0f/(float)Math.tan(fov*(Math.PI/360));
		final float neg_depth = zNear-zFar;
	
		m[0] = h / aspect;
		m[1] = 0;
		m[2] = 0;
		m[3] = 0;
	
		m[4] = 0;
		m[5] = h;
		m[6] = 0;
		m[7] = 0;
	
		m[8] = 0;
		m[9] = 0;
		m[10] = (zFar + zNear)/neg_depth;
		m[11] = -1;
	
		m[12] = 0;
		m[13] = 0;
		m[14] = 2.0f*(zNear*zFar)/neg_depth;
		m[15] = 0;
	}
	
	public int getSize() {
		return size;
	}
	
	public static String[] getNames() {
		return names;
	}
	
	public ByteBuffer getNamesAsBuffer() {
		String name_buffer = new String();
		for(String name: names) {
			name_buffer += name;
		} 
		return ByteBuffer.wrap(name_buffer.getBytes());
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