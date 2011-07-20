package engine.window.components;

import engine.window.tree.ColoredTextRenderer;
import engine.window.tree.ColoredTextString;
import engine.window.tree.EditFieldCellRenderer;
import engine.window.tree.Model;
import engine.window.tree.Node;
import engine.window.tree.SpanRenderer;
import engine.window.tree.SpanString;
import engine.window.tree.TreeListener;
import engine.monitoring.Observer;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TableRowSelectionManager;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import de.matthiasmann.twl.model.TreeTableNode;
import engine.entity.Entity;
import engine.entity.EntityList;

public class Tree extends ScrollPane implements Observer {
	// private MyNode dynamicNode;
	int state;
	EntityList objectList;
	Model treeModel;
	TreeTable treeTable;

	public Tree(EntityList objectList, Model m) {
		super();
		treeInit(objectList);
	}

	private void treeInit(EntityList objectList) {
		treeModel = new Model();

		if (objectList != null) {
			this.objectList = objectList;
			// objectList.registerObserver(this);
			// model.removeAll();
			// this.createEntityListNode();
		}

		treeTable = new TreeTable(treeModel);
		treeTable.setTheme("/table");
		treeTable.registerCellRenderer(SpanString.class, new SpanRenderer());
		treeTable.registerCellRenderer(StringModel.class,
			new EditFieldCellRenderer());
		treeTable.registerCellRenderer(ColoredTextString.class,
			new ColoredTextRenderer());

		TableSingleSelectionModel selectionModel = new TableSingleSelectionModel();
		selectionModel.addSelectionChangeListener(new TreeListener(treeTable,
			selectionModel, objectList));

		treeTable.setSelectionManager(new TableRowSelectionManager(
			selectionModel));

		setContent(treeTable);
		setTheme("/tableScrollPane");
	}

	// From here down Not used when using config files --Robert
	@Override
	public void update(Object _name) {
		String name = (String) _name;

		Model model = treeModel;
		// If the node does not exist, create it
		try {
			Node n = findChildNode(name, model);
			if (n == null) {
				model.insert(name, "");
				n = findChildNode(name, model);
			}

			// Remove all children
			n.removeAll();

			// Create new children with updated values
			createEntityNode(objectList.getItem(name), n);
		} catch (Exception e) {
			System.out.println("Failed to update node");
			e.printStackTrace();
		}
	}

	public void createEntityListNode() {
		Node entityNode;
		for (Entity ent : objectList.getEntities()) {
			try {
				entityNode = treeModel.insert(ent.getProperty("name"), ent
					.getPosition().toString());

				this.createEntityNode(ent, entityNode);
			} catch (Exception e) {
				System.out.println("Couldn't insert into config's treemodel");
				e.printStackTrace();
			}
		}
	}

	public void createEntityNode(Entity ent, Node entityNode) {
		for (String key : ent.getKeySet()) {
			Object obj = ent.getProperty(key);
			entityNode.insert(key, obj.toString());
		}
	}

	@SuppressWarnings("unused")
	private void createSubNode(TreeTableNode node, String parent, String name) {
		Node insertTo = findChildNode(parent, node);
		if (insertTo != null) {
			insertTo.insert(name, "Value?");
		}
	}

	public void centerScrollPane() {
		updateScrollbarSizes();
		setScrollPositionX(getMaxScrollPosX() / 2);
		setScrollPositionY(getMaxScrollPosY() / 2);
	}

	private Node findChildNode(String nodeName, TreeTableNode findIn) {
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
				Node temp = findChildNode(nodeName, child);
				if (temp != null) { return temp; }
			}
		}
		return null;
	}
}