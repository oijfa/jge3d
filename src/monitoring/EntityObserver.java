package monitoring;

public interface EntityObserver {
	public void update(String key, Object old_val, Object new_val);
}
