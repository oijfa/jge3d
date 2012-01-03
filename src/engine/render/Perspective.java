package engine.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Matrix4f;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class Perspective implements UBOInterface {
	//private Matrix4f perspective;
    private static final int size = 16;
    private static final String name = "perspective";
    private static final String names[] = {
	    "perspective"
    };
    
	public Perspective(Matrix4f perspective) {
		//this.perspective = perspective;
	}
	
	public FloatBuffer createBuffer(Matrix4f matrix) {
		float perspective_buffer[] = new float[size];
		
		for(int y=0;y< Math.sqrt(size);y++) {
			for(int x=0;x< Math.sqrt(size);x++) {
				perspective_buffer[x+y] = matrix.getElement(x,y);
			}
		}
				
		return FloatBuffer.wrap(perspective_buffer);
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
		return BufferUtils.createIntBuffer(size);
	}
	
	public Type getType() {
		return Type.PERSPECTIVE;
	}

	public FloatBuffer createBuffer() {
		// TODO Auto-generated method stub
		return null;
	}
}