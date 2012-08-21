package engine.entity;

public interface EntityListListener {
	void entityAdded(Entity ent);
	void entityRemoved(Entity ent);
	void entityModelChanged(Entity ent);
}
