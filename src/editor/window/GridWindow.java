package editor.window;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import editor.Block;
import editor.CubicGrid;

public class GridWindow extends ResizableFrame implements ActionListener{
	private DialogLayout layout;
	private CubicGrid<Block<Integer>> grid;
	
	public GridWindow(Integer grid_size) {
		this.setSize(400, 400);
		
		setTitle("JGE3d test");

		// Create the layout and button instances
		layout = new DialogLayout();

		this.grid = new CubicGrid<Block<Integer>>(grid_size);

		for(int x = 0; x < grid_size; x++){
			for(int y = 0; y < grid_size; y++){
				for(int z = 0; z < grid_size; z++){
					this.grid.set(x, y, z, new Block<Integer>());
				}
			}
		}

		// !!!EXAMPLE OF DIALOG LAYOUT!!!//
		// Sequential groups are like a Swing boxlayout and just lists from top
		// to bottom
		// Parallel groups align each start and size and can be cascaded
		//
		// Group for holding the Horizontal alignment of the buttons
		Group full_grid = layout.createParallelGroup().addGap();
		Group row = layout.createParallelGroup().addGap();
		for(int k=0;k<grid.size();k++) {
			for(int i=0;i<grid.size();i++) {
				row = layout.createParallelGroup().addGap();
				for(int j=0;j<grid.size();j++) {
					//TODO: *un*fix the z axis
					row.addWidget(grid.get(j, i, 0));
				}
				row.addGap();
			}
			full_grid.addGroup(row);
		}
		/*
		Group rows = layout.createSequentialGroup()
			.addGap()
			// Keeps all the buttons in a single vertical line as opposed to
			// staggering
			// left to right per row
			.addGroup(
				layout.createParallelGroup(
					new_game,
					load_game,
					options,
					quit)
				)
			.addGap();

		// Group for holding the Vertical alignment of the buttons
		Group button_vgroup = layout.createSequentialGroup()
			.addGap()
			// Add each widget without forming a group so that they rest one
			// under the other
			.addWidget(new_game)
			.addWidget(load_game)
			.addWidget(options)
			.addWidget(quit)
			.addGap();
		*/
		Group hurr = layout.createSequentialGroup().addGap().addGroup(full_grid).addGap();
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(hurr);
		layout.setVerticalGroup(full_grid);

		// Make sure to add the layout to the frame
		add(layout);
		// !!! END EXAMPLE !!!//
	}

	public void addCellListener(ActionListener listener){
	    for(int i = 0; i < grid.size(); i++){
	      for(int j = 0; j < grid.size(); j++){
	        for(int k = 0; k < grid.size(); k++){
	          grid.get(i, j, k).addActionListener(listener);
	        }
	      }
	    }
	  }
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(((Block<Integer>) e.getSource()).getColor().toString());
	}
}
