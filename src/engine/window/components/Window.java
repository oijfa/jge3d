package engine.window.components;

import de.matthiasmann.twl.ResizableFrame;

public class Window extends ResizableFrame {
	String name;
	
	public Window() {
		super();
		name = new String();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
