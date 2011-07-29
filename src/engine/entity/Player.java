package engine.entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;

import engine.render.Model;
import engine.render.Shader;

public class Player extends Entity {
	private KinematicCharacterController player;

	public Player(String name, float mass, float step_height, Model model, Shader shader) {
		super(name, mass, false, model, shader);
		initPlayer(step_height);
	}

	public Player(float mass, float step_height, Model model, Shader shader) {
		super(mass, false, model, shader);
		initPlayer(step_height);
	}

	public void initPlayer(float step_height) {
		player = new KinematicCharacterController(
			(PairCachingGhostObject) ((GhostObject) collision_object),
			(ConvexShape) collision_object.getCollisionShape(), step_height
		);
		player.setJumpSpeed(10.0f);
		collision_object.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		object_type = ObjectType.actor;
	}

	public void movePlayer(Vector3f walk_direction) {
		player.setWalkDirection(walk_direction);
	}

	public void setFallSpeed(float fall_speed) {
		player.setFallSpeed(fall_speed);
	}
	
	public void setGravity(float gravity) {
		player.setGravity(gravity);
	}
	
	public void setJumpSpeed(float jump_speed) {
		player.setJumpSpeed(jump_speed);
	}

	public void jump() {
		if (player.canJump()) player.jump();
	}
	
	public KinematicCharacterController getActor() {
		return player;
	}
}
