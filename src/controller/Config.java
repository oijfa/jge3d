package controller;

import java.util.HashMap;

import window.tree.Model;
import entity.EntityList;

public class Config {
	private static HashMap<String, ConfigItem> configs;
	private static String currentKey;
	
	Config(){
		configs = new HashMap<String, ConfigItem>();
		currentKey = null;
	}
	
	public void synchronized addConfig(String name, EntityList entityList, Model treeModel){
		if(currentKey == null){
			currentKey = name;
		}
		configs.put(name, new ConfigItem(entityList,treeModel));
	}
	
	public EntityList entlist() throws Exception{
		if(currentKey == null){
			throw new Exception("EntList: No config to fetch");
		}
		return configs.get(currentKey).list;
	}
	
	public Model model() throws Exception{
		if(currentKey == null){
			throw new Exception("TreeModel: No config to fetch");
		}
		return configs.get(currentKey).treeModel;
	}
	
	
	public void changeConfig(String key) throws Exception{
		if(!configs.containsKey(key)){
			throw new Exception("Can't change to a config that doesn't exist...");
		}
		currentKey = key;
	}
	
	class ConfigItem {
		public EntityList list;
		public Model treeModel;
		
		public ConfigItem(EntityList list,Model treeModel){
			this.list = list;
			this.treeModel = treeModel;
		}
	}
}
