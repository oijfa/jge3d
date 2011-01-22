package window;

import java.io.File;
import java.io.IOException;
import org.lwjgl.LWJGLException;

import window.tree.Model;

//import window.MainMenu;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import input.Input;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import entity.Camera;
import entity.EntityList;

public class Window extends DesktopArea {
	//private MainMenu mainMenu;
	//private TextureMenu textureMenu;
	private RotationMenu rotationMenu;
	private EntityMenu entityMenu;
	private LWJGLRenderer renderer;
	private GUI gui;
	private ThemeManager theme;
	private Input input;
	
	public Window(EntityList objectList) {
		super();
		windowInit(objectList, null);
	}
	
	public Window(EntityList objectList, Model m) {
		super();
		windowInit(objectList,m);
	}
	
	public void windowInit(EntityList objectList, Model m){
		try {
			renderer = new LWJGLRenderer();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		gui = new GUI(this, renderer);
		try {
			theme = ThemeManager.createThemeManager(
					(new File("resources/themes/default.xml")).toURI().toURL(),
					renderer);
			gui.applyTheme(theme);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		// Create the main menu
		mainMenu = new MainMenu();
		add(mainMenu);
		mainMenu.setTheme("mainmenu");
		mainMenu.setPosition(this.getWidth()/2-mainMenu.getWidth()/2, this.getHeight()/2-mainMenu.getHeight()/2);
		
		textureMenu = new TextureMenu();
		//add(textureMenu);
		textureMenu.setTheme("texturemenu");
		textureMenu.setPosition(this.getWidth()-textureMenu.getWidth(),0);
		*/

		input = new Input(objectList,this);
		entityMenu = new EntityMenu(m);
		add(entityMenu);
		entityMenu.setTheme("entitymenu");

		
		rotationMenu = new RotationMenu();
		add(rotationMenu);
		rotationMenu.setTheme("rotationmenu");
		

		//you have to do a gui update or it won't give you the sizes of the subwindows
		gui.update();
		
		entityMenu.setPosition(this.getWidth()-entityMenu.getWidth(),0);
		rotationMenu.setPosition(this.getWidth()-rotationMenu.getWidth(), this.getHeight()-rotationMenu.getHeight());
	}

	public void draw() {
		gui.update();
		//input.run();
	}

	public void destroy() {
		gui.destroy();
		theme.destroy();
	}

	public void setCamera(Camera cam) {
		rotationMenu.setCameraRef(cam);
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
}
