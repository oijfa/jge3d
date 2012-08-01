package editor.window;

import engine.Engine;
import engine.resource.ResourceManager;
import engine.window.components.Tree;
import engine.window.components.Window;
import engine.window.tree.Model;
import engine.window.tree.Node;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.model.TreeTableNode;

public class ResourceWindow extends Window {
	private final Tree resource_window;
	private final DialogLayout layout;
	private TreeDragNodeResource tsm;
	private Engine engine;

	public ResourceWindow(Engine engine) {
		super();
		resource_window = new Tree();
		tsm = new TreeDragNodeResource(resource_window.getTable());
		resource_window.setTreeSelectionManager(tsm);
		layout = new DialogLayout();
		this.engine = engine;
		resourceMenuInit();
	}

	public ResourceWindow(Model m, Engine engine) {
		super();
		resource_window = new Tree(m);
		layout = new DialogLayout();
		this.engine = engine;
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
		//resource_window.createFromProjectResources();
		createResources();
	}
	
	public void createResources() {
		Node found_node;
		for(String category: engine.resource_manager.getResources().keySet()) {
			if(resource_window.contains(category))
				found_node = resource_window.searchCurrentLevel(category, (Node)(TreeTableNode)resource_window.getBase());
			else
				found_node = resource_window.createNode(category, "", resource_window.getBase());
			
			for(ResourceManager.ResourceItem resource: engine.resource_manager.getResourcesInCategory(category)) {
				resource_window.createNode(resource.name, resource, found_node);
			}
		}
	}

	public void setEngine(Engine engine) {
		tsm.setEngine(engine);
	}
}
