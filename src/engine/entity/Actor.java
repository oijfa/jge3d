package engine.entity;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;

import engine.render.Model;
import engine.render.Shader;

public class Actor extends Entity {
	private KinematicCharacterController actor;
	private Vector3f walk_direction;

	public Actor(String name, float mass, float step_height, Model model, Shader shader) {
		super(name, mass, false, model, shader);
		initActor(step_height);
	}

	public Actor(float mass, float step_height, Model model, Shader shader) {
		super(mass, false, model, shader);
		initActor(step_height);
	}

	public void initActor(float step_height) {
		actor = new KinematicCharacterController(
			(PairCachingGhostObject) ((GhostObject) collision_object),
			(ConvexShape) collision_object.getCollisionShape(), step_height
		);
		actor.setJumpSpeed(10.0f);
		collision_object.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		object_type = ObjectType.actor;
		walk_direction = new Vector3f();
	}

	public void moveActor(Vector3f walk_correction) {
		walk_direction.add(walk_correction);
		actor.setWalkDirection(walk_direction);
	}

	public void setFallSpeed(float fall_speed) {
		actor.setFallSpeed(fall_speed);
	}
	
	public void setGravity(float gravity) {
		actor.setGravity(gravity);
	}
	
	public void setJumpSpeed(float jump_speed) {
		actor.setJumpSpeed(jump_speed);
	}

	public void jump() {
		if (actor.canJump()) actor.jump();
	}
	
	public KinematicCharacterController getActor() {
		return actor;
	}
}
