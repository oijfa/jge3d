package engine.utils;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class DomTools {
	public static ArrayList<Node> findChildrenByName(Node root, String name) {
		String[] names = new String[1];
		names[0] = name;
		return findChildrenByName(root, names);
	}

	public static ArrayList<Node> findChildrenByName(Node root, String[] names) {
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < names.length; i++) {
			Node e = root.getFirstChild();
			while (e != null) {
				if (e.getNodeName().equals(names[i])) {
					list.add(e);
				}
				e = e.getNextSibling();
			}
		}
		return list;
	}
}
