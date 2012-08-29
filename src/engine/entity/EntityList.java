package engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

public class EntityList implements ActionListener, Iterable<Entity> {
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
		entities.put(ent.getProperty("name"), ent);

		fireActionEvent();
		for(EntityListListener listener : listeners){
			listener.entityAdded(ent);
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
	/******************************************/	

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
	}
	
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
}