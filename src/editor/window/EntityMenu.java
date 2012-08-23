package editor.window;

import java.util.ArrayList;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Widget;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.entity.Entity;
import engine.window.components.RGBAAdjuster;
import engine.window.components.Window;
import engine.window.components.XYZAdjuster;

public class EntityMenu extends Window implements ActionListener {
//Right aligned docked to screen
	//EntName <tree>
	//World Props 
	////Use reflection to mirror the entity class
	////Props are inserted into a tree
	////Props use predefined controls based
	////  on datatype
	//
	//Model Props
	//Select model <tree>
	////Texture
	////ExtraFields???
	//
	//Color Props

	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private ArrayList<Widget> widgets;
	private Entity ent;
	
	public EntityMenu() {
		super();
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Entity Menu");
	
		this.setTheme("entitymenu");
		if(ent != null) {
			for(String prop: Entity.reqKeys) {
				if(ent.getProperty(prop).getClass() == float.class
						|| ent.getProperty(prop).getClass() == Float.class) { 
					Widget field = new EditField();
					widgets.add(field);
					((EditField)field).setText((String)ent.getProperty(prop));
				} else if (ent.getProperty(prop).getClass() == int.class
						|| ent.getProperty(prop).getClass() == Integer.class) {
					Widget field = new ValueAdjusterInt();
					widgets.add(field);
					((ValueAdjusterInt)field).setValue((Integer)ent.getProperty(prop));
				} else if (ent.getProperty(prop).getClass() == String.class) {
					Widget field = new EditField();
					widgets.add(field);
					((EditField)field).setText((String)ent.getProperty(prop));
				} else if (ent.getProperty(prop).getClass() == boolean.class ||
						ent.getProperty(prop).getClass() == Boolean.class) {
					Widget field = new ToggleButton();
					widgets.add(field);
					((ToggleButton)field).getModel().setPressed((Boolean)ent.getProperty(prop));
				} else if (ent.getProperty(prop).getClass() == Vector3f.class) {
					Widget field = new XYZAdjuster(prop);
					widgets.add(field);
					((XYZAdjuster)field).setValue((Vector3f)ent.getProperty(prop));
				} else if (ent.getProperty(prop).getClass() == Vector4f.class) {
					Widget field = new RGBAAdjuster(prop);
					widgets.add(field);
					((RGBAAdjuster)field).setValue((Vector4f)ent.getProperty(prop));
				} else{
					Widget field = new Label();
					widgets.add(field);
					((Label)field).setText((String)ent.getProperty(prop));
				}
			}
		}
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");
	
		// Reset temp row for vertical
		Group h_grid = dialoglayout.createParallelGroup();
		for(Widget widget: widgets) {
			h_grid.addWidget(widget);
		}
		
		
		Group v_grid = dialoglayout.createSequentialGroup();
		for(Widget widget: widgets) {
			v_grid.addWidget(widget);
		}
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(350,800);
		
		//this.add(layout);
		this.add(dialoglayout);
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}
	private void fireActionEvent(String event) {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this, event));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent("light");
	}
	
	public void setEntity(Entity ent) {
		this.ent = ent;
	}
}
