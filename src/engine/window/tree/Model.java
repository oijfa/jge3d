package engine.window.tree;

import java.util.ArrayList;

import de.matthiasmann.twl.model.AbstractTreeTableModel;

public class Model extends AbstractTreeTableModel {
	private static final String[] COLUMN_NAMES = { "Tree", "Value" };

	public int getNumColumns() {
		return 2;
	}

	public String getColumnHeaderText(int column) {
		return COLUMN_NAMES[column];
	}

	public Node insert(Object str0, String str1) {
		Node n = new Node(this, str0, str1);
		insertChild(n, getNumChildren());
		return n;
	}

	public Node insert(Object str0, String str1, String color) {
		Node n = new Node(this, str0, str1, color);
		insertChild(n, getNumChildren());
		return n;
	}

	public void remove(String name) {
		for (int i = 0; i < this.getNumChildren(); i++) {
			if (this.getChild(i).equals(name)) this.removeChild(i);
		}
	}

	public void removeAll() {
		this.removeAllChildren();
	}
	
	public ArrayList<Node> getChildren() {
		ArrayList<Node> children = new ArrayList<Node>();
		for (int j = 0; j < this.getNumChildren(); j++) {
			children.add((Node) this.getChild(j));
		}
		return children;
	}
}
