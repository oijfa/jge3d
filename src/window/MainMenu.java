package window;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;

public class MainMenu extends ResizableFrame {
    private final Button new_game;
    private final Button load_game;
    private final Button options;
    private final Button quit;
    
    public MainMenu() {
        setTitle("JGE3d test");

        this.new_game = new Button();
        this.load_game = new Button();
        this.options = new Button();
        this.quit = new Button();

        DialogLayout layout = new DialogLayout();
        layout.setTheme("content");
        
        layout.add(new_game);
        layout.add(load_game);
        layout.add(options);
        layout.add(quit);
        
        new_game.setSize(100, 25);
        new_game.setPosition(50, 25);
        new_game.setText("New Game");
        
        load_game.setSize(100, 25);
        load_game.setPosition(50, 75);
        load_game.setText("New Game");
        
        options.setSize(100, 25);
        options.setPosition(50, 125);
        options.setText("Options");
        
        quit.setSize(100, 25);
        quit.setPosition(50, 175);
        quit.setText("Quit");
        
        quit.addCallback(
        	new Runnable() {
        		public void run() {
        			Window.setQuit();
        		}
        	}
        );
        
        //Group vgroup = layout.createSequentialGroup(new_game,load_game,options,quit);
        //layout.setVerticalGroup(vgroup);
        //layout.addDefaultGaps();
        
        add(layout);
    }
}
