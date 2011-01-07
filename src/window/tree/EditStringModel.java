package window.tree;

import de.matthiasmann.twl.model.HasCallback;
import de.matthiasmann.twl.model.StringModel;
import entity.Entity;

public class EditStringModel extends HasCallback implements StringModel{
	private String value;
	private String key;
	private Entity observer;
	private Object starter;
	public EditStringModel(String key, String defaultValue, Entity observer, Object starter){
		if(defaultValue == null){
			throw new NullPointerException("defaultValue Missing when creating EditStringModel");
		}
		this.value = defaultValue;
		this.key = key;
		this.observer = observer;
		this.starter = starter;
	}
	
	public Entity getObserver(){
		return this.observer;
	}
	public String getKey(){
		return this.key;
	}
	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		
			if(value == null){
				throw new NullPointerException("Value");
			}
			if(!this.value.equals(value)){
				this.value = value;
				doCallback();
			}
			this.notifyEnt(starter);
	}
	public void notifyEnt(Object starter){
		//starter check for the Entity happens here. It works better this way.
		if(starter != observer){
			observer.nodeUpdate(key, value, starter);
			System.out.println(observer.getProperty(key).toString());
		}
	}
	@Override
	public void addCallback(Runnable callback) {
		// TODO I don't why i need this.
		
	}

	@Override
	public void removeCallback(Runnable callback) {
		// TODO Or this....
		
	}
	
}