package engine.entity;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import engine.physics.Physics;

public class EntityList{
	private HashMap<String,Entity> names;
	private Physics physics;

	//private HashMap<String,TypedConstraint> constraints;

	private ConcurrentLinkedQueue<QueueItem> physicsQueue;
	private ConcurrentLinkedQueue<QueueItem> renderQueue;
	
	public EntityList(Physics physics){
		names = new HashMap<String,Entity>();
		this.physics=physics;
		
		physicsQueue = new ConcurrentLinkedQueue<QueueItem>();
		renderQueue = new ConcurrentLinkedQueue<QueueItem>();
	}
	
	public void drawList(){ 
		//Have to change keySet into array so that a clone will be made
			//Avoids concurrency issues
		for(Object key:names.keySet().toArray()){ 
			names.get(key).draw(); 
		}
	}
	
	public void parsePhysicsQueue() {
		Object[] itemArray = physicsQueue.toArray();
		for(Object item:itemArray) {
			if(QueueItem.ADD == ((QueueItem) item).getAction())
				addPhysicsItem(((QueueItem) item).getEnt());
			else if(QueueItem.REMOVE == ((QueueItem) item).getAction())
				removePhysicsItem(((QueueItem) item).getEnt());
			physicsQueue.remove(item);
		}
	}
	
	public void parseRenderQueue() {
		Object[] itemArray = renderQueue.toArray();
		for(Object item:itemArray) {
			if(QueueItem.ADD == ((QueueItem) item).getAction())
				addRenderItem(((QueueItem) item).getEnt());
			else if(QueueItem.REMOVE == ((QueueItem) item).getAction())
				removeRenderItem(((QueueItem) item).getEnt());
			
			renderQueue.remove(item);
		}
	}
	
	/* ACCESSORS */
	public Entity getItem(String name){return names.get(name);	}
	public int size(){return names.size();}
	public Set<String> getKeySet(){return names.keySet();}
	public Physics getPhysics() {return physics;}
	public int physicsQueueSize(){return physicsQueue.size();}
	public int renderQueueSize(){return renderQueue.size();}
	
	/* MUTATORS */
	//Add an item to the entity List
	private boolean addPhysicsItem(Entity e){
		boolean ret = false;
		
		if(e.keyExists("name")){
			if( e.getCollisionObject() != null ){
				physics.getDynamicsWorld().addCollisionObject(e.getCollisionObject());
			}
			ret = true;
		}
		return ret;
	}
	private void addRenderItem(Entity e){
		if(e.keyExists("name")){
			if( e.getModel() != null ){
				e.getModel().createVBO();
			}
		}
	}
	private void removeRenderItem(Entity e){
		if(e.keyExists("name")){
			if( e.getModel() != null ){
				e.getModel().destroyVBO();
			}
		}
	}
	private void removePhysicsItem(Entity entity){
		names.remove(entity);
		physics.removeEntity(entity);
	}
	
	public void addEntity(Entity ent){
		names.put((String)ent.getProperty("name"), ent);
		physicsQueue.add(new QueueItem(ent,QueueItem.ADD));
		renderQueue.add(new QueueItem(ent,QueueItem.ADD));
	}
	
	public void updateEntity(Entity ent){
		renderQueue.add(new QueueItem(ent,QueueItem.ADD));
	}
	
	public void removeEntity(String name){
		if(names.containsKey(name)){
			removeEntity(names.get(name));
		}
	}
	public void removeEntity(Entity ent){ 
	    names.remove(ent.getProperty(Entity.NAME));
	    physicsQueue.add(new QueueItem(ent,QueueItem.REMOVE));
	    renderQueue.add(new QueueItem(ent,QueueItem.REMOVE));
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