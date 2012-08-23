package editor.window;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.TableBase;
import de.matthiasmann.twl.TableSelectionManager;
import de.matthiasmann.twl.TreeTable;
import de.matthiasmann.twl.model.TableSelectionModel;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.Engine;
import engine.entity.Camera;
import engine.entity.Entity;
import engine.window.tree.Node;

public class TreeDragNodeEntity implements TableSelectionManager, ActionListener{
	private TableBase table;
	private TreeTable table_data;
	private Engine engine;
	private String dragging_name;
	private Node last_node;
	private ArrayList<ActionListener> action_listeners;
	
	public TreeDragNodeEntity(TreeTable table_data) {
		this.table_data = table_data;
		action_listeners = new ArrayList<ActionListener>();
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
					if(event.getMouseButton() != -1 && event.isMouseDragEnd() && dragging_name != null) {
						Camera cam = (Camera)engine.getEntityList().getItem("camera");
						Vector3f new_pos = cam.getRayTo(event.getMouseX(), event.getMouseY(), cam.getDistance());
						
						//Class item_class = ((ResourceItem)table_data.getNodeFromRow(row).getData(1)).item_class.getClass();
						//item_class.newInstance()
						try {
							Entity ent = engine.addEntity(new Entity(engine.getEntity(dragging_name)));
							ent.setPosition(new Vector3f(0, 0, 0));
							ent.setAngularFactor(0, new Vector3f(0,1,0));
							ent.setGravity(new Vector3f(0,0,0));
							ent.setPosition(new_pos);
						} catch (Exception e){
							e.printStackTrace();
						}
						dragging_name = null;
					} else if (event.getMouseButton() != -1 && event.isMouseDragEnd() && dragging_name == null) {
						
					} else if (event.getMouseButton() != -1 && !event.isMouseDragEnd()) {
						dragging_name = (String)table_data.getNodeFromRow(row).getData(0);
					}
					last_node = (Node)table_data.getNodeFromRow(row);
				} else {
					System.out.println("Table is not set");
				}
			} else {
				System.out.println("Camera ref is not set");
			}
		} else {
			System.out.println("EntityListWindow: Engine is not set");
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

	public Node getLastClickedNode() {
		return last_node;
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		fireActionEvent("tree_node_clicked");
	}
	
	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent(String event) {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this, event));
		}
	}
}
