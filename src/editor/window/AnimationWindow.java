package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;
import engine.window.components.Window;

public class AnimationWindow extends Window implements ActionListener {
	private ComboBox<Integer> frame_cb;
	private ComboBox<Integer> animation_cb;
	private Button prev_button;
	private Button next_button;
	private Button add_button;
	private Button remove_button;
	private DialogLayout layout;
	private ArrayList<ActionListener> action_listeners;

	public AnimationWindow() {
		action_listeners = new ArrayList<ActionListener>();
		this.setTheme("animationwindow");
		setTitle("Animation Menu");

		frame_cb = new ComboBox<Integer>();
		frame_cb.setTheme("frame_cb");
		frame_cb.addActionListener(this);
		
		animation_cb = new ComboBox<Integer>();
		animation_cb.setTheme("animation_cb");
		animation_cb.addActionListener(this);

		prev_button = new Button();
		prev_button.setTheme("prev_button");
		next_button = new Button();
		next_button.setTheme("next_button");
		add_button = new Button();
		add_button.setTheme("add_button");
		remove_button = new Button();
		remove_button.setTheme("remove_button");
		
		layout = new DialogLayout();
		
		// Group for holding the Horizontal alignment of the buttons
		Group h_grid = layout.createParallelGroup();
		// Group for holding the Vertical alignment of the buttons
		Group v_grid = layout.createSequentialGroup();
		// Generic row up buttons
		Group row;

		// Create the horizontal rows
		row = layout.createSequentialGroup(
			animation_cb,
			add_button,
			remove_button
		);
		h_grid.addGroup(row);
		// Reset temp row for vertical
		row = layout.createSequentialGroup(
			prev_button,
			frame_cb,
			next_button
		);
		h_grid.addGroup(row);
		
		// Create the horizontal rows
		row = layout.createParallelGroup(
			animation_cb,
			add_button,
			remove_button
		);
		v_grid.addGroup(row);
		// Reset temp row for vertical
		row = layout.createParallelGroup(
			prev_button,
			frame_cb,
			next_button
		);
		v_grid.addGroup(row);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(h_grid);
		layout.setVerticalGroup(v_grid);
		
		this.add(layout);
	}
	
	public AnimationWindow(Integer num_frames) {
		super();
		populateFrames(num_frames);
	}

	public void populateFrames(Integer num_frames) {
		frame_cb.removeAllItems();
		for (int i = 0; i < num_frames; i++) {
			frame_cb.addItem(i);
		}
		frame_cb.setSelected(0);
	}

	public Integer getFrameSelection() {
		if(frame_cb.getNumChildren() <= 0)
			return 0;
		else
			return frame_cb.getSelected();
	}
	
	public void getFrameSelection(int selected) {
		if(	selected + 1 <= frame_cb.getModel().getNumEntries() &&
			selected - 1 >= -1 )
		{
			frame_cb.setSelected(selected);
		} else { 
			System.out.println(
				"Invalid frame number:" + selected + 
				" ;Currently on:" + frame_cb.getSelected() +
				" ;TotalChildren:" + frame_cb.getModel().getNumEntries()
			);
		}
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
