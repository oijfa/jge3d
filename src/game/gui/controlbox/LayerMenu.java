package game.gui.controlbox;

import de.matthiasmann.twl.ResizableFrame;
import engine.window.Window;
import engine.window.components.ComboBox;

public class LayerMenu extends ResizableFrame {
	ComboBox<String> layer_cb;
	Window parent;
		
	public LayerMenu(Window parent) {
				
		layer_cb = new ComboBox<String>();
	}
	
	public void populateLayers() {
		for(int i=1; i <= parent.getNumLayers(); i++) {
			layer_cb.addItem(i);
		}
	}
}
