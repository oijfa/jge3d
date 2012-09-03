package engine.window.components.datamodel;

import de.matthiasmann.twl.model.AbstractFloatModel;
import engine.entity.Entity;

public class FloatModelWithEntCallbacks extends AbstractFloatModel {
    private Entity ent;
    private String property;
    private float minValue;
    private float maxValue;

    public FloatModelWithEntCallbacks(float minValue, float maxValue, Entity ent, String property) {
    	this.ent = ent;
        this.property = property;
        
        if(Float.isNaN(minValue)) {
            throw new IllegalArgumentException("minValue is NaN");
        }
        if(Float.isNaN(maxValue)) {
            throw new IllegalArgumentException("maxValue is NaN");
        }
        if(minValue > maxValue) {
            throw new IllegalArgumentException("minValue > maxValue");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getValue() {
        return (Float)ent.getProperty(property);
    }

    public void setValue(float value) {
        ent.setProperty(property, value);
    }
}