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
    	System.out.println("getting");
    	return (String)ent.getProperty(property);
    }

    public void setValue(String value) {
    	System.out.println("getting");
        if(value == null) {
            throw new NullPointerException("value");
        }
        if(!ent.getProperty(property).equals(value)) {
        	ent.setProperty(property, value);
            doCallback();
        } else {
        	System.out.println("fuckbeans");
        }
    }    
}
