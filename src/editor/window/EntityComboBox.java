package editor.window;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.Engine;
import engine.entity.Entity;
import engine.window.components.ComboBox;
import engine.window.tree.Model;

public class EntityComboBox extends ComboBox<String> implements ActionListener {
	private final Engine engine;
	private Entity ent;
	
	public EntityComboBox(Engine engine, String name) {
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

	public EntityComboBox(Model m, Engine engine) {
		super();
		this.engine = engine;
	}
	
	public void copyEntity(String value) {
		ent = engine.getEntity(value);
	}
	
	public Entity getEntity() {
		return ent;
	}
	
	public void createEntityList() {
		this.removeAllItems();
		for(Entity e : engine.getEntityList()) {
			this.addItem((String)e.getProperty(Entity.NAME));
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		createEntityList();
	}
}
