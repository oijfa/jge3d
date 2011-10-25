package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ToggleButton;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.WindowManager;
import engine.window.components.Window;

public class ToolMenu  extends Window implements ActionListener {
	private ArrayList<ActionListener> action_listeners;

	private ToggleButton file_window;
	private ToggleButton palette_window;
	private ToggleButton grid_window;
	private ToggleButton perspective_window;
	private ToggleButton layer_window;
	private ToggleButton mirror_window;
	private ToggleButton color_window;
	
	private WindowManager window_manager;

	public ToolMenu(WindowManager windows) {	
		this.window_manager = windows;
		
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Tool Menu");

		createMenu();
		createButtonCallbacks();		
		
		setSize(300, 150);
	}
	
	public void createMenu() {
		// Create the layout and button instances
		DialogLayout layout = new DialogLayout();

		file_window = new ToggleButton("File");
		file_window.setTheme("file_window");
		palette_window = new ToggleButton("Colors");
		palette_window.setTheme("palette_window");
		grid_window = new ToggleButton("DrawSpace");
		grid_window.setTheme("grid_window");
		perspective_window = new ToggleButton("Perspective");
		perspective_window.setTheme("perspective_menu");
		layer_window = new ToggleButton("Layer");
		layer_window.setTheme("layer_menu");
		mirror_window = new ToggleButton("Mirror");
		mirror_window.setTheme("mirror_menu");
		color_window = new ToggleButton("Color");
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
			public void run() { 
				if(file_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("file_menu").setVisible(true);
				else
					 window_manager.getWindows().getByName("file_menu").setVisible(false);
				 window_manager.setPosition(window_manager.getWindows().getByName("file_menu"));
			}
		});
		palette_window.addCallback(new Runnable() {			
			@Override
			public void run() {
				if(palette_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("palette_window").setVisible(true);
				else
					 window_manager.getWindows().getByName("palette_window").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("palette_window"));
			}
		});
		grid_window.addCallback(new Runnable() {			
			@Override
			public void run() {  
				if(grid_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("grid_window").setVisible(true);
				else
					 window_manager.getWindows().getByName("grid_window").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("grid_window"));
			}
		});
		perspective_window.addCallback(new Runnable() {			
			@Override
			public void run() {
				if(perspective_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("perspective_menu").setVisible(true);
				else
					 window_manager.getWindows().getByName("perspective_menu").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("perspective_menu"));
			}
		});
		layer_window.addCallback(new Runnable() {			
			@Override
			public void run() {  
				if(layer_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("layer_menu").setVisible(true);
				else
					 window_manager.getWindows().getByName("layer_menu").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("layer_menu"));
			}
		});
		mirror_window.addCallback(new Runnable() {			
			@Override
			public void run() { 
				if(mirror_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("mirror_menu").setVisible(true);
				else
					 window_manager.getWindows().getByName("mirror_menu").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("mirror_menu"));
			}
		});
		color_window.addCallback(new Runnable() {			
			@Override
			public void run() {  
				if(color_window.getModel().isSelected()) 
					 window_manager.getWindows().getByName("color_window").setVisible(true);
				else
					 window_manager.getWindows().getByName("color_window").setVisible(false);
				window_manager.setPosition(window_manager.getWindows().getByName("color_window"));
			}
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
