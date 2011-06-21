package engine.monitoring;

public interface EntityListObserver {
	// just passes the starter to tell the observer if it started the update
	// chain or not.
	public void update();
}
