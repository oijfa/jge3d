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
		Group h_grid = layout.createParallelGroup();
		// Group for holding the Vertical alignment of the buttons
		Group v_grid = layout.createParallelGroup();
		// Generic row up buttons
		Group row = layout.createSequentialGroup(); 
		
		//Create the horizontal rows
		for(int i=0;i<grid.size();i++) {
			row = layout.createSequentialGroup();
			for(int j=0;j<grid.size();j++) {
				//TODO: *un*fix the z axis
				row.addWidget(grid.get(j, i, 0));
			}
			h_grid.addGroup(row);
		}

		for(int i=0;i<grid.size();i++) {
			row = layout.createSequentialGroup();
			for(int j=0;j<grid.size();j++) {
				//TODO: *un*fix the z axis
				row.addWidget(grid.get(i, j, 0));
			}
			v_grid.addGroup(row);
		}

		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		layout.setHorizontalGroup(h_grid);
		layout.setVerticalGroup(v_grid);

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
