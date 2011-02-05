package window.tree;


import de.matthiasmann.twl.TableBase;
import de.matthiasmann.twl.model.TreeTableNode;

public class JGETableBase extends TableBase{
	public void changeColor(Node node){
		CellRenderer c = this.getCellRenderer(0,0,node);
		c.applyTheme(this.tableBaseThemeInfo.getChildTheme(node.getColor()));
	}
	
	@Override
	protected TreeTableNode getNodeFromRow(int row) {
		return null;
	}

	@Override
	protected Object getCellData(int row, int column, TreeTableNode node) {
		return null;
	}

	@Override
	protected Object getTooltipContentFromRow(int row, int column) {
		return null;
	}

}
