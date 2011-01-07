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
import java.util.Set;

import monitoring.EntityObserver;
import monitoring.Observer;
import monitoring.Subject;

import physics.Physics;

public class EntityList implements Subject, EntityObserver{
	private HashMap<String,Entity> names;
	private Physics physics;
	private ArrayList<Observer> observers;
	
	public EntityList(Physics physics){
		names = new HashMap<String,Entity>();
		this.physics=physics;
		observers = new ArrayList<Observer>();
	}
	public boolean addItem(Entity e){
		if(e.keyExists("name")){
			names.put((String)e.getProperty("name"), e);
			names.size();
			physics.addEntity(e);
			e.registerObserver(this);
			notifyObservers();
			return true;
		}else{
			return false;
		}
	}
	public void removeItem(String name){
		Entity ent = this.getItem(name);
		names.remove(name);
		ent.removeObserver(this);
		notifyObservers();
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
	
	public Set<String> getKeySet(){
		return names.keySet();
	}
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}
	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
	@Override
	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++){
			Observer observer = (Observer)observers.get(i);
			observer.update();
		}
	}

	@Override
	public void update(String key, Object old_val, Object new_val) {
		if(key == "name"){
			Entity ent = this.getItem((String) old_val);
			this.removeItem((String) old_val);
			this.addItem(ent);
		}
		
	}
}