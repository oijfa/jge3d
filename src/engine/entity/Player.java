package engine.entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;

public class Player extends Entity {
	private KinematicCharacterController player;

	public Player(String name, float mass, float step_height) {
		super(name, mass, false);
		initPlayer(step_height);
	}

	public Player(float mass, float step_height) {
		super(mass, false);
		initPlayer(step_height);
	}

	public void initPlayer(float step_height) {
		player = new KinematicCharacterController(
			(PairCachingGhostObject) ((GhostObject) collision_object),
			(ConvexShape) collision_object.getCollisionShape(), step_height
		);
		player.setJumpSpeed(1.0f);
	}

	public void movePlayer(Vector3f walk_direction) {
		player.setWalkDirection(walk_direction);
	}

	public void setJumpSpeed(float jump_speed) {
		player.setJumpSpeed(jump_speed);
	}

	public void jump() {
		if (player.canJump()) player.jump();
	}

}
