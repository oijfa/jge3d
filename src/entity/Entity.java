/*
 * This class is what holds any object in the world.
 * 	The user can add/remove custom properties (though a few are 
 * 	unremovable for internal engine reasons)
 * 
 * 	//TODO: Needs to store models, etc
 * 
 * 	//TODO: Needs a draw function so it can draw itself
 * 
 * 	//TODO: Needs to be listenable.  That way the Entity List can know when the name changes
 * 
 *	//TODO: Maybe come up with actions like rotate, etc that a user might want to access
 *			Programmatically, and use skynet code to queue them up and play them back?
 *			Would function for animations and such too?
 */
package entity;

import importing.pieces.Model;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import physics.Physics;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity extends RigidBody{
	//Properties
	protected HashMap<String,Object> data;
	private Model model;
	
	/*Properties the engine uses alot*/
	public static String NAME = "name";
	
	//Required keys
	private String[] reqKeys = {"name", "collidable", "TTL"};
	
	//Keep track of number of entities for naming purposes
	protected static int num_entities=0;
	
	private EntityList subEntities;
	
	/* Constructors */
	public Entity(RigidBodyConstructionInfo r, boolean collide) {
		super(r);
		initialSetup(collide);
	}
	public Entity(float f, MotionState m, CollisionShape c, boolean collide ) {
		super(f,m,c);
		initialSetup(collide);
	}
	public Entity(float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v);
		initialSetup(collide);
	}
	public Entity(String _name, RigidBodyConstructionInfo r, boolean collide) {
		super(r);
		initialSetup(collide);
	}
	public Entity(String _name,float f, MotionState m, CollisionShape c, boolean collide ) {
		super(f,m,c);
		initialSetup(collide);
	}
	public Entity(String _name,float f, MotionState m, CollisionShape c, Vector3f v, boolean collide ) {
		super(f,m,c,v);
		initialSetup(collide);
	}
	private void initialSetup(boolean c){
		num_entities++;
		data = new HashMap<String,Object>();
		data.put("name", "ent" + String.valueOf(num_entities));
		data.put("collidable", c);
		data.put("TTL", 0);
		Physics.getInstance().addEntity(this);
	}
	
	/* Setters */
	//TODO:  MAkes thiss nots uses Vector3fs
	public void setPosition(Object p) {
		/*
		 * There's no straight-forward way to move a RigidBody to some location
		 * 	So that's what this class does.  It takes an Object because of skynet code
		 *	//TODO:  maybe remove that?  Dunno if we're going to keep using skynet code 
		 *	//TODO:  Maybe remove the whole thing?
		 */
		try {
			Vector3f pos = ((Vector3f) p);
			Transform trans = new Transform();
			float[] f = new float[3];
		
			this.getCenterOfMassTransform(trans);
			trans.setIdentity();
			
			f[0] = pos.x;
			f[1] = pos.y;
			f[2] = pos.z;
			
			trans.origin.set(f);
			this.setCenterOfMassTransform(trans);
		} catch (Exception e) {
			System.out.print(p.toString() + "<< Possible Incorrect data type for position, must be Vector3f\n");
			e.printStackTrace();
		}
	}
	public void setProperty(String key, Object val){data.put(key,val);}
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

	/* Getters */
	public EntityList getSubEntities(){return subEntities;}
	public String[] getKeys() {return (String[])data.keySet().toArray();}
	public boolean keyExists(String prop_name){
		for( int i = 0; i < data.keySet().size(); i++){
			//TODO: Probably a better way to loop through the keys, but I'm lazy
			if((data.keySet().toArray())[i].equals(prop_name))
				return true;
		}
		return false;
	}
	public Object getProperty(String key){return data.get(key);}
	
	public void draw(){
		Vector3f pos = new Vector3f();
		this.getCenterOfMassPosition(pos);
		
		GL11.glPushMatrix();
			GL11.glTranslatef(pos.x, pos.y, pos.z);
			if( model != null)
				model.draw();
		GL11.glPopMatrix();
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	public Model getModel() {
		return model;
	}
}
