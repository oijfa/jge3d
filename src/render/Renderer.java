package render;

import org.lwjgl.opengl.GL11;

public class Renderer {
	private static Renderer uniqueInstance;
	
	public static Renderer getInstance(){
		if( uniqueInstance == null)
			uniqueInstance = new Renderer();
		
		return uniqueInstance;
	}
	
	private Renderer(){
	}
	
	public void draw(){
		drawPoly();
	}
	
	private void drawPoly(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
	 
	 
		GL11.glBegin(GL11.GL_TRIANGLES);
	 
		// Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
	 
		// Right Side Facing Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		// Left Side Facing Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
	 
		// Bottom
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.1f, 0.1f, 0.1f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.2f, 0.2f, 0.2f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		GL11.glEnd();
	}
}