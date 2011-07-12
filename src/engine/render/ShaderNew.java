package engine.render;

import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCompileShaderARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateShaderObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDeleteObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDetachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetInfoLogARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetObjectParameteriARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glShaderSourceARB;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class ShaderNew {
	final String vshFile;
	final String vshSource;

	final int vshID;

	final String fshFile;
	final String fshSource;

	final int fshID;

	final int programID;

	//final int uniformLocation;

	ShaderNew(final String vshFile, final String fshFile) {
		// Initialize the vertex shader.
		this.vshFile = vshFile;
		vshSource = getShaderText(vshFile);

		vshID = glCreateShaderObjectARB(GL_VERTEX_SHADER_ARB);
		glShaderSourceARB(vshID, vshSource);
		glCompileShaderARB(vshID);

		printShaderObjectInfoLog(this.vshFile, vshID);

		if ( glGetObjectParameteriARB(vshID, GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE )
			System.out.println("A compilation error occured in a vertex shader.");

		// Initialize the fragment shader.
		this.fshFile = fshFile;
		fshSource = getShaderText(fshFile);

		fshID = glCreateShaderObjectARB(GL_FRAGMENT_SHADER_ARB);
		glShaderSourceARB(fshID, fshSource);
		glCompileShaderARB(fshID);

		printShaderObjectInfoLog(this.fshFile, fshID);

		if ( glGetObjectParameteriARB(fshID, GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE )
			System.out.println("A compilation error occured in a fragment shader.");

		// Initialize the shader program.
		programID = glCreateProgramObjectARB();

		glAttachObjectARB(programID, vshID);
		glAttachObjectARB(programID, fshID);

		glLinkProgramARB(programID);

		printShaderProgramInfoLog(programID);

		if ( glGetObjectParameteriARB(programID, GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE ){
			System.out.println("A linking error occured in a shader program." + vshFile);
		}

		//uniformLocation = getUniformLocation(programID, "transform");

	}

	void cleanup() {
		glDetachObjectARB(programID, vshID);
		glDetachObjectARB(programID, fshID);

		glDeleteObjectARB(vshID);
		glDeleteObjectARB(fshID);

		glDeleteObjectARB(programID);
	}
	
	protected static ByteBuffer fileBuffer = BufferUtils.createByteBuffer(1024 * 10);
	protected static String getShaderText(String file) {
		String shader = null;

		try {
			InputStream source = ShaderNew.class.getResourceAsStream(file);
			if ( source == null ) // dev-mode
				source = new FileInputStream("src/engine/importing/pieces/" + file);

			BufferedInputStream stream = new BufferedInputStream(source);

			byte character;
			while ( (character = (byte)stream.read()) != -1 )
				fileBuffer.put(character);

			stream.close();

			fileBuffer.flip();

			byte[] array = new byte[fileBuffer.remaining()];
			fileBuffer.get(array);
			shader = new String(array);

			fileBuffer.clear();
		} catch (IOException e) {
			System.out.println("Failed to read the shader source file: " + file + " " + e.toString());
		}

		return shader;
	}

	protected static int getUniformLocation(int ID, String name) {
		final int location = glGetUniformLocationARB(ID, name);

		if ( location == -1 )
			throw new IllegalArgumentException("The uniform \"" + name + "\" does not exist in the Shader Program.");

		return location;
	}

	protected static void printShaderObjectInfoLog(String file, int ID) {
		final int logLength = glGetObjectParameteriARB(ID, GL_OBJECT_INFO_LOG_LENGTH_ARB);
		if ( logLength <= 1 )
			return;

		System.out.println("\nInfo Log of Shader Object: " + file);
		System.out.println("--------------------------");
		System.out.println(glGetInfoLogARB(ID, logLength));
	}

	protected static void printShaderProgramInfoLog(int ID) {
		final int logLength = glGetObjectParameteriARB(ID, GL_OBJECT_INFO_LOG_LENGTH_ARB);
		if ( logLength <= 1 )
			return;

		System.out.println("\nShader Program Info Log: ");
		System.out.println("--------------------------");
		System.out.println(glGetInfoLogARB(ID, logLength));
	}
}
