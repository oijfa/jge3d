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

import importing.pieces.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.vecmath.Vector3f;

import monitoring.EntityObserver;


import org.lwjgl.opengl.GL11;


import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity extends RigidBody{
	//Properties
	protected HashMap<String,Object> data;
	private Model model;
	protected ArrayList<EntityObserver> observers;

	/*Properties the engine uses a lot*/
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
		
		observers = new ArrayList<EntityObserver>();
	}
	
	/* Setters */
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
		
			this.getCenterOfMassTransform(trans);
			trans.setIdentity();
			
			trans.origin.set(pos);
			this.setCenterOfMassTransform(trans);
		} catch (Exception e) {
			System.out.print(p.toString() + "<< Possible Incorrect data type for position, must be Vector3f\n");
			e.printStackTrace();
		}
	}
	public Vector3f getPosition(){
		Vector3f out = new Vector3f();
		this.getCenterOfMassPosition(out);
		return out;
	}
	
	// SET PROPERTY!!!
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

	/* Getters */
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
	
	public Set<String> getKeySet(){
		return data.keySet();
	}
	
}
