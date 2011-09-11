package engine.window.components;

import java.util.ArrayList;

public class WindowList extends ArrayList<Window> {
	private static final long serialVersionUID = 1L;
	
	public Window getByName(String name) {
		for(int i=0;i<this.size();i++) {
			if(this.get(i).getName().equals(name)) {
				return this.get(i);
			}
		}
		
		return null;
	}
	
	public void hideAll() {
		for(Window window: this) {
			window.setVisible(false);
		}
	}
}
