/*
package window.tree;

import de.matthiasmann.twl.model.HasCallback;
import entity.Entity;

public class EditStringModel extends HasCallback{
	private String value;
	private String key;
	private Entity entity;
	public EditStringModel(String key, Entity entity){
		super();
		this.value = (String) entity.getProperty(key);
		this.key = key;
		this.entity = entity;
	}
	
	public Entity getEntity(){
		return this.entity;
	}
	public String getKey(){
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		if(!this.value.equals(value)){
			this.value = value;
			doCallback();
		}
		entity.setProperty(key, value);
	}
}
*/