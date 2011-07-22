package engine.entity;

import javax.vecmath.Vector3f;

public class EntityCallbackFunctions {
	public EntityCallbackFunctions(String... filenames){
		//TODO: Parse files to create moar functions
	}
	
	private static Boolean hasFired = false;
	public static void explode(Entity source, Entity collided_with, EntityList ent_list){
		if(!hasFired) {
			System.out.println(
				"BOOOM!!! " + source.getProperty(Entity.NAME)
				+ " collided with " + collided_with.getProperty(Entity.NAME)
			);
			ent_list.getItem("model4").applyImpulse(new Vector3f(0,5,0), new Vector3f(0,0,0));
			
			hasFired=true;
		}
	}
}
