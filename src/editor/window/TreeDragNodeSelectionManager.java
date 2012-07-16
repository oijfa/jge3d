package editor.window;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.TableBase;
import de.matthiasmann.twl.TableSelectionManager;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSelectionModel;
import engine.entity.Camera;
import engine.entity.Entity;
import engine.resource.ResourceManager.ResourceItem;

public class TreeDragNodeSelectionManager implements TableSelectionManager {
	private TableBase table;
	private TreeTable table_data;
	private Camera camera;
	
	public TreeDragNodeSelectionManager(TreeTable table_data) {
		this.table_data = table_data;
	}
	
	@Override
	public TableSelectionModel getSelectionModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAssociatedTable(TableBase base) {
		table = base;
	}

	@Override
	public SelectionGranularity getSelectionGranularity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleKeyStrokeAction(String action, Event event) {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	String name_to_use = "bunny";
	Entity test = engine.addEntity(name_to_use, 1f, true, name_to_use, "default");
	test.setProperty(Entity.NAME, name_to_use);
	test.setPosition(new Vector3f(0, 0, 0));
	test.setAngularFactor(0, new Vector3f(0,1,0));
	test.setGravity(new Vector3f(0,0,0));
	*/
	@Override
	public boolean handleMouseEvent(int row, int column, Event event) {
		if(camera != null) {
			if(table != null) {
				if(event.getMouseButton() != -1 && event.isMouseDragEnd()) 
					System.out.println(
						"DROPPED: " + 
						table_data.getNodeFromRow(row).getData(1).toString() + 
						"@" + event.getMouseX() + ":" + event.getMouseY()
					);
					//Vector3f new_pos = camera.getRayTo(event.getMouseX(), event.getMouseY(), 20);
					//Class item_class = ((ResourceItem)table_data.getNodeFromRow(row).getData(1)).item_class.getClass();
					//item_class.newInstance()
				
					
				
				return true;
			}
			System.out.println("Table is not set");
		}
		System.out.println("Camera ref is not set");
		
		return false;
	}

	@Override
	public boolean isRowSelected(int row) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellSelected(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLeadRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLeadColumn() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void modelChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rowsInserted(int index, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rowsDeleted(int index, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void columnInserted(int index, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void columnsDeleted(int index, int count) {
		// TODO Auto-generated method stub

	}

	public void getLastClickedNode() {
		
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
