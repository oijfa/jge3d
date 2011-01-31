package controller;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import window.tree.Model;

public class Config {
	private static HashMap<String, ConfigItem> configs = new HashMap<String, ConfigItem>();
	private static String currentKey;
	
	public synchronized static void addConfig(String name, Vector3f position, Model treeModel){
		if(currentKey == null){
			currentKey = name;
		}
		configs.put(name, new ConfigItem(name, treeModel));
	}
	
	
	public synchronized static Model treeModel() throws Exception{
		if(currentKey == null){
			throw new Exception("TreeModel: No config to fetch");
		}
		return configs.get(currentKey).treeModel;
	}
	
	public synchronized static void changeConfig(String key) throws Exception{
		if(!configs.containsKey(key)){
			throw new Exception("Can't change to a config that doesn't exist...");
		}
		currentKey = key;
	}
	
	static class ConfigItem {
		public Model treeModel;
		public String name;
		
		public ConfigItem(String name, Model treeModel){
			this.treeModel = treeModel;
			this.name = name;
		}
	}

	public static String getName() throws Exception {
		if(currentKey == null){
			throw new Exception("Name: No config to fetch");
		}
		return configs.get(currentKey).name;
	}
}
