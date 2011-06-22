package engine.window.tree;

import de.matthiasmann.twl.TreeTable;

public class SpanRenderer extends TreeTable.StringCellRenderer {
	int span;

	@Override
	public void setCellData(int row, int column, Object data) {
		super.setCellData(row, column, data);
		span = ((SpanString) data).span;
	}

	@Override
	public int getColumnSpan() {
		return span;
	}
}
