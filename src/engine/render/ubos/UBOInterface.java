package engine.render.ubos;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import engine.render.UBO.Type;

public interface UBOInterface {
	public String[] getNames();
	public FloatBuffer createBuffer();
	public IntBuffer getIndices();
	public Type getType();
	public int getSize();
	public ByteBuffer getNamesAsBuffer();
	public String getName();
}
