/*
 * This class is what holds any object in the world.
 * 	The user can add/remove custom properties (though a few are 
 * 	unremovable for internal engine reasons)
 * 
 *	//TODO: Maybe come up with actions like rotate, etc that a user might want to access
 *			Programmatically, and use skynet code to queue them up and play them back?
 *			Would function for animations and such too?
 */
package entity;

import importing.pieces.Material;
import importing.pieces.Model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.vecmath.Vector3f;

import monitoring.EntityObserver;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity extends RigidBody{
	//Properties
	protected HashMap<String,Object> data;
	private Model model;
	protected ArrayList<EntityObserver> observers;
	private boolean shouldDraw = true;

	/*Properties the engine uses a lot*/
	public static String NAME = "name";
	
	//Required keys
	private String[] reqKeys = {"name", "collidable", "TTL"};
	
	//Keep track of number of entities for naming purposes
	protected static int num_entities=0;
	 
	//For making entity groups (complex bodies)
	private EntityList subEntities;
	
	/* Constructors */
	public Entity(float mass,CollisionShape shape, boolean collide ) {
		super(mass,new DefaultMotionState(),shape);
		createRigidBody(mass,shape,collide);
		initialSetup(collide);
	}
	public Entity(String name,float mass,CollisionShape shape, boolean collide) {
		super(mass,new DefaultMotionState(),shape);
		createRigidBody(mass,shape,collide);
		initialSetup(name,collide);
	}

	/* Initializing segments */
	//Creates the initial settings for a rigidbody
	//This function is what we use to make things rotate over multiple axes
	private void createRigidBody(float mass, CollisionShape shape, boolean collide) {
		// rigid body is dynamic if and only if mass is non zero,
		//otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0f, 0f, 0f);
		if (isDynamic) {
			shape = this.getCollisionShape();
			shape.calculateLocalInertia(mass, localInertia);
			this.setCollisionShape(shape);
		}	
		
		//This is extremely important; if you forget this
		//then nothing will rotate
		this.setMassProps(mass, localInertia);
		this.updateInertiaTensor();
	}
	//Sets the initial name of the body in the list
	//Also sets some default options to the ent
	private void initialSetup(boolean c){
		num_entities++;
		data = new HashMap<String,Object>();
		data.put("name", "ent" + String.valueOf(num_entities));
		data.put("collidable", c);
		data.put("TTL", 0);
		
		observers = new ArrayList<EntityObserver>();
	}
	private void initialSetup(String name, boolean c){
		num_entities++;
		data = new HashMap<String,Object>();
		data.put("name", name);
		data.put("collidable", c);
		data.put("TTL", 0);
		
		observers = new ArrayList<EntityObserver>();
	}
	
	
	/* Setters */
	public void setModel(Model model) {
		this.model = model;
	}
	public void setPosition(Object p) {
		try {
			Vector3f pos = ((Vector3f) p);
			Transform trans = this.getWorldTransform(new Transform());
			trans.origin.set(pos);
			this.setWorldTransform(trans);

		} catch (Exception e) {
			System.out.print(p.toString() + "<< Possible Incorrect data type for position, must be Vector3f\n");
			e.printStackTrace();
		}
	}
	public Vector3f getPosition(){
		Transform out = new Transform();
		out = this.getWorldTransform(new Transform());
		return out.origin;
	}
	public void setProperty(String key, Object val, Object starter){
			Object old_key_val = data.get(key);
			data.put(key,val);
			//starter is passed to tell when to end the horrible infinite loop
			notifyObservers(key, old_key_val, val, starter);
	}
	public void nodeUpdate(String key, Object val, Object starter){
		System.out.println("Entity.update");	
		this.setProperty(key, val, starter);
	}
	public void removeProperty(String key){
		//Protect our required keys. Don't delete those, oh no!
		boolean req = false;
		for( int i = 0; i < reqKeys.length; i++){
			if(reqKeys[i].equals(key))
				req = true;
		}
		if(!req){
			data.remove(key);
		}
	}
	public Set<String> getKeySet(){
		return data.keySet();
	}
	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}

	/* Getters */
	public Model getModel() {
		return model;
	}
	public EntityList getSubEntities(){return subEntities;}
	public Set<String> getKeys() {return data.keySet();}
	public boolean keyExists(String prop_name){
		for( int i = 0; i < data.keySet().size(); i++){
			//TODO: Probably a better way to loop through the keys, but I'm lazy
			if((data.keySet().toArray())[i].equals(prop_name))
				return true;
		}
		return false;
	}
	public Object getProperty(String key){return data.get(key);}
	public boolean shouldDraw() {
		return shouldDraw;
	}
	
	
	/* Drawing related stuff */
	//The entities drawing code is called from the entlist
	//when it is time to draw
	public void draw(){
		GL11.glPushMatrix();
			//Retrieve the current motionstate to get the transform
			//versus the world
			Transform transform_matrix = new Transform();
			DefaultMotionState motion_state = (DefaultMotionState) this.getMotionState();
			transform_matrix.set(motion_state.graphicsWorldTrans);
			
			//Adjust the position and rotation of the object from physics
			float[] body_matrix = new float[16];
			FloatBuffer buf = BufferUtils.createFloatBuffer(16);
			transform_matrix.getOpenGLMatrix(body_matrix);
			buf.put(body_matrix);
			buf.flip();
			GL11.glMultMatrix(buf);
			buf.clear();
			
			//Scaling code (testing)
			Vector3f halfExtent = new Vector3f();
			this.getCollisionShape().getLocalScaling(halfExtent);
			GL11.glScalef(1.0f * halfExtent.x, 1.0f * halfExtent.y, 1.0f * halfExtent.z);
			
			//Draw the model
			GL11.glPushMatrix();
				if( model != null)
					model.draw();		
			GL11.glPopMatrix();
			
			//Draw a test box too; just for now
			GL11.glPushMatrix();
				drawTestCube();
		    GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	
	/* Functions for EntityObservers */
	public void registerObserver(EntityObserver o) {
		observers.add(o);
	}
	public void removeObserver(EntityObserver o) {
		int i = observers.indexOf(o);
		if (i >= 0){
			observers.remove(i);
		}
	}
	public void notifyObservers(String key, Object old_name, Object new_name, Object starter) {
		for(int i = 0; i < observers.size(); i++){
			EntityObserver observer = (EntityObserver)observers.get(i);
			if(starter != observer){
				observer.update(key, old_name, new_name, starter);
			}	
		}
	}
	
	
	/* Debugging BS */
	//This is for debugging hitbox positions (should be deleted later)
	public void drawTestCube() {
		Material mat = new Material();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, mat.getAmbientAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, mat.getDiffuseAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, mat.getSpecularAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, mat.getEmissionAsBuffer());
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, mat.getShine());
        GL11.glBegin(GL11.GL_QUADS);
    	// Front Face
    	GL11.glNormal3f( 0.0f, 0.0f, 0.5f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-0.5f, -0.5f,  0.5f);   // Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f( 0.5f, -0.5f,  0.5f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f( 0.5f,  0.5f,  0.5f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f(-0.5f,  0.5f,  0.5f);   // Top Left Of The Texture and Quad

        // Back Face
        GL11.glNormal3f( 0.0f, 0.0f, -0.5f);
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f(-0.5f, -0.5f, -0.5f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f(-0.5f,  0.5f, -0.5f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f( 0.5f,  0.5f, -0.5f);   // Top Left Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 0.5f, -0.5f, -0.5f);   // Bottom Left Of The Texture and Quad

        // Top Face
        GL11.glNormal3f( 0.0f, 0.5f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f(-0.5f,  0.5f, -0.5f);   // Top Left Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-0.5f,  0.5f,  0.5f);   // Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f( 0.5f,  0.5f,  0.5f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f( 0.5f,  0.5f, -0.5f);   // Top Right Of The Texture and Quad

        // Bottom Face
        GL11.glNormal3f( 0.0f, -0.5f, 0.0f);
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f(-0.5f, -0.5f, -0.5f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f( 0.5f, -0.5f, -0.5f);   // Top Left Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 0.5f, -0.5f,  0.5f);   // Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f(-0.5f, -0.5f,  0.5f);   // Bottom Right Of The Texture and Quad

        // Right face
        GL11.glNormal3f( 0.5f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f( 0.5f, -0.5f, -0.5f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f( 0.5f,  0.5f, -0.5f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f( 0.5f,  0.5f,  0.5f);   // Top Left Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 0.5f, -0.5f,  0.5f);   // Bottom Left Of The Texture and Quad

        // Left Face
        GL11.glNormal3f( -0.5f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-0.5f, -0.5f, -0.5f);   // Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.0f); GL11.glVertex3f(-0.5f, -0.5f,  0.5f);   // Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(0.5f, 0.5f); GL11.glVertex3f(-0.5f,  0.5f,  0.5f);   // Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 0.5f); GL11.glVertex3f(-0.5f,  0.5f, -0.5f);   // Top Left Of The Texture and Quad
        GL11.glEnd();
	}
}
