package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.ResizableFrame;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;

public class LayerMenu extends ResizableFrame implements ActionListener {
	private ComboBox<Integer> layer_cb;
	private ArrayList<ActionListener> action_listeners;

	public LayerMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Layer Menu");

		layer_cb = new ComboBox<Integer>();
		layer_cb.setTheme("layer_cb");
		layer_cb.addActionListener(this);

		add(layer_cb);
	}

	public void populateLayers(Integer num_layers) {
		layer_cb.removeAllItems();
		for (int i = 0; i < num_layers; i++) {
			layer_cb.addItem(i);
		}
	}

	public Integer getSelection() {
		return layer_cb.getSelected();
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent() {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent();
	}
}
