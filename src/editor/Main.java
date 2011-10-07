package editor;

import javax.vecmath.Vector3f;

import editor.Main;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import editor.window.FileMenu;
import editor.window.GridWindow;
import editor.window.LayerMenu;
import editor.window.MirrorMenu;
import editor.window.PaletteWindow;
import editor.window.PerspectiveMenu;
import editor.window.ToolMenu;
//import editor.window.ToolBox;
import engine.Engine;
import engine.entity.*;

public class Main implements ActionListener {
	// A filthy hack to get around the combobox sending to events on select
	private Boolean combobox_hack = true;

	private Engine engine;

	private GridWindow grid_window;
	private PaletteWindow palette_window;
	private LayerMenu layer_menu;
	private FileMenu file_menu;
	private ToolMenu tool_menu;
	private MirrorMenu mirror_menu;
	private PerspectiveMenu perspective_menu;
	// private ToolBox tool_box;

	private Entity edit_model;
	private Camera camera;
	
	private boolean multiThreaded = false;

	public static void main(String args[]) {
		Main m = new Main();
		m.run();
	}

	public Main() {
		engine = new Engine();

		engine.addModel("box", "resources/models/misc/box.xgl", engine.getShaderByName("default"));
		engine.addModel("box2", "resources/models/misc/box2.xgl", engine.getShaderByName("default"));
		engine.addModel("singlebox", "resources/models/misc/singlebox.xgl", engine.getShaderByName("default"));
		
		int num_layers = 8;
		grid_window = new GridWindow(num_layers, engine.getModelByName("singlebox"));
		layer_menu = new LayerMenu();
		layer_menu.populateLayers(num_layers);
		palette_window = new PaletteWindow(216);
		file_menu = new FileMenu();
		perspective_menu = new PerspectiveMenu();
		mirror_menu = new MirrorMenu();
		
		tool_menu = new ToolMenu(engine.getWindowManager());
		// tool_box = new ToolBox();

		engine.addWindow(tool_menu, 250, 400);
		tool_menu.setName("tool_menu");
		engine.addWindow(grid_window, 400, 400);
		grid_window.setName("grid_window");
		engine.addWindow(palette_window, 300, 300);
		palette_window.setName("palette_window");
		engine.addWindow(layer_menu, 200, 30);
		layer_menu.setName("layer_menu");
		engine.addWindow(file_menu, 300, 150);
		file_menu.setName("file_menu");
		engine.addWindow(mirror_menu, 200, 30);
		mirror_menu.setName("mirror_menu");
		engine.addWindow(perspective_menu, 200, 30);
		perspective_menu.setName("perspective_menu");
		
		//engine.addWindow(new FileMenu(), 300, 300;);
		// engine.addWindow(tool_box, 200, 300);
		
		edit_model = engine.addEntity("edit_model", 0f, true, "singlebox", "default");
		edit_model.setProperty(Entity.NAME, "edit_model");
		edit_model.setPosition(new Vector3f(0,0,0));
		//edit_model.setGravity(new Vector3f(0,0,0));

		camera = engine.addCamera(1f, false, "box2");
		camera.setDistance(40.0f);
		camera.setPosition(new Vector3f(0, 0, 6));
		camera.focusOn(edit_model);
		
		engine.getWindowManager().getWindows().hideAll();
		tool_menu.setVisible(true);
	}

	public void run() {
		grid_window.addActionListener(this);
		palette_window.addActionListener(this);
		layer_menu.addActionListener(this);
		// tool_box.addActionListener(this);
		
		if(multiThreaded == true)
			runMultiThread();
		else
			runSingleThread();

	}

	public void runMultiThread() {
		engine.run();
	}	
	
	public void runSingleThread() {
		while(true){
			engine.renderOnce();
			engine.physicsOnce();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == palette_window) {
			grid_window.setCurrentColor(((PaletteWindow) ae.getSource()).getPrimaryColor());
		} else if (ae.getSource() == grid_window) {
			edit_model.setModel(grid_window.getGrid().getModel());
			engine.updateEntity(edit_model);
		} else if (ae.getSource() == layer_menu) {
			if (combobox_hack == true) {
				grid_window.loadLayer(layer_menu.getSelection());
				combobox_hack = false;
			} else if (combobox_hack == false) {
				combobox_hack = true;
			}
		}
	}
}
