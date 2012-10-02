package engine.render.model_pieces;

import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.opengl.TextureLoader;

import engine.render.Shader;
import engine.resource.Resource;
import engine.resource.ResourceManager;

public class Texture implements Resource {
	private org.newdawn.slick.opengl.Texture opengl_texture;
	private String extension;
	
	@Override
	public void loadFromFile(ResourceManager resource_manager, InputStream is,
			String extension) throws Exception {
		opengl_texture = TextureLoader.getTexture(extension, is);
		this.extension = extension;

        int width = opengl_texture.getImageWidth();
        int height = opengl_texture.getImageHeight();
        
        ByteBuffer data = BufferUtils.createByteBuffer(65536);
        data.put(opengl_texture.getTextureData());
        data.rewind();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, opengl_texture.getTextureID());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        if(opengl_texture.hasAlpha())
        	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        else
        	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, data);
	}

	public org.newdawn.slick.opengl.Texture getTexture() {
		return opengl_texture;
	}
	
	public void draw(Shader shader) {
		int base_tex = GL20.glGetUniformLocation(shader.getShaderID(), "texture_data");
		GL20.glUniform1i(base_tex, 0); //Texture unit 0 is for base images.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, opengl_texture.getTextureID());
	}
	
	@Override
	public String toXML() {
		return "<texture><extension>"+extension+"</extension></texture>";
	}
}
