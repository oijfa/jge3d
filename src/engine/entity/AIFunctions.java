package engine.entity;

import javax.vecmath.Vector3f;

import engine.Engine;

public class AIFunctions {
	public static void testFunction(Engine engine, Entity ent){
		System.out.println("testFunction AIFunction called.");
	}
	
	public static void followPlayer(Engine engine, Entity ent){
		Entity player = engine.getEntity("player");
		if( player != null ){
			Vector3f player_pos = player.getPosition();
			player_pos.sub(ent.getPosition());
			ent.activate();
			System.out.println(player_pos.toString());
			ent.applyImpulse(player_pos, new Vector3f(0,0,0));
		}
	}
}