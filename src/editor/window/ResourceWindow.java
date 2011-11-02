package editor.window;

import engine.window.components.Tree;
import engine.window.components.Window;
import engine.window.tree.Model;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import engine.entity.EntityList;

public class ResourceWindow extends Window {
	private final Tree textree;
	private final DialogLayout layout;

	public ResourceWindow(EntityList objectList) {
		super();
		textree = new Tree(objectList, null);
		layout = new DialogLayout();
		entityMenuInit(objectList);
	}

	public ResourceWindow() {
		super();
		textree = new Tree(null, null);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	public ResourceWindow(Model m) {
		super();
		textree = new Tree(null, m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	public ResourceWindow(EntityList objectList, Model m) {
		super();
		textree = new Tree(objectList, m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	private void entityMenuInit(EntityList objectList) {
		setTitle("Entity Editor");

		if (objectList != null) {
			// objectList.registerObserver(textree);
		}
		textree.setTheme("textree");

		Group hgroup = layout.createSequentialGroup().addGroup(
			layout.createParallelGroup(textree));

		Group vgroup = layout.createSequentialGroup().addGap()
			.addWidget(textree).addGap();

		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);

		// textree.setSize(getWidth()/3, getHeight()/3);

		add(layout);
		// upgrade the tree after it has been added.
	}
}
