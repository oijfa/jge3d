package engine.window.components.datamodel;

import de.matthiasmann.twl.model.AbstractIntegerModel;
import engine.entity.Entity;

public class IntegerModelWithEntCallbacks extends AbstractIntegerModel {
    private Entity ent;
    private String property;
    private int minValue;
    private int maxValue;

    public IntegerModelWithEntCallbacks(int minValue, int maxValue, Entity ent, String property) {
    	this.ent = ent;
        this.property = property;
        
        if(minValue > maxValue) {
            throw new IllegalArgumentException("minValue > maxValue");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getValue() {
        return (Integer)ent.getProperty(property);
    }

    public void setValue(int value) {
        ent.setProperty(property, value);
    }
}