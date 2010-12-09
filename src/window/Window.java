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
	private LWJGLRenderer renderer;
	private GUI gui;
	private ThemeManager theme;

	public Window() {
		// Create the main menu
		mainMenu = new MainMenu();
		add(mainMenu);
		mainMenu.setPosition(250, 200);
		
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
	}

	public void draw() {
		gui.update();
	}

	public void destroy() {
		gui.destroy();
		theme.destroy();
	}
}
