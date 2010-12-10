package window;

import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLException;

import window.MainMenu;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Window extends DesktopArea {
	private MainMenu mainMenu;
	private TextureMenu textureMenu;
	private EntityMenu entityMenu;
	private LWJGLRenderer renderer;
	private GUI gui;
	private ThemeManager theme;

	public Window() {
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
		
		// Create the main menu
		mainMenu = new MainMenu();
		add(mainMenu);

		textureMenu = new TextureMenu();
		add(textureMenu);
		
		entityMenu = new EntityMenu();
		add(entityMenu);
		
		//you have to do a gui update or it won't give you the sizes of the subwindows
		gui.update();
		
		mainMenu.setPosition(this.getWidth()/2-mainMenu.getWidth()/2, this.getHeight()/2-mainMenu.getHeight()/2);
		textureMenu.setPosition(this.getWidth()-textureMenu.getWidth(),0);
		entityMenu.setPosition(this.getWidth()-entityMenu.getWidth(),textureMenu.getHeight());
	}
	
	public void draw() {
		gui.update();
	}

	public void destroy() {
		gui.destroy();
		theme.destroy();
	}
}
