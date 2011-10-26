package engine.entity;

import javax.vecmath.Vector3f;

import engine.Engine;

public class EntityCallbackFunctions {
	public EntityCallbackFunctions(String... filenames){
		//TODO: Parse files to create moar functions
	}
	
	private static Boolean hasFired = false;
	public static void explode(Entity source, Entity collided_with, Engine engine){
		if(!hasFired && collided_with.getProperty("name").equals("model2")) {
			engine.getEntity("model4").applyImpulse(new Vector3f(0,15,0), new Vector3f(0, 0, 0));
			engine.getEntity("model4").activate();
			hasFired=true;
		}
	}
	
	public static void explodeOnPlayerContact(Entity source, Entity collided_with, Engine engine){
		if(collided_with.getProperty("name").equals("player")) {
			engine.getEntity("model5").activate();
			engine.getEntity("model5").applyImpulse(new Vector3f(0,100,0), new Vector3f(0, 0, 0));
		}
	}
	
	@SuppressWarnings("unused")
	private void debug(Entity source, Entity collided_with, Engine engine) {
		System.out.println(
			"Source: " + source.getProperty("name") + 
			" Collided With: " + collided_with.getProperty("name")
		);
	}
}
