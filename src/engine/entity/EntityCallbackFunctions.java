package engine.entity;

public class EntityCallbackFunctions {
	public EntityCallbackFunctions(String... filenames){
		//TODO: Parse files to create moar functions
	}
	
	public static void explode(Entity source, Entity collided_with){
		System.out.println(
			"BOOOM!!! " + source.getProperty(Entity.NAME)
			+ " collided with " + collided_with.getProperty(Entity.NAME)
		);
	}
}
