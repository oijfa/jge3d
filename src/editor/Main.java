package editor;
import javax.vecmath.Vector3f;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import editor.window.GridWindow;
import editor.window.LayerMenu;
import editor.window.PaletteMenu;
import engine.controller.*;
import engine.entity.*;
import engine.importing.FileLoader;

import com.bulletphysics.collision.shapes.BoxShape;

public class Main extends Controller implements ActionListener {
	private static final long serialVersionUID = 1L;

	private GridWindow grid_window;
	private PaletteMenu palette_menu;
	private LayerMenu layer_menu;
	
	private Entity model;
	
	@Override
	public void initialize() {
		//Testing the terrain stuff here
		//Terrain terrain = new Terrain(objectList);
		//terrain.createTerrain(75);
		
		Player player1 = new Player(1.0f, new BoxShape(new Vector3f(1, 1, 1)),0.5f);
		player1.setPosition(new Vector3f(0,100,0));
		player1.setProperty("name", "player1");
		objectList.enqueuePhysics(player1, QueueItem.ADD);
		objectList.parsePhysicsQueue();
		
		int num_layers = 8;
		grid_window = new GridWindow(num_layers);
		layer_menu = new LayerMenu(); layer_menu.populateLayers(num_layers);
		palette_menu = new PaletteMenu(216);
		
		grid_window.addActionListener(this);
		palette_menu.addActionListener(this);
		layer_menu.addActionListener(this);
		
		renderer.getWindow().addWindow(grid_window,200,200);
		renderer.getWindow().addWindow(palette_menu,300,300);
		renderer.getWindow().addWindow(layer_menu,200,30);
		
		model = new Entity(1,new BoxShape(new Vector3f(1,1,1)),true);
		model.setModel(FileLoader.loadFile("resources/models/misc/box2.xgl"));
		objectList.enqueuePhysics(model,QueueItem.ADD);
		objectList.enqueueRenderer(model, QueueItem.ADD);
		
		createCamera();
		((Camera)objectList.getItem(Camera.CAMERA_NAME)).focusOn(model);
		((Camera)objectList.getItem(Camera.CAMERA_NAME)).setDistance(20f);
	}
	private Boolean i=true;
	@Override
  	public void actionPerformed(ActionEvent ae) {
		if( ae.getSource() == palette_menu){
			grid_window.setCurrentColor(((PaletteMenu) ae.getSource()).getPrimaryColor());
		}
		if(ae.getSource() == grid_window) {
			model.setModel(grid_window.getGrid().getModel("resources/models/misc/box.xgl"));
		}
		if(ae.getSource() == layer_menu) {
			if(i==true) {
				grid_window.loadLayer(layer_menu.getSelection());
				i=false;
			}
			else if(i==false)
				i=true;
		}
  }
}
