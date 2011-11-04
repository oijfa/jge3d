package engine.window.components;

import java.io.IOException;

import test.JarContents;
import engine.window.tree.ColoredTextRenderer;
import engine.window.tree.ColoredTextString;
import engine.window.tree.EditFieldCellRenderer;
import engine.window.tree.Model;
import engine.window.tree.Node;
import engine.window.tree.SpanRenderer;
import engine.window.tree.SpanString;
import engine.monitoring.Observer;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.model.TreeTableNode;

public class Tree extends ScrollPane implements Observer {
	// private MyNode dynamicNode;
	int state;
	Model treeModel;
	TreeTable treeTable;

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

		/*
		//TODO: model listener
		TableSingleSelectionModel selectionModel = new TableSingleSelectionModel();
		selectionModel.addSelectionChangeListener(
			new TreeListener(treeTable,	selectionModel, objectList)
		);

		treeTable.setSelectionManager(
			new TableRowSelectionManager(
				selectionModel
			)
		);
		*/

		setContent(treeTable);
		setTheme("/tableScrollPane");
	}

	@Override
	public void update(Object _name) {
		String name = (String) _name;

		Model model = treeModel;
		// If the node does not exist, create it
		try {
			Node n = searchAllNodes(name, model);
			if (n == null) {
				model.insert(name, "");
				n = searchAllNodes(name, model);
			}

			// Remove all children
			n.removeAll();

			// Create new children with updated values
			//TODO: STUB FOR new JarContents!
		} catch (Exception e) {
			System.out.println("Failed to update node");
			e.printStackTrace();
		}
	}

	public void createFromProjectResources() {
		try {
			JarContents jar = new JarContents();
			
			TreeTableNode current_node = treeModel;
			Node found_node;
			
			for(String file_path: jar.getFiles()) {
				for(String file: file_path.split("/")) {
					found_node = searchCurrentLevel(file, current_node);
					if(found_node == null)
						createNode(file,"",(Node)current_node);
					else
						break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void createNode(String key, String value, Node base_node) {
		base_node.insert(key, value);
	}

	public void createSubNode(TreeTableNode node, String parent, String name) {
		Node insertTo = searchAllNodes(parent, node);
		if (insertTo != null) {
			insertTo.insert(name, "Value?");
		}
	}

	public void centerScrollPane() {
		updateScrollbarSizes();
		setScrollPositionX(getMaxScrollPosX() / 2);
		setScrollPositionY(getMaxScrollPosY() / 2);
	}

	private Node searchCurrentLevel(String nodeName, TreeTableNode findIn) {
		Node child;
		// Loop through all children
		for (int i = 0; i < findIn.getNumChildren(); i++) {
			child = (Node) findIn.getChild(i);

			// If the first column has what we're looking for
			System.out.println(child.getData(0) + "==" + nodeName);
			if (child.getData(0).equals(nodeName)) {
				// return it
				return child;
			}
		}
		return null;
	}
	
	private Node searchAllNodes(String nodeName, TreeTableNode findIn) {
		Node child;
		// Loop through all children
		for (int i = 0; i < findIn.getNumChildren(); i++) {
			child = (Node) findIn.getChild(i);

			// If the first column has what we're looking for
			System.out.println(child.getData(0) + "==" + nodeName);
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
}