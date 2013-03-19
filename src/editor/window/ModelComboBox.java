package editor.window;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.Engine;
import engine.entity.Entity;

import engine.resource.ResourceManager.ResourceItem;
import engine.window.components.ComboBox;
import engine.window.tree.Model;

public class ModelComboBox extends ComboBox<String> implements ActionListener {
	private final Engine engine;
	private Entity ent;
	
	public ModelComboBox(Engine engine, String name) {
		super();
		this.engine = engine;
		engine.getEntityList().addActionListener(this);

		this.addPropertyChangeListener(new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				copyEntity((String)evt.getNewValue());
			}
		});
	}

	public ModelComboBox(Model m, Engine engine) {
		super();
		this.engine = engine;
	}
	
	public void copyEntity(String value) {
		ent = engine.getEntity(value);
	}
	
	public Entity getEntity() {
		return ent;
	}
	
	public void createModelList() {
		this.removeAllItems();
		for(ResourceItem e : (ArrayList<ResourceItem>)engine.resource_manager.getResourcesInCategory("models")) {
			this.addItem((String)e.name);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		createModelList();
	}
}
