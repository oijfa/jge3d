package window;

import window.components.Tree;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import entity.EntityList;

public class EntityMenu extends ResizableFrame {
	private final Tree textree;
	private final DialogLayout layout;
	
	public EntityMenu(EntityList objectList) {
		setTitle("Entity Editor");
		
		textree = new Tree(objectList);
		objectList.registerObserver(textree);
		textree.setTheme("textree");
		layout = new DialogLayout();
		Group hgroup = layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(textree)
		);
		
		Group vgroup = layout.createSequentialGroup()
		.addGap()
		.addWidget(textree)
		.addGap();
		
		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);
		
		//textree.setSize(getWidth()/3, getHeight()/3);
		
		add(layout);
		//upgrade the tree after it has been added.
		textree.init();
	}
}
