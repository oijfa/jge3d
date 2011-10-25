package game.gui.controlbox;

import de.matthiasmann.twl.ResizableFrame;
import engine.window.WindowManager;
import engine.window.components.ComboBox;

public class LayerMenu extends ResizableFrame {
	ComboBox<String> layer_cb;
	WindowManager parent;

	public LayerMenu(WindowManager parent) {

		layer_cb = new ComboBox<String>();
	}

	public void populateLayers() {
		for (int i = 1; i <= parent.getNumLayers(); i++) {
			layer_cb.addItem(String.valueOf(i));
		}
	}
}
