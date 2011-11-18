package engine.render;

import com.bulletphysics.collision.dispatch.CollisionObject;

public interface RenderObject {
	public void draw(CollisionObject collision_object);	
}
