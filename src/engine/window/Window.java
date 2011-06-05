package engine.window;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;

import engine.window.tree.Model;

//import window.MainMenu;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import engine.input.Input;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import engine.entity.EntityList;
import game.GameInput;

public class Window extends DesktopArea {
	//private MainMenu mainMenu;
	//private TextureMenu textureMenu;
	private LWJGLRenderer renderer;
	private GUI gui;
	private ThemeManager theme;
	private Input input;
	private ArrayList<ResizableFrame> windows;
	private EntityList objectList;
	private Integer layers;
	
	public Window(EntityList objectList) {
		super();
		windowInit(objectList, null);
	}
	
	public Window(EntityList objectList, Model m) {
		super();
		windowInit(objectList,m);
	}
	
	public void windowInit(EntityList objectList, Model m){
		this.objectList = objectList;
		try {
			renderer = new LWJGLRenderer();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		gui = new GUI(this, renderer);
		try {
			theme = ThemeManager.createThemeManager(
					this.getClass().getClassLoader().getResource("resources/themes/default.xml"),
					renderer);
			gui.applyTheme(theme);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		input = new GameInput(this.objectList);
				
		//you have to do a gui update or it won't give you the sizes of the subwindows
		gui.update();
		
		windows = new ArrayList<ResizableFrame>();
	}

	public void draw() {
		gui.update();
	}

	public void destroy() {
		gui.destroy();
		theme.destroy();
	}

	protected boolean handleEvent(Event evt) { 
		//twl event handling
		//if(super.handleEvent(evt)) { 
	    //    return true; 
	    //} 
		//Our event handling
	    if(input.handleEvent(evt)) {
	        return true; 
	    }
	    
	    return false; 
	}
	
	public void addWindow(ResizableFrame window) {
		windows.add(window);
		ResizableFrame current_window = windows.get(windows.indexOf(window));
		add(current_window);
		//current_window.setTheme(window.getClass().getName().toLowerCase());
		
		//entityMenu.setPosition(this.getWidth()-entityMenu.getWidth(),0);
		//if( rotationMenu != null){
		//	rotationMenu.setPosition(this.getWidth()-rotationMenu.getWidth(), this.getHeight()-rotationMenu.getHeight());
		//}
		//mainMenu.setPosition(this.getWidth()/2-mainMenu.getWidth()/2, this.getHeight()/2-mainMenu.getHeight()/2);
		//textureMenu.setPosition(this.getWidth()-textureMenu.getWidth(),0);
	}

	public Integer getNumLayers() {
		// TODO Auto-generated method stub
		return layers;
	}
}
