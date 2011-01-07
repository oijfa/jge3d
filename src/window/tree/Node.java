package window.tree;

import de.matthiasmann.twl.model.AbstractTreeTableNode;
import de.matthiasmann.twl.model.TreeTableNode;

public class Node extends AbstractTreeTableNode {
    private Object str0;
    private Object str1;

    public Node(TreeTableNode parent, Object str0, Object str1) {
        super(parent);
        this.str0 = str0;
        this.str1 = str1;
        setLeaf(true);
    }

    public Object getData(int column) {
        return (column == 0) ? str0 : str1;
    }

    public Node insert(Object str0, Object str1) {
        Node n = new Node(this, str0, str1);
        insertChild(n, getNumChildren());
        setLeaf(false);
        return n;
    }

    public void remove(int idx) {
        removeChild(idx);
    }

    public void removeAll() {
        removeAllChildren();
    }
}
