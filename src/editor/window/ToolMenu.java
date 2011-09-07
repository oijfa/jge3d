package editor.window;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.LoadFileSelector;

public class ToolMenu  extends ResizableFrame implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	
	private LoadFileSelector load_file;
	
	private Button file_window;
	private Button palette_window;
	private Button grid_window;
	private Button perspective_window;
	private Button layer_window;
	private Button mirror_window;
	private Button color_window;
	
	LoadFileSelector.Callback callback = new LoadFileSelector.Callback() {
		@Override
		public void fileSelected(File file) {/* TODO Auto-generated method stub*/}
		@Override
		public void canceled() {/* TODO Auto-generated method stub*/}
	};
	

	public ToolMenu() {

				
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Tool Menu");

		createMenu();
		createButtonCallbacks();		
		
		setSize(300, 150);
	}
	
	public void createMenu() {
		// Create the layout and button instances
		DialogLayout layout = new DialogLayout();

		file_window = new Button("File");
		file_window.setTheme("file_window");
		palette_window = new Button("Colors");
		palette_window.setTheme("palette_window");
		grid_window = new Button("DrawSpace");
		grid_window.setTheme("grid_window");
		perspective_window = new Button("Perspective");
		perspective_window.setTheme("perspective_window");
		layer_window = new Button("Layer");
		layer_window.setTheme("file_window");
		mirror_window = new Button("Mirror");
		mirror_window.setTheme("mirror_window");
		color_window = new Button("Color");
		color_window.setTheme("color_window");
		
		file_window.setSize(200, 30);
		palette_window.setSize(200, 30);
		grid_window.setSize(200, 30);
		perspective_window.setSize(200, 30);
		layer_window.setSize(200, 30);
		mirror_window.setSize(200, 30);
		color_window.setSize(200, 30);
		
		// !!!EXAMPLE OF DIALOG LAYOUT!!!//
		// Sequential groups are like a Swing boxlayout and just lists from top
		// to bottom
		// Parallel groups align each start and size and can be cascaded
		//
		// Group for holding the Horizontal alignment of the buttons
		Group button_hgroup = layout
			.createSequentialGroup()
			.addGap()
			// Keeps all the buttons in a single vertical line as opposed to
			// staggering
			// left to right per row
			.addGroup(layout.createParallelGroup(
				file_window,
				palette_window,
				grid_window,
				perspective_window,
				layer_window,
				mirror_window,
				color_window)
			)
			.addGap();

		// Group for holding the Vertical alignment of the buttons
		Group button_vgroup = layout.createSequentialGroup()
			.addGap()
			// Add each widget without forming a group so that they rest one
			// under the other
			.addWidget(file_window)
			.addWidget(palette_window)
			.addWidget(grid_window)
			.addWidget(perspective_window)
			.addWidget(layer_window)
			.addWidget(mirror_window)
			.addWidget(color_window);

		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(button_hgroup);
		layout.setVerticalGroup(button_vgroup);

		// Make sure to add the layout to the frame
		add(layout);
	}
	
	private void createButtonCallbacks() {
		file_window.addCallback(new Runnable() {			
			@Override
			public void run() { /* create a new file somehow? */}
		});
		palette_window.addCallback(new Runnable() {			
			@Override
			public void run() { load_file.openPopup(); }
		});
		grid_window.addCallback(new Runnable() {			
			@Override
			public void run() { /* save_file.openPopup */}
		});
		perspective_window.addCallback(new Runnable() {			
			@Override
			public void run() { /* create a new file somehow? */}
		});
		layer_window.addCallback(new Runnable() {			
			@Override
			public void run() { load_file.openPopup(); }
		});
		mirror_window.addCallback(new Runnable() {			
			@Override
			public void run() { /* save_file.openPopup */}
		});
		color_window.addCallback(new Runnable() {			
			@Override
			public void run() { /* save_file.openPopup */}
		});
	}
	
	/*Public Callback methods*/
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
	/*End Public Callback methods*/
}
