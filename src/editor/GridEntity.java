package editor;

import com.bulletphysics.collision.shapes.CollisionShape;

import engine.entity.Entity;

public class GridEntity extends Entity {

	public GridEntity(float mass, CollisionShape shape, boolean collide) {
		super("grid", mass, shape, collide);
	}
}
