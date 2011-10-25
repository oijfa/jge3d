package engine.window;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import engine.window.components.Window;
import engine.window.components.WindowList;
import engine.window.tree.Model;

import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import engine.input.InputMap;
import engine.input.components.KeyMapException;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class WindowManager extends DesktopArea {
	private LWJGLRenderer renderer;
	private GUI gui;
	private ThemeManager theme;

	private WindowList windows;
	private InputMap key_map;

	private Integer layers;

	public WindowManager() {
		super();
		windowInit(null);
	}

	public WindowManager(Model m) {
		super();
		windowInit(m);
	}

	public void windowInit(Model m) {
		try {
			renderer = new LWJGLRenderer();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		gui = new GUI(this, renderer);
		try {
			theme = ThemeManager.createThemeManager(this.getClass()
				.getClassLoader().getResource("resources/themes/default.xml"),
				renderer);
			gui.applyTheme(theme);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// you have to do a gui update or it won't give you the sizes of the
		// subwindows
		gui.update();

		windows = new WindowList();
	}

	public void draw() {
		gui.update();
	}

	public void destroy() {
		gui.destroy();
		theme.destroy();
	}

	protected boolean handleEvent(Event evt) {
		// Our event handling
		//gui.handleKeyRepeat();
		try {
			if (key_map != null && !evt.isKeyRepeated() && key_map.handleEvent(evt)) { return true; }
		} catch (KeyMapException e) {
			// TODO 
			System.out.println("Failed to handle event for whatever reason.");
			e.printStackTrace();
		}

		return false;
	}

	public void addWindow(Window window, int width, int height) {
		window.setSize(width, height);

		gui.update();

		setPosition(window);
		
		windows.add(window);
		Widget current_window = windows.get(windows.indexOf(window));
		add(current_window);
	}

	public void setPosition(Window window) {
		if (windows.size() > 0) {
			Window last_window = windows.get(0);
			
			for(Window test_window: windows) {
				if(test_window.isVisible() && test_window.getX() >= last_window.getX() && test_window.getY() > last_window.getY())
					last_window = test_window;
			}
			
			if (window.getHeight() + last_window.getBottom() < this.getHeight()) {
				window.setPosition(0, last_window.getBottom());
			} else if (window.getHeight() + last_window.getBottom() > this
				.getHeight() && last_window.getX() == 0) {
				window.setPosition(last_window.getRight(), 0);
			} else {
				window.setPosition(last_window.getRight(),
					last_window.getBottom());
			}
			// System.out.println("NewPos:"+window.getX()+":"+window.getY()+" ###last:"+last_window.getWidth()+":"+last_window.getHeight()+"###");
		}
	}
	
	public Integer getNumLayers() {
		return layers;
	}

	public void setKeyMap(InputMap i) {
		key_map = i;
	}
	
	public WindowList getWindows() {
		return windows;
	}
}
