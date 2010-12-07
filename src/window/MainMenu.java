package window;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;

public class MainMenu extends ResizableFrame {
	private final DialogLayout layout;
    private final Button new_game;
    private final Button load_game;
    private final Button options;
    private final Button quit;
    
    public MainMenu() {
        setTitle("JGE3d test");

        //Create the layout and button instances
        layout = new DialogLayout();
        this.new_game = new Button("New Game");
        this.load_game = new Button("Load Game");
        this.options = new Button("Options");
        this.quit = new Button("Quit");

        //When the quit button is clicked run this command
        quit.addCallback(
        	new Runnable() {
        		public void run() {
        			Window.setQuit();
        		}
        	}
        );

        //!!!EXAMPLE OF DIALOG LAYOUT!!!//
        //Sequential groups are like a Swing boxlayout and just lists from top to bottom
        //Parallel groups align each start and size and can be cascaded
        //
      	//Group for holding the Horizontal alignment of the buttons
        Group button_hgroup = layout.createSequentialGroup()
        	.addGap()
        	//Keeps all the buttons in a single vertical line as opposed to staggering 
        	//left to right per row
        	.addGroup(layout.createParallelGroup(new_game,load_game,options,quit))    		
        	.addGap();
        
        //Group for holding the Vertical alignment of the buttons
        Group button_vgroup = layout.createSequentialGroup()
    		.addGap()
    		//Add each widget without forming a group so that they rest one
    		//under the other
    		.addWidget(new_game)        		
    		.addWidget(load_game)
    		.addWidget(options)
    		.addWidget(quit)
    		.addGap();
        
        //All Dialog layout groups must have both a HorizontalGroup and VerticalGroup
        //Otherwise "incomplete" exception is thrown and layout is not applied
        layout.setHorizontalGroup(button_hgroup);
        layout.setVerticalGroup(button_vgroup);
        
        //Make sure to add the layout to the frame
        add(layout);
        //!!! END EXAMPLE !!!//
    }
}
