package engine.window.components;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;

public class ContainerBox extends ResizableFrame {
	private DialogLayout frame_layout;
	private Group horizontal;
	private Group vertical;
	
	public ContainerBox(String name) {
		setTitle(name);

		// Create the layout and button instances
		frame_layout = new DialogLayout();
		frame_layout.setTheme("frame_layout");
		
		Group horizontal = frame_layout.createParallelGroup();
		Group vertical = frame_layout.createSequentialGroup();
		
		frame_layout.setHorizontalGroup(horizontal);
		frame_layout.setVerticalGroup(vertical);		

		// Make sure to add the layout to the frame
		add(frame_layout);
		// !!! END EXAMPLE !!!//
	}
	
	public void addWindow(ResizableFrame frame) {
		horizontal.addWidget(frame);
		vertical.addWidget(frame);
	}
}
