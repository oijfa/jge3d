package engine.window.components;

import javax.vecmath.Vector4f;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;


public class XYZWAdjuster extends Widget {
	private DialogLayout dialoglayout;
	private Label label;
	private SlidingValueAdjuster x;
	private SlidingValueAdjuster y;
	private SlidingValueAdjuster z;
	private SlidingValueAdjuster w;

	public XYZWAdjuster(String name) {
		this.setTheme("rgbaadjuster");
		label = new Label(name);
		label.setTheme("label");
		x = new SlidingValueAdjuster("X");
		x.setTheme("slidingvalueadjuster");
		y = new SlidingValueAdjuster("Y");
		y.setTheme("slidingvalueadjuster");
		z = new SlidingValueAdjuster("Z");
		z.setTheme("slidingvalueadjuster");
		w = new SlidingValueAdjuster("W");
		w.setTheme("slidingvalueadjuster");
		
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
	}
	
	public void setLabel(String name) {
		label.setText(name);
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
}
