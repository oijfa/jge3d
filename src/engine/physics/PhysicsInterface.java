package engine.physics;

import javax.vecmath.Vector3f;

import engine.entity.Entity;
import engine.entity.EntityList;
import engine.entity.EntityListListener;

public interface PhysicsInterface extends EntityListListener {
	public void clientUpdate();

	public void handleGhostCollisions(EntityList entity_list);

	public boolean addEntity(Entity entity);

	public void removeEntity(Entity entity);

	public Entity pickEntityWithRay(Vector3f position, Vector3f ray_to, EntityList entity_list);
}
