package engine.window.components;

import engine.resource.ResourceManager.ResourceItem;
import engine.window.tree.ColoredTextRenderer;
import engine.window.tree.ColoredTextString;
import engine.window.tree.EditFieldCellRenderer;
import engine.window.tree.Model;
import engine.window.tree.Node;
import engine.window.tree.SpanRenderer;
import engine.window.tree.SpanString;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TableSelectionManager;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.StringModel;

public class Tree extends ScrollPane {// implements Observer {
	private Model treeModel;
	private TreeTable treeTable;

	public Tree() {
		super();
		treeInit();
	}
	
	public Tree(Model m) {
		super();
		treeInit();
	}

	private void treeInit() {
		treeModel = new Model();

		treeTable = new TreeTable(treeModel);
		treeTable.setTheme("/table");
		treeTable.registerCellRenderer(SpanString.class, new SpanRenderer());
		treeTable.registerCellRenderer(StringModel.class,
			new EditFieldCellRenderer());
		treeTable.registerCellRenderer(ColoredTextString.class,
			new ColoredTextRenderer());
		//treeTable.registerCellRenderer(EditField.class, new EditFieldCellRenderer());
		treeTable.registerCellRenderer(ComboBox.class, new ComboBox<String>());

		setContent(treeTable);
		setTheme("/tableScrollPane");
	}

	public void createFromProjectResources() {
		/*
		try {
			//Query the jar for resource listings
			JarContents jar = new JarContents();

			//for each file path in listing
			for(String file_path: jar.getFiles()) {
				Node found_node = null;
				Node previous_node = null;
				//for each sub dir in file path
				for(String file: file_path.split("/")) {
					//find the file at the level you are in
					if(found_node == null) //search the root model
						found_node = searchCurrentLevel(file, treeModel);
					else //search the last node visited
						found_node = searchCurrentLevel(file, found_node);
					
					//if it doesn't exist create at this level
					if(found_node == null) {
						if(previous_node == null) //create in root 
							found_node = createNode(file,"",treeModel);
						else //create in last node visited
							found_node = createNode(file,"",previous_node);
					} //else check the next subdir from the subnode
					
					previous_node = found_node;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public Model getBase() {
		return treeModel;
	}
	
	public TreeTable getTable() {
		return treeTable;
	}
	
	public boolean contains(String key) {
		boolean found = false;
		for(Node node: treeModel.getChildren()) {
			if(node.getData(1).equals("key"))
				found = true;
		}
		return found;
	}
	
	public Node createNode(String key, Object value, Model base_node) {
			return base_node.insert(key, value);
	}
	
	public Node createNode(String key, ResourceItem value, Node base_node) {
		return base_node.insert(key, value);
	}

	public Node createSubNode(Node node, String parent, String name) {
		Node insertTo = searchAllNodes(parent, node);
		if (insertTo != null) {
			return insertTo.insert(name, "Value?");
		} 
		return null;	
	}

	public void removeAllNodes() {
		treeModel.removeAll();
	}
	
	public void centerScrollPane() {
		updateScrollbarSizes();
		setScrollPositionX(getMaxScrollPosX() / 2);
		setScrollPositionY(getMaxScrollPosY() / 2);
	}

	public Node searchCurrentLevel(String nodeName, Node findIn) {
		Node child;
		// Loop through all children
		if(findIn != null) {
			for (int i = 0; i < findIn.getNumChildren(); i++) {
				child = (Node)findIn.getChild(i);
	
				// If the first column has what we're looking for
				if (child.getData(0).equals(nodeName)) {
					// return it
					return child;
				}
			}
		}
		return null;
	}
	
	public Node searchAllNodes(String nodeName, Node findIn) {
		Node child;
		// Loop through all children
		for (int i = 0; i < findIn.getNumChildren(); i++) {
			child = (Node)findIn.getChild(i);

			// If the first column has what we're looking for
			if (child.getData(0).equals(nodeName)) {
				// return it
				return child;
			} else {
				// else look through the child's children
				Node temp = searchAllNodes(nodeName, child);
				if (temp != null) { return temp; }
			}
		}
		return null;
	}
	
	public void setTreeSelectionManager(TableSelectionManager tsm) {
		treeTable.setSelectionManager(tsm);
	}
}
