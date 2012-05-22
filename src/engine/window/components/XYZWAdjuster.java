package engine.window.components;

import java.util.ArrayList;

import javax.vecmath.Vector4f;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;


public class XYZWAdjuster extends Widget implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private Label label;
	private LabeledValueAdjuster x;
	private LabeledValueAdjuster y;
	private LabeledValueAdjuster z;
	private LabeledValueAdjuster w;

	public XYZWAdjuster(String name) {
		action_listeners = new ArrayList<ActionListener>();
		this.setTheme("rgbaadjuster");
		label = new Label(name);
		label.setTheme("label");
		x = new LabeledValueAdjuster("X");
		x.setTheme("labeledvalueadjuster");
		y = new LabeledValueAdjuster("Y");
		y.setTheme("labeledvalueadjuster");
		z = new LabeledValueAdjuster("Z");
		z.setTheme("labeledvalueadjuster");
		w = new LabeledValueAdjuster("W");
		w.setTheme("labeledvalueadjuster");
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");
		
		// Group for holding the Horizontal alignment of the buttons
		Group h_grid = dialoglayout.createParallelGroup();
		// Group for holding the Vertical alignment of the buttons
		Group v_grid = dialoglayout.createSequentialGroup();
		// Generic row up buttons
		Group row;

		//horizontal
		row = dialoglayout.createParallelGroup(
			label,
			x,
			y,
			z,
			w
		);
		h_grid.addGroup(row);
		//Reset temp row for vertical
		row = dialoglayout.createSequentialGroup(
			label,
			x,
			y,
			z,
			w
		);
		v_grid.addGroup(row);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(300, 150);
		
		this.add(dialoglayout);
		
		addCallbacks();
	}
	
	public void addCallbacks() {
		x.addActionListener(this);
		y.addActionListener(this);
		z.addActionListener(this);
		w.addActionListener(this);
	}
	
	public void setLabel(String name) {
		label.setText(name);
	}
	
	public void setValue(Vector4f value) {
		x.setValue(value.x);
		y.setValue(value.y);
		z.setValue(value.z);
		w.setValue(value.w);
	}
	public float getXValue() {
		return x.getValue();
	}
	public float getYValue() {
		return y.getValue();
	}
	public float getZValue() {
		return z.getValue();
	}
	public float getWValue() {
		return w.getValue();
	}	
	public Vector4f getVector() {
		return new Vector4f(x.getValue(),y.getValue(),z.getValue(),w.getValue());
	}
	
	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent() {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent();
	}
}
