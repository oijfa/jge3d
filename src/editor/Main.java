package editor;
import javax.vecmath.Vector3f;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import editor.window.GridWindow;
import editor.window.LayerMenu;
import editor.window.PaletteMenu;
import engine.Engine;
import engine.entity.*;

import com.bulletphysics.collision.shapes.BoxShape;

public class Main implements ActionListener {
	private static final long serialVersionUID = 1L;

	Engine engine;
	
	private GridWindow grid_window;
	private PaletteMenu palette_menu;
	private LayerMenu layer_menu;
	
	private Entity model;
	private Camera camera;
	
	public static void main(String args[]){
	  Main m = new Main();
	  m.run();
	}
	
	public Main(){
    engine = new Engine();
  
    int num_layers = 8;
    grid_window = new GridWindow(num_layers);
    layer_menu = new LayerMenu(); layer_menu.populateLayers(num_layers);
    palette_menu = new PaletteMenu(216);
    
    engine.addWindow(grid_window,400,400);
    engine.addWindow(palette_menu,300,300);
    engine.addWindow(layer_menu,200,30);
    
    
    
    model = new Entity(1,new BoxShape(new Vector3f(1,1,1)),true);
    model.setModel(grid_window.getGrid().getModel("resources/models/misc/box.xgl"));
    model.setProperty(Entity.NAME, "model");
    model.setPosition(new Vector3f(0,0,0));
    
    camera = new Camera(10d, new BoxShape(new Vector3f(1,1,1)), false, model);
    camera.setProperty(Entity.NAME, "camera");
    camera.setPosition(new Vector3f(0,0,0));
    
    engine.addEntity(model);
	}
	
	public void run(){
	  grid_window.addActionListener(this);
    palette_menu.addActionListener(this);
    layer_menu.addActionListener(this);
    
     engine.run();
	}
	/*
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
		
	}
  */
	
	private Boolean i=true;
	@Override
	public void actionPerformed(ActionEvent ae) {
  	if( ae.getSource() == palette_menu){
  		grid_window.setCurrentColor(((PaletteMenu) ae.getSource()).getPrimaryColor());
  	}else	if(ae.getSource() == grid_window) {
  		model.setModel(grid_window.getGrid().getModel("resources/models/misc/box.xgl"));
  	}else if(ae.getSource() == layer_menu) {
  		if(i==true) {
  			grid_window.loadLayer(layer_menu.getSelection());
  			i=false;
  		}else if(i==false){
  			i=true;
  		}
  	}
  }
}
