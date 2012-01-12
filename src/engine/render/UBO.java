//TODO: Maybe add transforms?
package engine.render;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;

import engine.render.ubos.UBOInterface;

public class UBO {
	private boolean hasUBO = false;
	private int uboID;
	private int block_index;
	private int block_size;
	public static enum Type {LIGHT, MATERIAL, PERSPECTIVE};
	private UBOInterface ubo_interface;

	public UBO(Shader shader, UBOInterface ubo) {
		createUBO(ubo, shader);
	}

	public boolean getHasUBO() {
		return hasUBO;
	}

	public int getVBOID() {
		return uboID;
	}

	// *******************VBO METHODS**************************
	public void createUBO(UBOInterface ubo, Shader shader) {
		//Make sure not to duplicate UBOs		
		if (hasUBO) {
			//System.out.println("DestroyingVBO: " + modelVBOID + ":" + modelVBOindexID);
			destroyUBO();
		}
		
		ubo_interface = ubo;
		
		//create a new UBO reference
		uboID = createUBOID(1);
		
		//Set the uniform offsets for updating
		/*
		ARBUniformBufferObject.glGetUniformIndices(
			shader.getShaderID(), 
			ubo.getNamesAsBuffer(), 
			ubo.getIndices()
		);
		*/		
		
		/* No idea what to do with this
		int offset = ARBUniformBufferObject.glGetActiveUniforms(
			shader.getShaderID(), 
			uboID, 
			ARBUniformBufferObject.GL_UNIFORM_OFFSET
		);
		*/
		
		//create a reference to the data definition in the shader
		block_index = ARBUniformBufferObject.glGetUniformBlockIndex(
			shader.getShaderID(),
			ubo.getName()
		);
		
		//get the size of the block you just found
		//TODO: I'm unsure of the last param I think it's very wrong
		block_size = ARBUniformBufferObject.glGetActiveUniformBlock(
			shader.getShaderID(), 
			block_index,
			ARBUniformBufferObject.GL_UNIFORM_BLOCK_DATA_SIZE
		);

		//clear buffer
		bufferData(uboID);
		
		//not sure why I need to do this (^ didn't we just clear it?)
		ARBUniformBufferObject.glBindBufferBase(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			0,
			uboID
		);
		
		//Associate the uniform to its binding point
		ARBUniformBufferObject.glUniformBlockBinding(
			shader.getShaderID(), 
			block_index,
			uboID
		);

		// Set the notifier
		hasUBO = true;
	}
	/*
	public void buffer_ubo(Shader shader) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(ubo_interface.getSize());
		buffer.put(ubo_interface.createBuffer());

		// NEVER FLIP AGAIN PAST THIS POINT UNLESS YOU'RE LOADING IN
		// COMPLETELY NEW DATA
		buffer.flip();

		// Put data in allocated buffers
		bufferData(uboID, buffer);
	}*/

	private int createUBOID(int i) {
		IntBuffer buffer = BufferUtils.createIntBuffer(i);
		ARBVertexBufferObject.glGenBuffersARB(buffer);
		return buffer.get(0);
	}
	
	public void bufferData(int id) {
		ARBBufferObject.glBindBufferARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			id
		);
		//TODO: conflicting reports of whether 1st arg should be 
		//ARBUniformBufferObject.GL_UNIFORM_BUFFER_EXT
		ARBBufferObject.glBufferSubDataARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER, 
			block_size, 
			ubo_interface.createBuffer()
		);
		ARBBufferObject.glBindBufferARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER, 
			0
		);
	}
	
	public void destroyUBO() {
		ARBBufferObject.glDeleteBuffersARB(uboID);
		hasUBO = false;
	}
	// *****************END VBO METHODS***********************
}
