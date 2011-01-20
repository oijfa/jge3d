package monitoring;

public interface EntityObserver {
	//along with the updated data it passes the starter to check if the observer started the update chain
	public void update(String key, Object old_val, Object new_val);
}
