package window.tree;

import controller.Config;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import entity.Entity;
import entity.EntityList;

public class TreeListener implements Runnable{
	TreeTable tree;
	TableSingleSelectionModel selection;
	EntityList objects;
	
	public TreeListener(TreeTable tree, TableSingleSelectionModel selection, EntityList objects){
		this.tree = tree;
		this.selection = selection;
		this.objects = objects;
	}
	
	public void run() {
		int row = selection.getFirstSelected();
		if(row >= 0) {
			Node n = (Node)tree.getNodeFromRow(row);
			try{
				Entity newFocus = objects.getItem(Config.getName() + "-" + (String)n.getData(0));
				if( newFocus != null){
					objects.getItem("linear_focus").setPosition(newFocus.getPosition());
				}
			}catch(Exception e){
				System.out.println("No config name");
			}
		}
	}
}
