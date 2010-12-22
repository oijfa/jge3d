package window.components;

import java.util.prefs.Preferences;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.AbstractTreeTableModel;
import de.matthiasmann.twl.model.AbstractTreeTableNode;
import de.matthiasmann.twl.model.PersistentStringModel;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.model.TreeTableNode;

public class Tree extends ScrollPane implements Runnable {
    //private MyNode dynamicNode;
    int state;
    Node subNode;

    public Tree() {
        Model m = new Model();
        PersistentStringModel psm = new PersistentStringModel(
            Preferences.userNodeForPackage(getClass()),
            "demoEditField",
        	"you can edit this"
        );

        Node a = m.insert("A", "1");
        a.insert("Aa", "2");
        a.insert("Ab", "3");
        a.insert("EditField", psm);
        Node b = m.insert("B", "4");
        b.insert("hurrtest", "lawl");
        m.insert(new SpanString("This is a very long string which will span into the next column.", 2), "Not visible");
        m.insert("This is a very long string which will be clipped.", "This is visible");

        TreeTable t = new TreeTable(m);
        t.setTheme("/table");
        t.registerCellRenderer(SpanString.class, new SpanRenderer());
        t.registerCellRenderer(StringModel.class, new EditFieldCellRenderer());
        t.setDefaultSelectionManager();

        setContent(t);
        setTheme("/tableScrollPane");
    }

    public void run() {
    	
    }
    
    public void centerScrollPane() {
        updateScrollbarSizes();
        setScrollPositionX(getMaxScrollPosX()/2);
        setScrollPositionY(getMaxScrollPosY()/2);
    }

    static class Node extends AbstractTreeTableNode {
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

    static class Model extends AbstractTreeTableModel {
        private static final String[] COLUMN_NAMES = {"Tree", "Value"};
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
    }

    static class SpanString {
        private final String str;
        private final int span;

        public SpanString(String str, int span) {
            this.str = str;
            this.span = span;
        }

        @Override
        public String toString() {
            return str;
        }
    }
    
    static class SpanRenderer extends TreeTable.StringCellRenderer {
        int span;

        @Override
        public void setCellData(int row, int column, Object data) {
            super.setCellData(row, column, data);
            span = ((SpanString)data).span;
        }

        @Override
        public int getColumnSpan() {
            return span;
        }
    }

    static class EditFieldCellRenderer implements TreeTable.CellWidgetCreator {
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
}