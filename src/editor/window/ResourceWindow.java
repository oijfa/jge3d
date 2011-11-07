package editor.window;

import engine.window.components.Tree;
import engine.window.components.Window;
import engine.window.tree.Model;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;

public class ResourceWindow extends Window {
	private final Tree resource_window;
	private final DialogLayout layout;

	public ResourceWindow() {
		super();
		resource_window = new Tree();
		layout = new DialogLayout();
		resourceMenuInit();
	}

	public ResourceWindow(Model m) {
		super();
		resource_window = new Tree(m);
		layout = new DialogLayout();
		resourceMenuInit();
	}

	private void resourceMenuInit() {
		setTitle("Resource Editor");

		Group hgroup = layout.createSequentialGroup().addGroup(
			layout.createParallelGroup(resource_window));

		Group vgroup = layout.createSequentialGroup().addGap()
			.addWidget(resource_window).addGap();

		layout.setHorizontalGroup(hgroup);
		layout.setVerticalGroup(vgroup);

		// textree.setSize(getWidth()/3, getHeight()/3);

		add(layout);
		
		// update tree contents after it has been added.
		resource_window.createFromProjectResources();
	}
}
