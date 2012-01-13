package engine.render.ubos;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class TransformationMatrices implements UBOInterface {
	//private Matrix4f perspective;
    private static final int size = 16;
    private static final String name = "TransformationMatrices";
    private static final String names[] = {
	    "projection"
    };
    		
    private float projection[];
    
	public TransformationMatrices(float fov, float aspect, float zNear, float zFar) {
		projection = new float[size];
		buildProjectionMatrix(projection, fov, aspect, zNear, zFar);
	}
	
	public FloatBuffer createBuffer() {
		FloatBuffer buf = BufferUtils.createFloatBuffer(projection.length);
		
		buf.put(projection);
		buf.flip();
		
		return buf;
	}
	
	public void buildProjectionMatrix(float m[], float fov, float aspect, float zNear, float zFar) {
		final float h = 1.0f/(float)Math.tan(fov*(Math.PI/360));
		final float neg_depth = zNear-zFar;
		
		//TODO: calculate these values instead of this f'd magic number 
		float r = 5f;
		float t = 5f;
		
		//This assumes that near plane of frustum is symmetric.  If not
			//Then look here: http://www.songho.ca/opengl/gl_projectionmatrix.html
			//There's a more complex calculation for general frustums.
		float[][] matrix = {
			{ -zNear/r,        0,                               0,      0},
			{        0, -zNear/t,                               0,      0},
			{        0,        0,      -(zFar+zNear)/(zFar-zNear),      (-2*zFar*zNear)/(zFar-zNear)},
			{        0,        0,                              -1,      0},
		};
		
		System.out.print("[\n");
		for(int row = 0; row < 4; row++){
			System.out.print("    [");
			for(int col = 0; col < 4; col++){
				m[(row*4)+col] = matrix[row][col];
				System.out.format(" %f ",m[(row*4)+col]);
			}
			System.out.print(" ]\n");
		}
		System.out.print("]\n");
		/*
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
		*/
		/*
		m[0] = h / aspect;
		m[4] = 0;
		m[8] = 0;
		m[12]= 0;
		
		m[1] = 0;
		m[5] = h;
		m[9] = 0;
		m[13]= 0;
		
		m[2] = 0;
		m[6] = 0;
		m[10]= (zFar + zNear)/neg_depth;
		m[14]= -1;
		
		m[3] = 0;
		m[7] = 0;
		m[11]= 2.0f*(zNear*zFar)/neg_depth;
		m[15]= 0;
		*/
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