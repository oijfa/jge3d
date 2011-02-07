package window.tree;

import de.matthiasmann.twl.TreeTable;

public class JGETreeTable extends TreeTable{
	public JGETreeTable(Model treeModel) {
		super(treeModel);
	}

	public void changeColor(Node node){
		CellRenderer c = this.getCellRenderer(0,0,node);
	}
}

