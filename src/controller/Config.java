package controller;

import importing.pieces.Model;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class Config {
	ArrayList<ConfigItem> items;
	
	public ConfigItem createItem(String name,String value,Vector3f pos,Model model,String parent_name){
		ConfigItem i = new ConfigItem(name,value,pos,model);
		if(parent_name == null){
			items.add(i);
		}else{
			for(ConfigItem e: items){
				if(e.getName().equals(parent_name)){
					e.items.add(i);
				}
			}
		}
		return i;
	}
	
	public class ConfigItem{
		private String name;
		private String value;
		private Vector3f pos;
		private Model model;
		ArrayList<ConfigItem> items;
		
		public ConfigItem(String name,String value,Vector3f pos,Model model){
			this.setName(name);
			this.setValue(value);
			this.setPos(new Vector3f(pos.x,pos.y,pos.z));
			this.setModel(model);
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setPos(Vector3f pos) {
			this.pos = pos;
		}

		public Vector3f getPos() {
			return pos;
		}

		public void setModel(Model model) {
			this.model = model;
		}

		public Model getModel() {
			return model;
		}
	}
}
