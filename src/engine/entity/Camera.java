package engine.entity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import engine.render.Model;

public class Camera extends Entity {
	/* Static class variables */
	// Don't flip over, its confusing.
	private static final double maximum_declination = (Math.PI / 2.0f) - 0.01f;
	private static final double minimum_declination = (-1.0f * ((Math.PI / 2.0f) - 0.01f));
	private static final double minimum_distance = 1;
	private static final double maximum_distance = 100f;
	public static final String CAMERA_NAME = "camera";
	

	/* Class fields */
	private double declination; // Angle up and down
	private double rotation; // Angle left and right
	private double distance; // distance from focus
	private Vector3f up_vector; // vector pointing up
	private Vector3f movement; //user control
	private long prev_time; //last update in nanosecs

	private volatile Entity focus;
	private volatile Entity default_focus;

	/* Constructors */
	public Camera(float mass, boolean collidable, Model model) {
	    this(mass, collidable, model, null);
	}
	
	public Camera(Float mass, boolean collide, Model model, Entity defFocus) {
		super(mass, collide, model, null);
		cameraInit(defFocus);
	}

  private void cameraInit(Entity defFocus) {
	  	prev_time = System.nanoTime();
	  	movement = new Vector3f();
		default_focus = defFocus;
		focus = default_focus;
		setProperty(Entity.NAME, "camera");
		setPosition(new Vector3f(0, 0, 0));
		declination = 0;
		rotation = 0;
		distance = 20.0f;
		setUpVector(new Vector3f(0, 1, 0));
		updatePosition();
		
		getModel().setTransparent();
	}

	public void focusOn(Entity newFocus) {
		if (newFocus == null) {
			focus = default_focus;
		} else {
			focus = newFocus;
		}
	}

	/* Accessors */
	public Vector3f getUp() {
		return up_vector;
	}

	public Vector3f getFocusPosition() {
		Vector3f temp = new Vector3f();
		if (focus != null) {
			temp = focus.getPosition();
		} else {
			temp.set(0, 0, 0);
		}
		return temp;
	}

	public Entity getFocus() {
		return focus;
	}
	
	/*
	public void freeRoam() {
		default_focus = new Entity("def_focus", 0f, false, FileLoader.loadFile("resources/models/misc/box.xgl"));
		focus = default_focus;
	}
	*/

	/* Mutators */
	private void setUpVector(Vector3f newUp) {
		up_vector = newUp;
	}

	public void setDistance(Float f) {
		if (f > maximum_distance) {
			distance = maximum_distance;
		} else {
			if (f < minimum_distance) {
				distance = minimum_distance;
			} else {
				distance = f;
			}
		}
	}

	public synchronized void incrementDistance(Double change) {
		double temp = distance + change;
		if (temp > maximum_distance) {
			distance = maximum_distance;
		} else if (temp < minimum_distance) {
			distance = minimum_distance;
		} else {
			distance = temp;
		}
	}
	
	public synchronized void incrementPosition(float toward_focus, float strafe, float up_down) {
		//float a = 0;
		Vector3f position = new Vector3f();
		//Vector3f focPos = getFocusPosition();

		double xrotrad, yrotrad;
	    yrotrad = declination / 180 * 3.141592654f;
	    xrotrad = rotation / 180 * 3.141592654f;
	    position.x += Math.sin(yrotrad);
	    position.z -= Math.cos(yrotrad);
	    position.y -= Math.sin(xrotrad);
	    setPosition(position);
	}
		
	public double getDistance() {
		return distance;
	}

	public void incrementDeclination(Double angle) {
		declination += angle;
		if (declination > maximum_declination) {
			declination = maximum_declination;
		}
		if (declination < minimum_declination) {
			declination = minimum_declination;
		}

		// System.out.println(distance);
		// Not needed because renderer always calls it
		// updatePosition();
	}

	public void setDeclination(Double angle) {
		this.declination = angle;
	}

	public void incrementRotation(Double angle) {
		rotation += angle;
		/*
		 * Checks if rotation is over 2*Pi and adjusts it accordingly. This way
		 * the camera's rotation doesn't lock up(Slow Down) at around 2*Pi +
		 * Pi/2 of the rotation
		 */
		if (Math.abs(rotation) > 6.28318531f) {
			if (rotation > 0) {
				rotation -= 6.28318531f;
			} else if (rotation < 0) {
				rotation += 6.28318531f;
			}
		}
		// Not needed because renderer always calls it
		// updatePosition();
	}

	public void setRotation(Double angle) {
		this.rotation = angle;
	}

	public void addMovement(Vector3f movement) {
		this.movement.add(movement);
	}
	
	public void subMovement(Vector3f movement) {
		this.movement.sub(movement);
	}
	
	public void updatePosition() {
		float a = 0;

		Vector3f adjusted_movementVector3f = new Vector3f(movement);
		adjusted_movementVector3f.scale(prev_time*0.0000000000000002f);
		if(!movement.equals(new Vector3f(0,0,0))) {
			incrementRotation(((Float)adjusted_movementVector3f.x).doubleValue());
			incrementDeclination(((Float)adjusted_movementVector3f.y).doubleValue());
			incrementDistance(((Float)adjusted_movementVector3f.z).doubleValue());
		}
		prev_time = System.nanoTime();
		
		Vector3f position = new Vector3f();
		Vector3f focPos = getFocusPosition();

		// calculate positions from angles as if focus were (0,0,0)
		position.y = (float) ((distance * Math.sin(declination)));
		a = (float) ((distance * Math.cos(declination)));
		position.x = (float) (a * Math.sin(rotation));
		position.z = (float) (a * Math.cos(rotation));
		position.add(focPos);
		setPosition(position);
	}

	/* debug functions */
	public void debug() {
		// Debug the camera
		// System.out.print("Height:		" + height + "	Width:	" + width + "\n");
		Vector3f position = new Vector3f();
		position = this.getPosition();

		Vector3f focpos = this.getFocusPosition();
		System.out.print(
			"Camera = X:	" + position.x + "	Y:	" + position.y
			+ "	Z:	" + position.z + "\n"
		);
		System.out.print(
			"Focus  = X:	" + focpos.x + "	Y:	" + focpos.y + "	Z:	"
			+ focpos.z + "\n"
		);
		System.out.print(
			"Up     = X:	" + up_vector.x + "	Y:	" + up_vector.y
			+ "	Z:	" + up_vector.z + "\n\n"
		);
	}

	// TODO: This needs cleaned up and commented real bad
	// TODO: Magic Number for far clipping
	public Vector3f getRayTo(int x, int y) {
		return getRayTo(x, y, (int) 1000);
	}

	public Vector3f getRayTo(int x, int y, int farDistance) {
		// Create stupid floatbuffers for LWJGL
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		Vector3f pos = new Vector3f();

		// Get some information about the viewport, modelview, and projection
		// matrix
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		// Find the depth to
		GL11.glReadPixels(
			x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT,	winZ
		);

		// get the position in 3d space by casting a ray from the mouse
		// coords to the first contacted point in space
		// GLU.gluUnProject(mouseX, mouseY, winZ.get(), modelview, projection,
		// viewport, position);
		GLU.gluUnProject(
			x, y, winZ.get(), modelview, projection, viewport, position
		);

		// Make a vector out of the silly float buffer LWJGL forces us to use
		pos.set(position.get(0), position.get(1), position.get(2));

		return pos;
	}
}
