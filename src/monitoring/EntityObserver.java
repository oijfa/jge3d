package monitoring;

public interface EntityObserver {
	public void update();
	public void update(String key, String old_val, String new_val);
}
