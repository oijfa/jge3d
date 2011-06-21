package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.ResizableFrame;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;

public class PerspectiveMenu extends ResizableFrame implements ActionListener {
	private ComboBox<Integer> perspective_cb;
	private ArrayList<ActionListener> action_listeners;

	public PerspectiveMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Perspective Menu");

		perspective_cb = new ComboBox<Integer>();
		perspective_cb.setTheme("perspective_cb");
		perspective_cb.addActionListener(this);

		add(perspective_cb);
	}

	public void populateLayers(Integer num_layers) {
		perspective_cb.addItem("XY");
		perspective_cb.addItem("XZ");
		perspective_cb.addItem("YZ");
	}

	public Integer getSelection() {
		return perspective_cb.getSelected();
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
