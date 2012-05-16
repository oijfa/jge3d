package editor.window;

import java.util.ArrayList;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.render.ubos.Material;
import engine.window.components.RGBAAdjuster;
import engine.window.components.SlidingValueAdjuster;
import engine.window.components.Window;

public class MaterialMenu extends Window implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private RGBAAdjuster ambient;
	private RGBAAdjuster diffuse;
	private RGBAAdjuster specular;
	private SlidingValueAdjuster shininess;
	private SlidingValueAdjuster alpha;
	private Material material;
	
	public MaterialMenu() {
		super();
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Material Menu");

		this.setTheme("materialmenu");
		
		ambient = new RGBAAdjuster("Ambient");
		ambient.setTheme("rgbaadjuster");
		diffuse = new RGBAAdjuster("Diffuse");
		specular = new RGBAAdjuster("Specular");
		shininess = new SlidingValueAdjuster("Shiny");
		alpha = new SlidingValueAdjuster("Alpha");
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");

		// Reset temp row for vertical
		Group h_grid = dialoglayout.createParallelGroup(
			ambient,
			diffuse,
			specular,
			shininess,
			alpha
		);
		
		Group v_grid = dialoglayout.createSequentialGroup(
			ambient,
			diffuse,
			specular,
			shininess,
			alpha
		);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(300,600);
		
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
		fireActionEvent("material");
	}
	
	public Material getMaterial() {
		return material;
	}
	public void updateMaterial() {
		material.setMaterial(
			ambient.getVector(),
			diffuse.getVector(),
			specular.getVector(),
		    shininess.getValue(),
		    alpha.getValue()
		);
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
}
