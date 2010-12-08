package render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import window.Window;

public class Renderer {
	private static Renderer uniqueInstance;
	private Window window;

	public static Renderer getInstance() {
		if (uniqueInstance == null)
			uniqueInstance = new Renderer();

		return uniqueInstance;
	}

	public Renderer() {

	}

	public void createRenderer() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
			Display.setTitle("JGE3d");
			Display.setVSyncEnabled(true);

			window = new Window();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void destroy() {
		window.destroy();
	}

	public void draw() {
		//while (!Display.isCloseRequested() && Controller.getRunning()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GLU.gluLookAt(-5, 0, 0, 0, 0, 0, 0, 1, 0);

			// Draw a test object
			drawPoly();

			window.draw();
			Display.update();

			// Reduce input lag
			GL11.glGetError(); // this call will burn the time between vsyncs
			Display.processMessages(); // process new native messages since
										// Display.update();
			Mouse.poll(); // now update Mouse events
			Keyboard.poll(); // and Keyboard too

			/*
			 * Thread.currentThread(); try { Thread.sleep(10); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		//}
	}

	private void drawPoly() {
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