package window.tree;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.StringModel;

public class EditFieldCellRenderer implements TreeTable.CellWidgetCreator {
    private StringModel model;
    private int editFieldHeight;

    public Widget updateWidget(Widget existingWidget) {
        EditField ef = (EditField)existingWidget;
        if(ef == null) {
            ef = new EditField();
        }
        ef.setModel(model);
        return ef;
    }

    public void positionWidget(Widget widget, int x, int y, int w, int h) {
        widget.setPosition(x, y);
        widget.setSize(w, h);
    }

    public void applyTheme(ThemeInfo themeInfo) {
        editFieldHeight = themeInfo.getParameter("editFieldHeight", 10);
    }

    public String getTheme() {
        return "EditFieldCellRenderer";
    }

    public void setCellData(int row, int column, Object data) {
        this.model = (StringModel)data;
    }

    public int getColumnSpan() {
        return 1;
    }

    public int getPreferredHeight() {
        return editFieldHeight;
    }

    public Widget getCellRenderWidget(int x, int y, int width, int height, boolean isSelected) {
        return null;
    }
}