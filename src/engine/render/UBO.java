//TODO: Maybe add transforms?
package engine.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.ARBVertexBufferObject;

public class UBO {
	private boolean hasUBO = false;
	private int uboID;
	private int block_index;
	private FloatBuffer buffer;
	public static enum Type {LIGHT, MATERIAL, PERSPECTIVE};

	public UBO() {

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
		
		//create a new UBO reference
		uboID = createUBOID(1);
		
		//create a reference to the data definition in the shader
		block_index = ARBUniformBufferObject.glGetUniformBlockIndex(
			shader.getShaderID(),
			ubo.getName()
		);
		
		//latch to the specific instance of the uniform
		//TODO: I'm unsure of the last param I think it's very wrong
		ARBUniformBufferObject.glGetActiveUniformBlock(
			shader.getShaderID(), 
			block_index,
			uboID
		);
		
		//clear buffer
		bufferData(uboID, null, ubo);
		
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
			0
		);
		
		//Set the uniform offsets for updating
		ARBUniformBufferObject.glGetUniformIndices(
			shader.getShaderID(), 
			ubo.getNamesAsBuffer(), 
			ubo.getIndices()
		);		
		
		// Set the notifier
		hasUBO = true;
	}

		//put data into buffer		
	public void buffer_ubo(Shader shader, UBOInterface ubo) {
		buffer = BufferUtils.createFloatBuffer(ubo.getSize());
		buffer.put(ubo.createBuffer());

		// NEVER FLIP AGAIN PAST THIS POINT UNLESS YOU'RE LOADING IN
		// COMPLETELY NEW DATA
		buffer.flip();

		// Put data in allocated buffers
		bufferData(uboID, buffer, ubo);
	}

	public int createUBOID(int i) {
		IntBuffer buffer = BufferUtils.createIntBuffer(i);
		ARBVertexBufferObject.glGenBuffersARB(buffer);
		return buffer.get(0);
	}
	
	public void bufferData(int id, FloatBuffer buffer, UBOInterface ubo) {
		ARBBufferObject.glBindBufferARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER,
			id
		);
		//TODO: conflicting reports of whether it should be ARBUniformBufferObject.GL_UNIFORM_BUFFER_EXT
		ARBBufferObject.glBufferSubDataARB(
			ARBUniformBufferObject.GL_UNIFORM_BUFFER, 
			ubo.getSize(), 
			ubo.createBuffer()
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
