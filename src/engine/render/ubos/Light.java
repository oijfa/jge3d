package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class Light implements UBOInterface {
    private Vector4f position;
    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;
    private float constantAtt;
    private float linearAtt;
    private float quadraticAtt;
    private Vector3f spotDirection;
    private float spotExponent;
    private float spotCutoff;
    private int array_index = 0;
    private static final int size = 24;
    private static final String name = "Lights";
    
	public Light(
		Vector4f position,
    	Vector4f ambient,
    	Vector4f diffuse,
    	Vector4f specular,
    	float constantAtt,
    	float linearAtt,
    	float quadraticAtt,
    	Vector3f spotDirection,
    	float spotExponent,
    	float spotCutoff) {
			this.position=position;
			this.ambient=ambient;
			this.diffuse=diffuse;
			this.specular=specular;
			this.constantAtt=constantAtt;
			this.linearAtt=linearAtt;
			this.quadraticAtt=quadraticAtt;
			this.spotDirection=spotDirection;
			this.spotExponent=spotExponent;
			this.spotCutoff=spotCutoff;
	}
	
	public FloatBuffer createBuffer() {
		FloatBuffer buf = BufferUtils.createFloatBuffer(size);
		
		//position
		buf.put(position.x);
		buf.put(position.y);
		buf.put(position.z);
		buf.put(position.w);

		//ambient
		buf.put(ambient.x);
		buf.put(ambient.y);
		buf.put(ambient.z);
		buf.put(ambient.w);

		//diffuse
		buf.put(diffuse.x);
		buf.put(diffuse.y);
		buf.put(diffuse.z);
		buf.put(diffuse.w);
		
		//specular
		buf.put(specular.x);
		buf.put(specular.y);
		buf.put(specular.z);
		buf.put(specular.w);

		//attenuation
		buf.put(constantAtt);
		buf.put(linearAtt);
		buf.put(quadraticAtt);
	    
		//spot direction
		buf.put(spotDirection.x);
		buf.put(spotDirection.y);
		buf.put(spotDirection.z);
	    
		//shininess
	    buf.put(spotExponent);
	    buf.put(spotCutoff);
	    
	    buf.flip();
	    
		return buf;
	}
	
	public int getSize() {
		return size;
	}
	
	public String[] getNames() {
		String names[] = {
		    "Lights.light[" + array_index + "].position",
		    "Lights.light[" + array_index + "].ambient",
		    "Lights.light[" + array_index + "].diffuse",
		    "Lights.light[" + array_index + "].specular",
		    "Lights.light[" + array_index + "].constant_attenuation",
		    "Lights.light[" + array_index + "].linear_attenuation",
		    "Lights.light[" + array_index + "].quadratic_attenuation",
		    "Lights.light[" + array_index + "].spot_direction",
		    "Lights.light[" + array_index + "].spot_cutoff",
		    "Lights.light[" + array_index + "].spot_exponent"
	    };
		
		return names;
	}
	
	public IntBuffer getIndices() {
		return BufferUtils.createIntBuffer(size);
	}

	public Type getType() {
		return Type.LIGHT;
	}
	
	public String getName() {
		return name;
	}
}
