package engine.window.tree;

import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import engine.entity.Entity;
import engine.entity.EntityList;

public class TreeListener implements Runnable {
	TreeTable tree;
	TableSingleSelectionModel selection;
	EntityList objects;

	public TreeListener(TreeTable tree, TableSingleSelectionModel selection,
		EntityList objects) {
		this.tree = tree;
		this.selection = selection;
		this.objects = objects;
	}

	public void run() {
		int row = selection.getFirstSelected();
		if (row >= 0) {
			Node n = (Node) tree.getNodeFromRow(row);

			Entity newFocus;
			try {
				newFocus = objects.getItem("hurr" + "-"
					+ (String) n.getData(0).toString());
				if (newFocus != null) {
					itemClicked(newFocus);
				}
			} catch (Exception e) {
				System.out.println("No config to catch");
				e.printStackTrace();
			}
		}
	}

	private void itemClicked(Entity newFocus) {
		// TODO Implement this if you're going ot use this class
	}
}
