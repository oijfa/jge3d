package engine.entity;

import javax.vecmath.Vector3f;

import engine.Engine;

public class EntityCallbackFunctions {
	public EntityCallbackFunctions(String... filenames){
		//TODO: Parse files to create moar functions
	}
	
	private static Boolean hasFired = false;
	public static void explode(Entity source, Entity collided_with, Engine engine){
		if(!hasFired) {
			engine.getEntity("model4").applyImpulse(new Vector3f(0,15,0), new Vector3f(0, 0, 0));
			engine.getEntity("model4").activate();
			hasFired=true;
		}
	}
}
