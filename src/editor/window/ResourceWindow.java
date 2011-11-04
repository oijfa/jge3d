package editor.window;

import engine.window.components.Tree;
import engine.window.components.Window;
import engine.window.tree.Model;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import engine.entity.EntityList;

public class ResourceWindow extends Window {
	private final Tree resource_window;
	private final DialogLayout layout;

	public ResourceWindow(EntityList objectList) {
		super();
		resource_window = new Tree(null);
		layout = new DialogLayout();
		entityMenuInit(objectList);
	}

	public ResourceWindow() {
		super();
		resource_window = new Tree(null);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	public ResourceWindow(Model m) {
		super();
		resource_window = new Tree(m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	public ResourceWindow(EntityList objectList, Model m) {
		super();
		resource_window = new Tree(m);
		layout = new DialogLayout();
		entityMenuInit(null);
	}

	private void entityMenuInit(EntityList objectList) {
		setTitle("Resource Editor");

		if (objectList != null) {
			// objectList.registerObserver(textree);
		}

		Group hgroup = layout.createSequentialGroup().addGroup(
			layout.createParallelGroup(resource_window));

		Group vgroup = layout.createSequentialGroup().addGap()
			.addWidget(resource_window).addGap();

		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);

		// textree.setSize(getWidth()/3, getHeight()/3);

		add(layout);
		
		// upgrade the tree after it has been added.
	}
}
