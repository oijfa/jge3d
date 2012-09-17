package engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bulletphysics.collision.dispatch.CollisionObject;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

public class EntityList implements EntityListener, Iterable<Entity> {
	private HashMap<Object, Entity> entities;
	private ArrayList<ActionListener> action_listeners;
	private ArrayList<EntityListListener> listeners;

	public EntityList() {
		entities = new HashMap<Object, Entity>();
		action_listeners = new ArrayList<ActionListener>();
		listeners = new ArrayList<EntityListListener>();
	}
	
	/*************** QUEUE METHODS ***************/
	public void addEntity(Entity ent) {
		fireActionEvent();
		for(EntityListListener listener : listeners){
			listener.entityAdded(ent);
		}
		entities.put(ent.getProperty(Entity.NAME), ent);
		ent.addListener(this);
		for(String key: ent.getKeySet()) {
			if(key.equals(Entity.POSITION))
				ent.setProperty(key,ent.getProperty(key,false));
			else if(!ent.getProperty(Entity.NAME).equals(Camera.NAME))
				ent.setProperty(key,ent.getProperty(key,true));
			else {
				System.out.println(
					"###ERROR - Adding ent: " + 
					ent.getProperty(Entity.NAME) + 
					" | Key: " + 
					key + 
					" | Value: " +
					ent.getProperty(key)
				); 
			}
		}
	}
	
	public void removeEntity(Object key) {
		if (entities.containsKey(key)) {
			Entity ent = (entities.get(key));

			entities.remove(ent.getProperty(Entity.NAME));

			fireActionEvent();
			for(EntityListListener listener : listeners){
				listener.entityRemoved(ent);
			}
			ent.removeListener(this);
		}
	}
	/**********************************************/
	
	/*************** ACCESSORS ***************/
	public Entity getItem(Object key) {
		return entities.get(key);
	}
	
	public int entityCount() {
		return this.getEntities().size();
	}

	public Set<Object> getKeySet() {
		return entities.keySet();
	}
	
	public ArrayList<Entity> getEntities() {
		Set<Entity> ents = new HashSet<Entity>();
		for(Entity e : entities.values()){
			ents.add(e);
		}
		return new ArrayList<Entity>(ents);
	}
	
	public ArrayList<Entity> getSubEntities(Entity ent) {
		ArrayList<Entity> ents = new ArrayList<Entity>();
		if(ent.getSubEntities().entityCount() > 0) {
			for(Entity sub_ent: ent.getSubEntities()) {
				ents.add(sub_ent);
				ents.addAll(getSubEntities(sub_ent));
			}
		}
			
		return ents;
	}
	
	public ArrayList<Entity> getEntitiesAndSubEntities() {
		Set<Entity> ents = new HashSet<Entity>();
		for(Entity e : entities.values()){
			ents.add(e);
			ents.addAll(getSubEntities(e));
		}
		return new ArrayList<Entity>(ents);
	}
	/******************************************/	
	
	public void fireActionEvent() {
		for (ActionListener al : action_listeners) {
			al.actionPerformed(new ActionEvent(this));
		}
	}
	
	public void fireActionEvent(String event) {
		for (ActionListener al : action_listeners) {
			al.actionPerformed(new ActionEvent(this, event));
		}
	}

	public void addActionListener(ActionListener al) {
		action_listeners.add(al);
	}

	@Override
	public Iterator<Entity> iterator() {
		if(entities != null)
			return new EntityIterator(entities);
		else
			return new EntityIterator();
	}
	
	public void addListener(EntityListListener listener){
		listeners.add(listener);
	}

	@Override
	public void entityPropertyChanged(String property, Entity entity) {
		if(property.equals("name")) {
			String name = (String) entity.getProperty("name");
			entities.values().remove(entity);
			entities.put(name,entity);
		}		
	}
	
	public Entity getEntityByCollisionObject(CollisionObject co) {
		for(Entity ent: getEntitiesAndSubEntities()){
			if(ent.getProperty(Entity.COLLISION_OBJECT).equals(co)) {
				return ent;
			}
		}
		return null;
	}
}