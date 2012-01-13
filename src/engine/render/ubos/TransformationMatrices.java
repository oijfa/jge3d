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
		final float h = 1.0f/(float)Math.tan(fov);
		final float neg_depth = (zNear-zFar);
				
		//TODO: calculate these values instead of this f'd magic number 
		//float r = 5f;
		//float t = 5f;
		//This assumes that near plane of frustum is symmetric.  If not
		//Then look here: http://www.songho.ca/opengl/gl_projectionmatrix.html
		//There's a more complex calculation for general frustums.
		/*
		float[][] matrix = {
			{ -zNear/r,        0,                               0,      0},
			{        0, -zNear/t,                               0,      0},
			{        0,        0,      -(zFar+zNear)/(zFar-zNear),      (-2*zFar*zNear)/(zFar-zNear)},
			{        0,        0,                              -1,      0},
		};
		*/
		float[][] matrix = {
			{ h/aspect,			0,                              	0,      0},
			{        0,			h,									0,      0},
			{        0,			0,			(zFar + zNear)/neg_depth,      (2.0f*zNear*zFar)/neg_depth},
			{        0,			0,									-1,      0},
		};
		
		debugMatrix(matrix);
		
		for(int row = 0; row < 4; row++){
			for(int col = 0; col < 4; col++){
				projection[(row*4)+col] = matrix[row][col];
			}
		}
	}
	
	public void buildLookAtMatrix(Vector3f eye, Vector3f focus, Vector3f up) {
		Vector3f f = new Vector3f();
		Vector3f s = new Vector3f();
		Vector3f u = new Vector3f();
		
		f.sub(focus, eye);
		f.normalize();
		
		up.normalize();
		s.cross(f, up);
		u.cross(s, f);
		float[][] matrix = {
			{	s.x,	s.y,	s.z,	0},
			{	u.x,	u.y,	u.z,	0},
			{	-f.x,	-f.y,	-f.z,	0},
			{	0,		0,		0,		1},
		};
		
		debugMatrix(matrix);
		
		for(int row = 0; row < 4; row++){
			for(int col = 0; col < 4; col++){
				lookat[(row*4)+col] = matrix[row][col];
			}
		}
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
	
	public void debugMatrix(float matrix[][]) {
		float m[] = new float[size];
		System.out.print("[\n");
		for(int row = 0; row < 4; row++){
			System.out.print("    [");
			for(int col = 0; col < 4; col++){
				m[(row*4)+col] = matrix[row][col];
				if(col==3)
					System.out.format(" %f ",m[(row*4)+col]);
				else
					System.out.format(" %f, ",m[(row*4)+col]);
			}
			System.out.print(" ]\n");
		}
		System.out.print("]\n");
	}
}