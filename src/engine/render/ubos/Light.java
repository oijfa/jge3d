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
    private float constant_attenuation;
    private float linear_attenuation;
    private float quadratic_attenuation;
    private Vector3f spot_direction;
    private float spot_cutoff;
    private float spot_exponent;
    private int array_index = 0;
    private static final int size = 24;
    private static final String name = "Lights";
        
	public Light(
			Vector4f position,
	    	Vector4f ambient,
	    	Vector4f diffuse,
	    	Vector4f specular,
	    	float constant_attenuation,
	    	float linear_attenuation,
	    	float quadratic_attenuation,
	    	Vector3f spot_direction,
	    	float spot_cutoff,
	    	float spot_exponent
	    	) {
				this.position=position;
				this.ambient=ambient;
				this.diffuse=diffuse;
				this.specular=specular;
				this.constant_attenuation=constant_attenuation;
				this.linear_attenuation=linear_attenuation;
				this.quadratic_attenuation=quadratic_attenuation;
				this.spot_direction=spot_direction;
				this.spot_cutoff=spot_cutoff;
				this.spot_exponent=spot_exponent;
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
			buf.put(constant_attenuation);
			buf.put(linear_attenuation);
			buf.put(quadratic_attenuation);
		    
			//spot direction
			buf.put(spot_direction.x);
			buf.put(spot_direction.y);
			buf.put(spot_direction.z);
		    
			//shininess
			buf.put(spot_cutoff);
		    buf.put(spot_exponent);
		    
		    buf.flip();
		    
			return buf;
		}
	
	public int getSize() {
		return getNames().length;
	}
	
	public String[] getNames() {
		String names[] = {
		    "light[" + array_index + "].position",
		    "light[" + array_index + "].ambient",
		    "light[" + array_index + "].diffuse",
		    "light[" + array_index + "].specular",
		    "light[" + array_index + "].constant_attenuation",
		    "light[" + array_index + "].linear_attenuation",
		    "light[" + array_index + "].quadratic_attenuation",
		    "light[" + array_index + "].spot_direction",
		    "light[" + array_index + "].spot_cutoff",
		    "light[" + array_index + "].spot_exponent"
	    };
		
		return names;
	}
	
	public IntBuffer getIndices() {
		return IntBuffer.wrap(new int[getNames().length]);
	}

	public Type getType() {
		return Type.LIGHT;
	}
	
	public String getName() {
		return name;
	}
}
