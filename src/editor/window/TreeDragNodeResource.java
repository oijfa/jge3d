package editor.window;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.TableBase;
import de.matthiasmann.twl.TableSelectionManager;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSelectionModel;
import engine.Engine;
import engine.entity.Camera;
import engine.entity.Entity;

public class TreeDragNodeResource implements TableSelectionManager {
	private TableBase table;
	private TreeTable table_data;
	private Engine engine;
	private String dragging_name;
	
	public TreeDragNodeResource(TreeTable table_data) {
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
	
	@Override
	public boolean handleMouseEvent(int row, int column, Event event) {
		if(engine != null) {
			if(engine.getEntityList().getItem("camera") != null) {
				if(table != null) {
					if(event.getMouseButton() != -1 && event.isMouseDragEnd()) {
						Camera cam = (Camera)engine.getEntityList().getItem("camera");
						Vector3f new_pos = cam.getRayTo(event.getMouseX(), event.getMouseY(), (float)cam.getDistance());
						
						//Class item_class = ((ResourceItem)table_data.getNodeFromRow(row).getData(1)).item_class.getClass();
						//item_class.newInstance()
						try {
							Entity ent = engine.addEntity(1f, true, dragging_name, "default");
							ent.setProperty("position",new Vector3f(0, 0, 0));
							ent.setAngularFactor(0, new Vector3f(0,1,0));
							ent.setProperty("gravity",new Vector3f(0,0,0));
							ent.setProperty("position",new_pos);
						} catch (Exception e){
							e.printStackTrace();
						}
						return true;
					} else if (event.getMouseButton() != -1 && !event.isMouseDragEnd()) {
						dragging_name = (String)table_data.getNodeFromRow(row).getData(0);
					}
				} else {
					System.out.println("Table is not set");
				}
			} else {
				System.out.println("Camera ref is not set");
			}
		} else {
			System.out.println("ResourceWindow: Engine is not set");
		}
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
	
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
}
