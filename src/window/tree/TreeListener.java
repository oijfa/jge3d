package window.tree;

import controller.Config;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSingleSelectionModel;
import entity.Camera;
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
	
			Entity newFocus;
			try {
				newFocus = objects.getItem(Config.getName() + "-" + (String)n.getData(0));
				if( newFocus != null){
					((Camera)objects.getItem(Camera.CAMERA_NAME)).focusOn(newFocus);
					newFocus.applyImpulse(new Vector3f(0,1,0), newFocus.getPosition());
					newFocus.activate();
					newFocus.setAngularFactor(0.0f, new Vector3f(1,1,0));
				}
			} catch (Exception e) {
				System.out.println("No config to catch");
				e.printStackTrace();
			}
		}
	}
}
