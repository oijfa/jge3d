package engine.window.components;

import java.util.ArrayList;

import de.matthiasmann.twl.model.ListModel;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import editor.action_listener.ActionListener;
import editor.action_listener.ActionEvent;

public class ComboBox<E> extends de.matthiasmann.twl.ComboBox<Object> {
	private ArrayList<ActionListener> action_listeners;

	public ComboBox() {
		super();
		action_listeners = new ArrayList<ActionListener>();
		this.addCallback(new Callback(this));
	}

	public void setLabel() {

	}

	public void addItem(Object item) {
		// ListModel<Object> old_list_model = this.getModel();
		SimpleChangableListModel<Object> new_list_model = (SimpleChangableListModel<Object>) this.getModel();
		new_list_model.addElement(item);
		this.setModel(new_list_model);
	}

	public void removeItem(Object item) {
		ListModel<Object> old_list_model = this.getModel();
		SimpleChangableListModel<Object> new_list_model = new SimpleChangableListModel<Object>();
		new_list_model.addElements(old_list_model);
		for (int i = 0; i < new_list_model.getNumEntries(); i++) {
			if (new_list_model.getEntry(i).equals(item)) new_list_model
				.removeElement(i);
		}
		this.setModel(new_list_model);
	}

	public void removeAllItems() {
		ListModel<Object> old_list_model = this.getModel();
		SimpleChangableListModel<Object> new_list_model = new SimpleChangableListModel<Object>();
		new_list_model.addElements(old_list_model);
		for (int i = 0; i < new_list_model.getNumEntries(); i++) {
			new_list_model.removeElement(i);
		}
		this.setModel(new_list_model);
	}

	public void fireActionEvent() {
		for (ActionListener l : action_listeners) {
			l.actionPerformed(new ActionEvent(this));
		}
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private class Callback implements Runnable {
		ComboBox<E> owner;

		public Callback(ComboBox<E> owner) {
			this.owner = owner;
		}

		@Override
		public void run() {
			owner.fireActionEvent();
		}
	}
}
