/*
 * The list of entities that the renderer will be drawing.
 * 
 * The reason its in a class rather than just using a hashmap is it will eventually 
 * listen to all of the entities it contains, in case their name changes.  (It can change 
 * the key they are saved in)
 * 
 * //TODO:	Listen to the entities it contains.  Yes, that means right now the class has 
 * 			no purpose other than being a placeholder
 */
package entity;

import java.util.ArrayList;
import java.util.HashMap;


import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;


import java.util.Set;
import monitoring.EntityListObserver;

import monitoring.EntityObserver;
import physics.Physics;

public class EntityList implements EntityObserver{
	private HashMap<String,Entity> names;
	private Physics physics;

	private HashMap<String,TypedConstraint> constraints;

	private ArrayList<EntityListObserver> observers;

	
	public EntityList(Physics physics){
		names = new HashMap<String,Entity>();
		this.physics=physics;
		constraints = new HashMap<String,TypedConstraint>();
		observers = new ArrayList<EntityListObserver>();
	}
	public boolean addItem(Entity e, Object starter){
		if(e.keyExists("name")){
			names.put((String)e.getProperty("name"), e);
			names.size();
			physics.addEntity(e);
			e.registerObserver(this);
			notifyObservers(starter);
			return true;
		}else{
			return false;
		}
	}
	public void removeItem(String name, Object starter){
		Entity ent = this.getItem(name);
		names.remove(name);
		ent.removeObserver(this);
		notifyObservers(starter);
		}
	public Entity getItem(String name){
		//System.out.println("Length of list: " + String.valueOf(names.size()));
		//System.out.println("List: " + names.toString());
		return names.get(name);
	}
	public void drawList(){ 
		//Have to change keySet into array so that a clone will be made
			//Avoids concurrency issues
		for(Object key:names.keySet().toArray()){ 
			names.get(key).draw(); 
		}
	}
	
	public int size(){return names.size();}
	
	/*Physics Constraints*/
	public void addBallJoint(String name, Entity object1, Vector3f point1, Entity object2, Vector3f point2){
		//Setup a Ball joint between the two objects, at the point given
		Point2PointConstraint ballJoint = new Point2PointConstraint(
			object1,
			object2,
			point1,
			point2
		);
		physics.getDynamicsWorld().addConstraint(ballJoint);
	}
	
	public void removeJoint(String constraint_name){
		if( constraints.containsKey(constraint_name) ){
			physics.getDynamicsWorld().removeConstraint(constraints.get(constraint_name));
			constraints.remove(constraint_name);
		}
	}
	
	public Set<String> getKeySet(){
		return names.keySet();
	}
	
	/* Subject implementation */
	public void registerObserver(EntityListObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(EntityListObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers(Object starter) {
		for(int i = 0; i < observers.size(); i++){
			EntityListObserver observer = (EntityListObserver)observers.get(i);
			if(starter != observer){
				observer.update(starter);
			}
		}
	}

	@Override
	public void update(String key, Object old_val, Object new_val, Object starter) {
		if(key == "name"){
			//this.removeItem((String) old_val);
			//this.addItem(ent);
			updateListItems((String) old_val);
			notifyObservers(starter);
		}
	}
	//Only called when entityList needs to be updated this way
	//We don't call notifyObservers twice for add and remove.
	public boolean updateListItems(String old_val){
		Entity ent = this.getItem(old_val);
		names.remove(old_val);
		ent.removeObserver(this);
		if(ent.keyExists("name")){
			names.put((String)ent.getProperty("name"), ent);
			names.size();
			physics.addEntity(ent);
			ent.registerObserver(this);
			return true;
		}else{
			return false;
		}
	}
	
	public Physics getPhysics() {
		return physics;
	}
}