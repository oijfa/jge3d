package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import monitoring.EntityListObserver;
import monitoring.EntityObserver;

import physics.Physics;

public class EntityList implements EntityObserver{
	private HashMap<String,Entity> names;
	private Physics physics;

	//private HashMap<String,TypedConstraint> constraints;

	private ArrayList<EntityListObserver> observers;
	private ArrayList<QueueItem> queue;
	
	public EntityList(Physics physics){
		names = new HashMap<String,Entity>();
		this.physics=physics;
		observers = new ArrayList<EntityListObserver>();
		queue = new ArrayList<QueueItem>();
		
		//constraints = new HashMap<String,TypedConstraint>();
	}
	
	public void drawList(){ 
		//Have to change keySet into array so that a clone will be made
			//Avoids concurrency issues
		for(Object key:names.keySet().toArray()){ 
			names.get(key).draw(); 
		}
	}
	
	public void parseQueue() {
		for(QueueItem item:queue) {
			if(QueueItem.ADD == item.getAction())
				addItem(item.getEnt());
			else if(QueueItem.REMOVE == item.getAction())
				removeItem(item.getEnt());
		}
	}
	
	/* ACCESSORS */
	public Entity getItem(String name){return names.get(name);	}
	public int size(){return names.size();}
	public Set<String> getKeySet(){return names.keySet();}
	public Physics getPhysics() {return physics;}
	public int queueSize(){return queue.size();}
	
	/* MUTATORS */
	//Add an item to the entity List
	private boolean addItem(Entity e){
		boolean ret = false;
		
		if(e.keyExists("name")){
			names.put((String)e.getProperty("name"), e);
			names.size();
			physics.addEntity(e);
			e.registerObserver(this);
			notifyObservers();
			ret = true;
		}
		return ret;
	}
	private void removeItem(Entity entity){
		names.remove(entity);
		entity.removeObserver(this);
		physics.removeEntity(entity);
		notifyObservers();
	}
	//Set actions that need to wait on the physics
	public void enqueue(Entity ent, int action) {
		queue.add(new QueueItem(ent,action));
	}
	
	/* ENTITY OBSERVER IMPLEMENTATION */
	public void update(String key, Object old_val, Object new_val) {
		if(key == "name"){
			Entity ent = this.getItem(key);
			enqueue(ent, QueueItem.REMOVE);
			this.addItem(ent);
			notifyObservers();
		}
	}
	
	/* SUBJECT IMPLEMENTATION */
	public void registerObserver(EntityListObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(EntityListObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++){
			EntityListObserver observer = (EntityListObserver)observers.get(i);
			observer.update();
		}
	}
	
	/*Physics Constraints*/
	/*public void addBallJoint(String name, Entity object1, Vector3f point1, Entity object2, Vector3f point2){
		//Setup a Ball joint between the two objects, at the point given
		Point2PointConstraint ballJoint = new Point2P*/
	/*ointConstraint(
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
	}*/
}