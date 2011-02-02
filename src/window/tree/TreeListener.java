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
					//Reset position of all other objects
					for(String name: objects.getKeySet()) {
						//For everything but the camera do the following
						if(!name.equals(Camera.CAMERA_NAME)) {
							//Stop the movement
							objects.getItem(name).setDamping(1.0f,1.0f);
							objects.getItem(name).activate();
							objects.getItem(name).setAngularFactor(0.0f, new Vector3f(0,0,0));
							
							//Move the object back to its original position
							objects.getItem(name).setPosition(Config.getPosition(name));
							objects.getItem(name).setAngularIdentity();
						}
							
					}
					
					//Focus to this ent
					((Camera)objects.getItem(Camera.CAMERA_NAME)).focusOn(newFocus);
					
					//Move the ent out of the lineup
					newFocus.applyImpulse(new Vector3f(0,1,0), newFocus.getPosition());
					newFocus.setDamping(0.0f,0.0f);
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
