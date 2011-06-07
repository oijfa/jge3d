package editor.window;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.DialogLayout.Group;
import editor.Block;
import editor.CubicGrid;

public class GridWindow extends ResizableFrame implements ActionListener{
	private CubicGrid<Block<Integer>> grid;
	private Color current_color = new Color((byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF);
	private DialogLayout layout;
	
	public GridWindow(Integer grid_size) {
		this.setSize(400, 400);
		
		setTitle("Grid View");

		this.grid = new CubicGrid<Block<Integer>>(grid_size);

		for(int z = 0; z < grid_size; z++){
			for(int y = 0; y < grid_size; y++){
				for(int x = 0; x < grid_size; x++){
					this.grid.set(x, y, z, new Block<Integer>());
				}
			}
		}

		this.addCellListener(this);
		
		// Create the layout and button instances
		layout = new DialogLayout();
		
		if(grid_size > 0)
			loadLayer(0);
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
	
	public void setCurrentColor(Color color){
		current_color = color;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		((Block<Integer>) e.getSource()).setColor(current_color);
	}
	
	public void loadLayer(Integer layer) {
		layout.removeAllChildren();
		this.removeChild(layout);

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
				row.addWidget(grid.get(j, i, layer));
			}
			h_grid.addGroup(row);
		}

		//Create vertical rows
		for(int i=0;i<grid.size();i++) {
			row = layout.createSequentialGroup();
			for(int j=0;j<grid.size();j++) {
				//TODO: *un*fix the z axis
				row.addWidget(grid.get(i, j, layer));
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
}
