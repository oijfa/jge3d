package engine.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import engine.Engine;

public class AIManager {
	//HashMap<String, ScriptClassOrWhatever> scripts; //List of all scripts
	private HashMap<String, ArrayList<Method>> script_assignments;
	
	public AIManager(){
		script_assignments = new HashMap<String, ArrayList<Method>>();
	}
	
	//TODO:  Decide whether the same script multiple times is a bad idea
	public void assignScript(String ent_name, String... script_names){
		for(String script_name : script_names){
			try {
				ArrayList<Method> script_list;
				
				Method method = AIFunctions.class.getMethod(script_name, Engine.class, Actor.class);
				
				if(!script_assignments.containsKey(ent_name)){
					script_list = new ArrayList<Method>();
					script_assignments.put(ent_name, script_list);
				}else{
					script_list = script_assignments.get(ent_name);
				}
	
				script_list.add(method);
			} catch (Exception e) {
				System.out.println("Tried to assign script that doesn't exist: " + script_names.toString());
				e.printStackTrace();
			}
		}
	}
	
	public void unassignScript(String ent_name, String script_name){
		ArrayList<Method> script_list = script_assignments.get(ent_name);
		if(script_list != null){
			if(script_list.size() > 0){
				script_list.remove(script_name);
			}else{
				script_assignments.remove(ent_name);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Method> getScripts(String ent_name){
		ArrayList<Method> methods = script_assignments.get(ent_name);
		if( methods != null){
			methods = (ArrayList<Method>) methods.clone();
		}
		return methods;
	}

	public void invokeAllMethods(Entity ent, Engine engine) {
		ArrayList<Method> methods = getScripts((String)ent.getProperty(Entity.NAME));
		if( methods != null ){
			for (Method method : methods){
				try {
					method.invoke(AIFunctions.class,engine,ent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void invokeAllMethodsForAllEnts(Engine engine) {
		for(String ent_name: script_assignments.keySet()) {
			Actor actor = engine.getActor(ent_name);
			ArrayList<Method> methods = getScripts((String)actor.getProperty(Entity.NAME));
			if( methods != null ){
				for (Method method : methods){
					try {
						method.invoke(AIFunctions.class,engine,actor);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}		
	}
}
