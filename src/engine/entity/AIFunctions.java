package engine.entity;

import javax.vecmath.Vector3f;

import engine.Engine;

public class AIFunctions {
	public static void testFunction(Engine engine, Entity ent){
		System.out.println("testFunction AIFunction called.");
	}
	
	public static void followPlayer(Engine engine, Actor ent){
		Actor player = (Actor)engine.getEntity("player");
		if( player != null ){
			//ent.clearForces();
			Vector3f towards_pos = new Vector3f();
			Vector3f player_pos = player.getPosition();
			player_pos.sub(ent.getPosition());
			ent.activate();
			//ent.applyImpulse(player_pos, new Vector3f(0,0,0));
			towards_pos.normalize(player_pos);
			towards_pos.scale(0.005f);
			
			ent.moveActor(towards_pos);
		}
	}
}
