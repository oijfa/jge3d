package window;

import window.components.Tree;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import entity.EntityList;

public class EntityMenu extends ResizableFrame {
	private final Tree textree;
	private final DialogLayout layout;
	public EntityMenu() {
		setTitle("Entity Editor");
		
		textree = new Tree();
		textree.setTheme("textree");
		layout = new DialogLayout();
		Group hgroup = layout.createSequentialGroup()
		.addGap()
		.addGroup(layout.createParallelGroup(textree)
		.addGap()
		);
		
		Group vgroup = layout.createSequentialGroup()
		.addWidget(textree)
		;
		
		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);
		
		add(layout);
	}
	
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
		
		add(layout);
	}
}
