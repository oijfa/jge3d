package engine.window.components;

import java.util.ArrayList;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Scrollbar;
import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.Scrollbar.Orientation;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.FloatModel;
import de.matthiasmann.twl.model.SimpleFloatModel;
import de.matthiasmann.twl.utils.TintAnimator;
import de.matthiasmann.twl.utils.TintAnimator.TimeSource;
import editor.action_listener.ActionListener;
import editor.action_listener.ActionEvent;

public class SlidingValueAdjuster extends Widget implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private Scrollbar h_scroll;
	private DialogLayout dialoglayout;
	private ValueAdjusterFloat value_adjuster;
	private Label label;
	private static int min = 0;
	private static int max = 255;
	private Color base_color;
	
	public SlidingValueAdjuster(String name) {
		super();
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");
		
		this.setTheme("slidingvalueadjuster");
		action_listeners = new ArrayList<ActionListener>();
		
		label = new Label(name);
		label.setTheme("label");
		h_scroll = new Scrollbar(Orientation.HORIZONTAL);
		h_scroll.setTheme("hscrollbar");
		value_adjuster = new ValueAdjusterFloat();
		value_adjuster.setTheme("valueadjuster");
		
		FloatModel va_model = new SimpleFloatModel(min,max,0);
		value_adjuster.setModel(va_model);
		h_scroll.setMinMaxValue(min, max);
				
		createCallbacks();
		createLayout();
	}

	private void createCallbacks() {
		value_adjuster.getModel().addCallback(new Runnable() {
			@Override
			public void run() {
				h_scroll.setValue(Float.valueOf(value_adjuster.getValue()).intValue());
			}
		});
		h_scroll.addCallback(new Runnable() {
			@Override
			public void run() {
				value_adjuster.setValue(h_scroll.getValue());
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
		Group h_grid = dialoglayout.createSequentialGroup(label,h_scroll, value_adjuster);
		
		// Create the horizontal rows
		Group v_grid = dialoglayout.createParallelGroup(label,h_scroll, value_adjuster);
		
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
