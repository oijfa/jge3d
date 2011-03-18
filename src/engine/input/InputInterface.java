package engine.input;

import de.matthiasmann.twl.Event;

public interface InputInterface {
	public boolean handleMouse(Event evt);
	public boolean handleKeyboard(Event evt);
}
