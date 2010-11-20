package window;

import window.MainMenu;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;


public class Window extends DesktopArea{
	private final FPSCounter fpsCounter;
    private final Label mouseCoords;
    private MainMenu mainMenu;
    private GUI gui;
	
	public static void main(String[] args){
		Window win = new Window();
	}
	
	public Window(){
		super();
		fpsCounter = new FPSCounter();
        mouseCoords = new Label();

        add(fpsCounter);
        add(mouseCoords);
        
        mainMenu = new MainMenu();
        add(mainMenu);
        
        mainMenu.setSize(400, 200);
        mainMenu.setPosition(0, 0);
	}
}
