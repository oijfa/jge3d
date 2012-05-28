package engine.window.components;

import java.util.ArrayList;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.FloatModel;
import de.matthiasmann.twl.model.SimpleFloatModel;
import de.matthiasmann.twl.utils.TintAnimator;
import de.matthiasmann.twl.utils.TintAnimator.TimeSource;
import editor.action_listener.ActionListener;
import editor.action_listener.ActionEvent;

public class LabeledValueAdjuster extends Widget implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private ValueAdjusterFloat value_adjuster;
	private Label label;
	private static float min = Float.MIN_VALUE;
	private static float max = Float.MAX_VALUE;
	private Color base_color;
	
	public LabeledValueAdjuster(String name) {
		super();
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");
		
		this.setTheme("labeledvalueadjuster");
		action_listeners = new ArrayList<ActionListener>();
		
		label = new Label(name);
		label.setTheme("label");

		value_adjuster = new ValueAdjusterFloat();
		value_adjuster.setTheme("valueadjuster");
		
		FloatModel va_model = new SimpleFloatModel(min,max,0);
		value_adjuster.setModel(va_model);
				
		dialoglayout.setSize(350, 25);
		
		createCallbacks();
		createLayout();
	}

	private void createCallbacks() {
		value_adjuster.getModel().addCallback(new Runnable() {
			@Override
			public void run() {
				fireActionEvent();
			}
		});
	}

	public void setColor(Color color) {
		this.base_color = color;
		super.setTintAnimator(new TintAnimator(new TimeSource() {
			@Override
			public void resetTime() {
			}

			@Override
			public int getTime() {
				return 0;
			}
		}));
		this.getTintAnimator().setColor(base_color);
	}
	
	private void createLayout() {
		// Create the horizontal rows
		Group h_grid = dialoglayout.createSequentialGroup(label,value_adjuster);
		
		// Create the horizontal rows
		Group v_grid = dialoglayout.createParallelGroup(label,value_adjuster);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(300, 25);
		
		this.add(dialoglayout);
	}
	
	public float getValue() {
		return value_adjuster.getValue();
	}
	
	public void setValue(float value) {
		value_adjuster.setValue(value);
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
