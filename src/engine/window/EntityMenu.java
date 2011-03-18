package engine.window;

import engine.window.components.Tree;
import engine.window.tree.Model;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.ResizableFrame;
import engine.entity.EntityList;

public class EntityMenu extends ResizableFrame {
	private final Tree textree;
	private final DialogLayout layout;
	
	public EntityMenu(EntityList objectList) {
		super();
		textree = new Tree(objectList,null);
		layout = new DialogLayout();
		entityMenuInit(objectList);
	}
	
	public EntityMenu() {
		super();
		textree = new Tree(null, null);
		layout = new DialogLayout();
		entityMenuInit(null);
	}
	
	
	public EntityMenu(Model m) {
		super();
		textree = new Tree(null, m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}
	
	public EntityMenu(EntityList objectList, Model m) {
		super();
		textree = new Tree(objectList, m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}


	private void entityMenuInit(EntityList objectList){
		setTitle("Entity Editor");
		
		if(objectList != null){
			//objectList.registerObserver(textree);
		}
		textree.setTheme("textree");
		
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
	}
}
