package engine.window.components.datamodel;

import de.matthiasmann.twl.model.HasCallback;
import de.matthiasmann.twl.model.StringModel;
import engine.entity.Entity;

public class StringModelWithEntCallbacks extends HasCallback implements StringModel {
    private Entity ent;
    private String property;

    public StringModelWithEntCallbacks(Entity ent, String property) {
        this.ent = ent;
        this.property = property;
    }

    public String getValue() {
    	return (String)ent.getProperty(property);
    }

    public void setValue(String value) {
        if(value == null) {
            throw new NullPointerException("value");
        }

        System.out.println("NOOOO");
    	ent.setProperty(property, value);
        doCallback();
    }    
}
