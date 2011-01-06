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

import monitoring.EntityObserver;
import monitoring.Observer;
import monitoring.Subject;

import physics.Physics;

public class EntityList implements Subject, EntityObserver{
	private HashMap<String,Entity> names;
	private Physics physics;
	private ArrayList<Observer> observers;
	private HashMap<String,TypedConstraint> constraints;
	
	public EntityList(Physics physics){
		names = new HashMap<String,Entity>();
		this.physics=physics;
		observers = new ArrayList<Observer>();
		constraints = new HashMap<String,TypedConstraint>();
	}
	public boolean addItem(Entity e){
		if(e.keyExists("name")){
			names.put((String)e.getProperty("name"), e);
			names.size();
			physics.addEntity(e);
			e.registerObserver(this);
			return true;
		}else{
			return false;
		}
	}
	public void removeItem(String name){
		Entity ent = this.getItem(name);
		names.remove(name);
		ent.removeObserver(this);
		}
	public Entity getItem(String name){
		//System.out.println("Length of list: " + String.valueOf(names.size()));
		//System.out.println("List: " + names.toString());
		return names.get(name);
	}
	public void drawList(){ 
		for(String key:names.keySet()){ 
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
	
	/* Subject implementation */
	@Override
	public void registerObserver(Observer o) {
	
	}
	@Override
	public void removeObserver(Observer o) {
		
	}
	@Override
	public void notifyObservers() {
		
	}

	@Override
	public void update(String key, Object old_val, Object new_val) {
		if(key == "name"){
			Entity ent = this.getItem((String) old_val);
			this.removeItem((String) old_val);
			this.addItem(ent);
			System.out.println(this.size());
			System.out.println("UPDATE RAN!");
		}
		
	}
}