package render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
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
	private float x=0,y=0,z=0;
	
	private float nearClipping = 1.0f;
	private float farClipping = 1000.0f;
	private float zoom = 1;  //The closer this value is to 0, the farther you are zoomed in.
	
	//Default light (needs turning into an entity
    private float lightAmbient[]={ 0.5f, 0.5f, 0.5f, 1.0f };    // Ambient Light Values ( NEW )
    private float lightDiffuse[]={ 1.0f, 1.0f, 1.0f, 1.0f };    // Diffuse Light Values ( NEW )
    private float lightPosition[]={ 0.0f, 0.0f, 2.0f, 1.0f };   // Light Position ( NEW )
	
	public static Renderer getInstance() {
		if (uniqueInstance == null)
			uniqueInstance = new Renderer();

		return uniqueInstance;
	}

	public Renderer(){}

	public void draw() {
		//while (!Display.isCloseRequested() && Controller.getRunning()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
			GL11.glLoadIdentity();
			GLU.gluLookAt(0, -5f, -10f, 0, 0, 0, 0, 1, 0);

			// Draw a test object
			drawPoly();

			window.draw();
			
			GL11.glFlush();
			Display.update();

			// Reduce input lag
			//GL11.glGetError(); // this call will burn the time between vsyncs
			Display.processMessages(); // process new native messages since
										// Display.update();
			Mouse.poll(); // now update Mouse events
			Keyboard.poll(); // and Keyboard too

			/*
			 * Thread.currentThread(); try { Thread.sleep(10); } catch
			 * (InterruptedException e) { 
			 * e.printStackTrace(); }
			 */
		//}
	}

	private void drawPoly() {
		x++;y++;z++;
		for(int i=0;i<3;i++) {
			GL11.glPushMatrix();
			
			switch(i) {
				case 0: GL11.glTranslatef(0.0f, 1.5f, 0.0f); break;
				case 1: GL11.glTranslatef(-1.0f, -1.0f, 0.0f); break;
				case 2: GL11.glTranslatef(1.0f, -1.0f, 0.0f); break;
				default: System.out.print(i+"\n"); break;
			}
			switch(i) {
				case 0: GL11.glRotatef(x*-1,y,		z,		0);; break;
				case 1: GL11.glRotatef(x,	y*-1,	z,		0);; break;
				case 2: GL11.glRotatef(x,	y		,z*-1,	0);; break;
				default: System.out.print(i+"\n"); break;
			}
			
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
			GL11.glPopMatrix();
		}
	}
	
	public void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(1024,768));
			Display.create();
		} catch (LWJGLException e) {
			System.out.println("***Failed to create Display***");
			e.printStackTrace();
		}
		
		Display.setTitle("JGE3d");
		Display.setVSyncEnabled(true);
		
		window = new Window();
		
		//initialize the view
		GL11.glEnable(GL11.GL_TEXTURE_2D);     
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		setPerspective();
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		//Initialize default settings
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
        GL11.glEnable(GL11.GL_LIGHT1);    
		GL11.glEnable(GL11.GL_LIGHTING);
		
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH_HINT);
        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	}
	
	public void setPerspective(){setPerspective(nearClipping,farClipping,zoom);}
	public void setPerspective(float near, float far, float zoomVal) {
		nearClipping = near;
		farClipping = far;
		if(zoomVal <= 1.0 && zoomVal > 0){
			zoom = zoomVal;
		}else if(zoomVal > 1.0){
			zoomVal = 1.0f;
		}else{
			zoomVal = 0.1f; //TODO:  I guess this is the smallest zoom we'd want?
		}
		
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(
				20.0f, 
				(float) window.getWidth() / window.getHeight(), 
				nearClipping, 
				farClipping);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void destroy() {window.destroy();}
}