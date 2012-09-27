package engine.entity;

public interface EntityListener {
	public void entityPropertyChanged(String property, Entity entity, Object old_value);
}
