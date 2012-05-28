package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.Label;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.render.ubos.Light;
import engine.window.components.RGBAAdjuster;
import engine.window.components.SlidingValueAdjuster;
import engine.window.components.Window;
import engine.window.components.XYZAdjuster;
import engine.window.components.XYZWAdjuster;

public class LightMenu extends Window implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	
    private XYZWAdjuster position;
    private RGBAAdjuster ambient;
    private RGBAAdjuster diffuse;
    private RGBAAdjuster specular;
    private SlidingValueAdjuster constant_attenuation;
    private SlidingValueAdjuster linear_attenuation;
    private SlidingValueAdjuster quadratic_attenuation;
    private XYZAdjuster spot_direction;
    private SlidingValueAdjuster spot_cutoff;
    private SlidingValueAdjuster spot_exponent;
    private Label num_lights;
    private Label light_index;
	
    private Light light;
    
	public LightMenu() {
		super();
		light = new Light();
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Material Menu");

		this.setTheme("materialmenu");
		
		position = new XYZWAdjuster("Position");
		position.setTheme("xyzwadjuster");
		ambient = new RGBAAdjuster("Ambient");
		ambient.setTheme("rgbaadjuster");
		diffuse = new RGBAAdjuster("Diffuse");
		diffuse.setTheme("rgbaadjuster");
		specular = new RGBAAdjuster("Specular");
		specular.setTheme("rgbaadjuster");
		constant_attenuation = new SlidingValueAdjuster("Const Att");
		constant_attenuation.setTheme("slidingvalueadjuster");
	    linear_attenuation = new SlidingValueAdjuster("LinearAtt");
	    linear_attenuation.setTheme("slidingvalueadjuster");
	    quadratic_attenuation = new SlidingValueAdjuster("Quad Att");
	    quadratic_attenuation.setTheme("slidingvalueadjuster");
	    spot_direction = new XYZAdjuster("Direction");
	    spot_direction.setTheme("xyzadjuster");
	    spot_cutoff = new SlidingValueAdjuster("Cutoff");
	    spot_cutoff.setTheme("slidingvalueadjuster");
	    spot_exponent = new SlidingValueAdjuster("Exponent");
	    spot_exponent.setTheme("slidingvalueadjuster");
	    num_lights = new Label("NumLights");
	    num_lights.setTheme("label");
	    light_index = new Label("LightNum");
	    light_index.setTheme("label");
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");

		// Reset temp row for vertical
		Group h_grid = dialoglayout.createParallelGroup(
			position,
			ambient,
			diffuse,
			specular,
		    constant_attenuation,
		    linear_attenuation,
		    quadratic_attenuation,
		    spot_direction,
		    spot_cutoff,
		    spot_exponent,
		    num_lights,
		    light_index
		);
		
		Group v_grid = dialoglayout.createSequentialGroup(
			position,
			ambient,
			diffuse,
			specular,
		    constant_attenuation,
		    linear_attenuation,
		    quadratic_attenuation,
		    spot_direction,
		    spot_cutoff,
		    spot_exponent,
		    num_lights,
		    light_index
		);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(350,800);
		
		//this.add(layout);
		this.add(dialoglayout);
		
		addCallbacks();
	}
	
	private void addCallbacks() {
		position.addActionListener(this);
		ambient.addActionListener(this);
		diffuse.addActionListener(this);
		specular.addActionListener(this);
	    constant_attenuation.addActionListener(this);
	    linear_attenuation.addActionListener(this);
	    quadratic_attenuation.addActionListener(this);
	    spot_direction.addActionListener(this);
	    spot_cutoff.addActionListener(this);
	    spot_exponent.addActionListener(this);
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
		updateLight();
	}
	
	public Light getLight() {
		return light;
	}
	public void updateLight() {
		light.setLight(
			position.getVector(),
			ambient.getVector(),
			diffuse.getVector(),
			specular.getVector(),
		    constant_attenuation.getValue(),
		    linear_attenuation.getValue(),
		    quadratic_attenuation.getValue(),
		    spot_direction.getVector(),
		    spot_cutoff.getValue(),
		    spot_exponent.getValue()
		);
	}
	public void setLight(Light light) {
		position.setValue(light.getPosition());
		ambient.setValue(light.getAmbient());
		diffuse.setValue(light.getDiffuse());
		specular.setValue(light.getSpecular());
	    constant_attenuation.setValue(light.getConstantAttenuation());
	    linear_attenuation.setValue(light.getLinearAttenuation());
	    quadratic_attenuation.setValue(light.getQuadraticAttenuation());
	    spot_direction.setValue(light.getSpotDirection());
	    spot_cutoff.setValue(light.getSpotCutoff());
	    spot_exponent.setValue(light.getSpotExponent());
	    
	    this.light = light;
	}
	
	
}
