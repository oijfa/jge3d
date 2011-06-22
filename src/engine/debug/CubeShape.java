package engine.debug;

import engine.importing.pieces.Material;

import org.lwjgl.opengl.GL11;

public class CubeShape {

	/* Debugging BS */
	// This is for debugging hitbox positions (should be deleted later)
	public static void drawTestCube(float half_extent) {
		Material mat = new Material();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT,
			mat.getAmbientAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE,
			mat.getDiffuseAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR,
			mat.getSpecularAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION,
			mat.getEmissionAsBuffer());
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS,
			mat.getShine());
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR);

		GL11.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
		// Front Face
		GL11.glNormal3f(0.0f, 0.0f, half_extent);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-half_extent, -half_extent, half_extent); // Bottom Left
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(half_extent, -half_extent, half_extent); // Bottom Right
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(half_extent, half_extent, half_extent); // Top Right Of
																// The Texture
																// and Quad
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(-half_extent, half_extent, half_extent); // Top Left Of
																	// The
																	// Texture
																	// and Quad

		// Back Face
		GL11.glNormal3f(0.0f, 0.0f, -half_extent);
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(-half_extent, -half_extent, -half_extent); // Bottom
																	// Right Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(-half_extent, half_extent, -half_extent); // Top Right
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(half_extent, half_extent, -half_extent); // Top Left Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(half_extent, -half_extent, -half_extent); // Bottom Left
																	// Of The
																	// Texture
																	// and Quad

		// Top Face
		GL11.glNormal3f(0.0f, half_extent, 0.0f);
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(-half_extent, half_extent, -half_extent); // Top Left Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-half_extent, half_extent, half_extent); // Bottom Left
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(half_extent, half_extent, half_extent); // Bottom Right
																// Of The
																// Texture and
																// Quad
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(half_extent, half_extent, -half_extent); // Top Right Of
																	// The
																	// Texture
																	// and Quad

		// Bottom Face
		GL11.glNormal3f(0.0f, -half_extent, 0.0f);
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(-half_extent, -half_extent, -half_extent); // Top Right
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(half_extent, -half_extent, -half_extent); // Top Left Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(half_extent, -half_extent, half_extent); // Bottom Left
																	// Of The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(-half_extent, -half_extent, half_extent); // Bottom
																	// Right Of
																	// The
																	// Texture
																	// and Quad

		// Right face
		GL11.glNormal3f(half_extent, 0.0f, 0.0f);
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(half_extent, -half_extent, -half_extent); // Bottom
																	// Right Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(half_extent, half_extent, -half_extent); // Top Right Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(half_extent, half_extent, half_extent); // Top Left Of
																// The Texture
																// and Quad
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(half_extent, -half_extent, half_extent); // Bottom Left
																	// Of The
																	// Texture
																	// and Quad

		// Left Face
		GL11.glNormal3f(-half_extent, 0.0f, 0.0f);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-half_extent, -half_extent, -half_extent); // Bottom
																	// Left Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, 0.0f);
		GL11.glVertex3f(-half_extent, -half_extent, half_extent); // Bottom
																	// Right Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(half_extent, half_extent);
		GL11.glVertex3f(-half_extent, half_extent, half_extent); // Top Right Of
																	// The
																	// Texture
																	// and Quad
		GL11.glTexCoord2f(0.0f, half_extent);
		GL11.glVertex3f(-half_extent, half_extent, -half_extent); // Top Left Of
																	// The
																	// Texture
																	// and Quad
		GL11.glEnd();

		GL11.glDisable(GL11.GL_COLOR);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
