package engine.physics;

import javax.vecmath.Vector3f;

import engine.entity.Entity;
import engine.entity.EntityList;
import engine.entity.EntityListListener;
import engine.entity.EntityListener;

public abstract class PhysicsInterface implements EntityListListener, EntityListener {
	public abstract void clientUpdate();

	public abstract void handleGhostCollisions(EntityList entity_list);

	public abstract Entity pickEntityWithRay(Vector3f position, Vector3f ray_to, EntityList entity_list);

	public abstract void parsePhysicsQueue();
}
