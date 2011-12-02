package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;
import engine.window.components.Window;

public class LayerMenu extends Window implements ActionListener {
	private ComboBox<Integer> layer_cb;
	private ArrayList<ActionListener> action_listeners;
	private Button decrease_button;
	private Button increase_button;
	private DialogLayout layout;

	public LayerMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Layer Menu");

		layer_cb = new ComboBox<Integer>();
		layer_cb.setTheme("layer_cb");
		layer_cb.addActionListener(this);
		
		decrease_button = new Button();
		decrease_button.setTheme("decrease_button");
		increase_button = new Button();
		increase_button.setTheme("increase_button");
		
		decrease_button.addCallback(new Runnable() {
			
			@Override
			public void run() {
				fireActionEvent("decrease");
			}
		});
		increase_button.addCallback(new Runnable() {
			
			@Override
			public void run() {
				fireActionEvent("increase");
			}
		});
		
		layout = new DialogLayout();
		
		// Group for holding the Horizontal alignment of the buttons
		Group h_grid = layout.createSequentialGroup();
		// Group for holding the Vertical alignment of the buttons
		Group v_grid = layout.createParallelGroup();
		// Generic row up buttons
		Group row;

		// Reset temp row for vertical
		row = layout.createSequentialGroup(
			decrease_button,
			layer_cb,
			increase_button
		);
		h_grid.addGroup(row);
		row = layout.createParallelGroup(
			decrease_button,
			layer_cb,
			increase_button
		);
		v_grid.addGroup(row);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(h_grid);
		layout.setVerticalGroup(v_grid);
		
		this.add(layout);
	}

	public void populateLayers(Integer num_layers) {
		layer_cb.removeAllItems();
		for (int i = 0; i < num_layers; i++) {
			layer_cb.addItem(i);
		}
		layer_cb.setSelected(0);
	}

	public Integer getSelection() {
		if(layer_cb.getNumChildren() <= 0)
			return 0;
		else
			return layer_cb.getSelected();
	}
	
	public void setSelection(int selected) {
		if(	selected + 1 <= layer_cb.getModel().getNumEntries() &&
			selected - 1 >= -1 )
		{
			layer_cb.setSelected(selected);
		} else { 
			System.out.println(
				"Invalid layer number:" + selected + 
				" ;Currently on:" + layer_cb.getSelected() +
				" ;TotalChildren:" + layer_cb.getModel().getNumEntries()
			);
		}
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent(String event) {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this, event));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent("combobox");
	}
}
