package editor.window;

import java.util.ArrayList;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;
import engine.window.components.Window;

public class MirrorMenu extends Window implements ActionListener {
	private ComboBox<Integer> mirror_cb;
	private ArrayList<ActionListener> action_listeners;

	public MirrorMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Axis Mirror Menu");

		mirror_cb = new ComboBox<Integer>();
		mirror_cb.setTheme("perspective_cb");
		mirror_cb.addActionListener(this);

		add(mirror_cb);
	}

	public void populateLayers(Integer num_layers) {
		mirror_cb.addItem("X");
		mirror_cb.addItem("Y");
		mirror_cb.addItem("Z");
		mirror_cb.addItem("XY");
		mirror_cb.addItem("XZ");
		mirror_cb.addItem("YZ");
		mirror_cb.addItem("XYZ");
	}

	public Integer getSelection() {
		return mirror_cb.getSelected();
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
