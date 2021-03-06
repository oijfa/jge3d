package editor;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import editor.Main;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import editor.window.AnimationWindow;
import editor.window.EntityListMenu;
import editor.window.ProjectWindow;
import editor.window.GridWindow;
import editor.window.LayerMenu;
import editor.window.MirrorMenu;
import editor.window.PaletteWindow;
import editor.window.PerspectiveMenu;
import editor.window.ResourceWindow;
import editor.window.ToolMenu;
//import editor.window.ToolBox;
import engine.Engine;
import engine.entity.*;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Texture;
import engine.render.ubos.Light;
import engine.render.ubos.Lights;
import engine.render.ubos.Material;

public class Main implements ActionListener {
	// A filthy hack to get around the combobox sending to events on select
	private Boolean combobox_hack = true;

	private Engine engine;

	private GridWindow grid_window;
	private PaletteWindow palette_window;
	private LayerMenu layer_menu;
	private AnimationWindow animation_window;
	private ProjectWindow project_window;
	private ToolMenu tool_menu;
	private MirrorMenu mirror_menu;
	private PerspectiveMenu perspective_menu;
	private ResourceWindow resource_window;
	private EntityListMenu entity_list;
	// private ToolBox tool_box;

	private Entity edit_model;
	private Camera camera;
	
	private boolean multiThreaded = true;

	public static void main(String args[]) {
		Main m = new Main();
		m.run();
	}

	public Main() {
		engine = new Engine();

		int num_layers = 12;
		grid_window = new GridWindow(num_layers, (engine.render.Model)engine.resource_manager.getResource("box", "models"));

		layer_menu = new LayerMenu();
		layer_menu.populateLayers(num_layers);
		animation_window = new AnimationWindow();
		palette_window = new PaletteWindow(216);
		project_window = new ProjectWindow();
		perspective_menu = new PerspectiveMenu();
		mirror_menu = new MirrorMenu();
		resource_window = new ResourceWindow(engine);
		entity_list = new EntityListMenu(engine);
		
		tool_menu = new ToolMenu(engine.getWindowManager());
		// tool_box = new ToolBox();

		engine.addWindow(tool_menu, 250, 400);
		tool_menu.setName("tool_menu");
		engine.addWindow(grid_window, 400, 400);
		grid_window.setName("grid_window");
		engine.addWindow(palette_window, 300, 300);
		palette_window.setName("palette_window");
		engine.addWindow(animation_window, 300, 300);
		animation_window.setName("animation_window");
		engine.addWindow(layer_menu, 200, 30);
		layer_menu.setName("layer_menu");
		engine.addWindow(project_window, 300, 100);
		project_window.setName("project_window");
		engine.addWindow(mirror_menu, 200, 30);
		mirror_menu.setName("mirror_menu");
		engine.addWindow(perspective_menu, 200, 30);
		perspective_menu.setName("perspective_menu");
		engine.addWindow(resource_window, 200, 400);
		resource_window.setName("resource_window");
		engine.addWindow(entity_list, 200, 400);
		entity_list.setName("entity_list");
				
		//engine.addWindow(new FileMenu(), 300, 300;);
		// engine.addWindow(tool_box, 200, 300);
		
		
		edit_model = engine.addEntity("edit_model", 0f, true, "tri_cube", "transparent_textured");
		((Model)edit_model.getProperty(Entity.MODEL)).setTexture(
			(Texture) engine.resource_manager.getResource("test3","textures")
		);
		//((Model)edit_model.getProperty(Entity.MODEL)).setWireFrame(true);
		edit_model.setProperty(Entity.NAME, "edit_model");
		edit_model.setProperty(Entity.POSITION, new Vector3f(0,0,0));
		edit_model.setProperty("gravity",new Vector3f(0,0,0));

		camera = engine.addCamera(1f, false, "box2");
		camera.setDistance(40.0f);
		camera.focusOn(edit_model);
		
		resource_window.setEngine(engine);
		
		engine.getWindowManager().getWindows().hideAll();
		tool_menu.setVisible(true);
		
		//addUBOsToShader("default");
		addUBOsToTexturedShader("transparent_textured");			
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
	
	//This function is just for testing; we'll need to set this stuff at the map level
	public void addUBOsToShader(String shader_name) {
		Lights lights;
		Light light;
		Light light2;
				
		lights = new Lights();
		
		Shader shader = (Shader)engine.resource_manager.getResource(shader_name, "shaders");
		light = new Light(
			new Vector4f(0.0f,2.0f,10.0f,1.0f),
			new Vector4f(10.0f,0.0f,0.0f,255.0f),
			new Vector4f(10.0f,0.0f,0.0f,255.0f),
			new Vector4f(10.0f,0.0f,0.0f,255.0f),
			0.1f,
			1.0f,
			0.1f,
			new Vector3f(0.0f,-1.0f,0.0f),
			1.0f,
			1.0f
		);
        light2 = new Light(
			new Vector4f(0.0f,2.0f,-10.0f,1.0f),
			new Vector4f(0.0f,0.0f,10.0f,255.0f),
			new Vector4f(0.0f,0.0f,10.0f,255.0f),
			new Vector4f(0.0f,0.0f,10.0f,255.0f),
			0.1f,
			1.0f,
			0.1f,
			new Vector3f(0.0f,-1.0f,0.0f),
			1.0f,
			1.0f
		);
        lights.add(light);
        lights.add(light2);
        
        shader.addUBO(lights);
        
    	shader.addUBO(camera.getMVPmatrix());
    	
    	shader.addUBO(new Material());
	}
	
	public void addUBOsToTexturedShader(String shader_name) {
		Shader shader = (Shader)engine.resource_manager.getResource(shader_name, "shaders");
    	shader.addUBO(camera.getMVPmatrix());
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == palette_window) {
			grid_window.setCurrentColor(((PaletteWindow) ae.getSource()).getPrimaryColor());
		} else if (ae.getSource() == grid_window) {
			if(ae.getAction() == "mouseup") {
				layer_menu.setSelection(layer_menu.getSelection()+1);
				grid_window.loadLayer(layer_menu.getSelection());
			}
			else if(ae.getAction() == "mousedown") {
				layer_menu.setSelection(layer_menu.getSelection()-1);
				grid_window.loadLayer(layer_menu.getSelection());
			}
			else {
				//grid_window.loadLayer(layer_menu.getSelection());
				edit_model.setProperty("model",grid_window.getGrid().getModel());
			}
		} else if (ae.getSource() == layer_menu) {
			if(ae.getAction() == "combobox") {
				if (combobox_hack == true) {
					grid_window.loadLayer(layer_menu.getSelection());
					combobox_hack = false;
				} else if (combobox_hack == false) {
					combobox_hack = true;
				}
			} else {
				if(ae.getAction() == "increase") {
					grid_window.setGridSize(grid_window.getGrid().getSize()+1);
					grid_window.loadLayer(layer_menu.getSelection());
				}
				else if(ae.getAction() == "decrease") {
					grid_window.setGridSize(grid_window.getGrid().getSize()-1);
					grid_window.loadLayer(layer_menu.getSelection());
				}
			}
		}
	}
}
