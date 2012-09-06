package engine.window.components.datamodel;

import de.matthiasmann.twl.model.BooleanModel;
import de.matthiasmann.twl.model.HasCallback;
import engine.entity.Entity;

public class BooleanModelWithEntCallbacks extends HasCallback implements BooleanModel {
    private Entity ent;
    private String property;

    public BooleanModelWithEntCallbacks(Entity ent, String property) {
    	this.ent = ent;
        this.property = property;
        setValue(getValue());
    }

    @Override
    public boolean getValue() {
        return (Boolean)ent.getProperty(property);
    }

	@Override
	public void setValue(boolean value) {
		ent.setProperty(property, value);
		doCallback();
	}
}