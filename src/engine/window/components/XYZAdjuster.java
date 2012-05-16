package engine.window.components;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;


public class XYZAdjuster extends Widget {
	private DialogLayout dialoglayout;
	private Label label;
	private SlidingValueAdjuster x;
	private SlidingValueAdjuster y;
	private SlidingValueAdjuster z;

	public XYZAdjuster(String name) {
		this.setTheme("rgbaadjuster");
		label = new Label(name);
		label.setTheme("label");
		x = new SlidingValueAdjuster("X");
		x.setTheme("slidingvalueadjuster");
		y = new SlidingValueAdjuster("Y");
		y.setTheme("slidingvalueadjuster");
		z = new SlidingValueAdjuster("Z");
		z.setTheme("slidingvalueadjuster");

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
			z
		);
		h_grid.addGroup(row);
		//Reset temp row for vertical
		row = dialoglayout.createSequentialGroup(
			label,
			x,
			y,
			z
		);
		v_grid.addGroup(row);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(300, 125);
		
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
	public Vector3f getVector() {
		return new Vector3f(x.getValue(),y.getValue(),z.getValue());
	}
}
