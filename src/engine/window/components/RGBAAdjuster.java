package engine.window.components;

import java.util.ArrayList;

import javax.vecmath.Vector4f;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;

public class RGBAAdjuster extends Widget implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private Label label;
	private SlidingValueAdjuster r;
	private SlidingValueAdjuster g;
	private SlidingValueAdjuster b;
	private SlidingValueAdjuster a;

	public RGBAAdjuster(String name) {
		action_listeners = new ArrayList<ActionListener>();
		this.setTheme("rgbaadjuster");
		label = new Label(name);
		label.setTheme("label");
		r = new SlidingValueAdjuster("R");
		r.setTheme("slidingvalueadjuster");
		g = new SlidingValueAdjuster("G");
		g.setTheme("slidingvalueadjuster");
		b = new SlidingValueAdjuster("B");
		b.setTheme("slidingvalueadjuster");
		a = new SlidingValueAdjuster("A");
		a.setTheme("slidingvalueadjuster");
		
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
			r,
			g,
			b,
			a
		);
		h_grid.addGroup(row);
		//Reset temp row for vertical
		row = dialoglayout.createSequentialGroup(
			label,
			r,
			g,
			b,
			a
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
		r.addActionListener(this);
		g.addActionListener(this);
		b.addActionListener(this);
		a.addActionListener(this);
	}
	
	public void setValue(Vector4f color) {
		r.setValue(color.x);
		g.setValue(color.y);
		b.setValue(color.z);
		a.setValue(color.w);
	}
	
	public void setLabel(String name) {
		label.setText(name);
	}
	public float getR() {
		return r.getValue();
	}
	public float getG() {
		return g.getValue();
	}
	public float getB() {
		return b.getValue();
	}
	public float getA() {
		return a.getValue();
	}	
	public Vector4f getVector() {
		return new Vector4f(r.getValue(),g.getValue(),b.getValue(),a.getValue());
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
		fireActionEvent("rgbaadjuster");
	}
}
