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
    //private static final int size = 24;
    private static final String name = "lights";
        
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
    	)
	{
		this.position=new Vector4f(position);
		this.ambient=new Vector4f(ambient);
		this.diffuse=new Vector4f(diffuse);
		this.specular=new Vector4f(specular);
		this.constant_attenuation=new Float(constant_attenuation);
		this.linear_attenuation=new Float(linear_attenuation);
		this.quadratic_attenuation=new Float(quadratic_attenuation);
		this.spot_direction=new Vector3f(spot_direction);
		this.spot_cutoff=new Float(spot_cutoff);
		this.spot_exponent=new Float(spot_exponent);
	}
		
	public FloatBuffer createBuffer(int block_size, IntBuffer offsets) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(block_size/4);

		float[] copy_array = new float[block_size/4];
		
		int i=0;
		copy_array[offsets.get(i)/4 + 0] = position.x;
		copy_array[offsets.get(i)/4 + 1] = position.y;
		copy_array[offsets.get(i)/4 + 2] = position.z;
		copy_array[offsets.get(i)/4 + 3] = position.w;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = ambient.x;
		copy_array[offsets.get(i)/4 + 1] = ambient.y;
		copy_array[offsets.get(i)/4 + 2] = ambient.z;
		copy_array[offsets.get(i)/4 + 3] = ambient.w;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = diffuse.x;
		copy_array[offsets.get(i)/4 + 1] = diffuse.y;
		copy_array[offsets.get(i)/4 + 2] = diffuse.z;
		copy_array[offsets.get(i)/4 + 3] = diffuse.w;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = specular.x;
		copy_array[offsets.get(i)/4 + 1] = specular.y;
		copy_array[offsets.get(i)/4 + 2] = specular.z;
		copy_array[offsets.get(i)/4 + 3] = specular.w;
	
		i++;
		copy_array[offsets.get(i)/4 + 0] = constant_attenuation;
	
		i++;
		copy_array[offsets.get(i)/4 + 0] = linear_attenuation;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = quadratic_attenuation;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = spot_direction.x;
		copy_array[offsets.get(i)/4 + 1] = spot_direction.y;
		copy_array[offsets.get(i)/4 + 2] = spot_direction.z;

		i++;
		copy_array[offsets.get(i)/4 + 0] = spot_cutoff;

		i++;
		copy_array[offsets.get(i)/4 + 0] = spot_exponent;
		
		buf.put(copy_array);
		buf.flip();

		return buf;
	}
	
	public int getSize() {
		return getNames().length;
	}
	
	public String[] getNames() {
		String names[] = {
		    "light["+array_index+"].position",
		    "light["+array_index+"].ambient",
		    "light["+array_index+"].diffuse",
		    "light["+array_index+"].specular",
		    "light["+array_index+"].constant_attenuation",
		    "light["+array_index+"].linear_attenuation",
		    "light["+array_index+"].quadratic_attenuation",
		    "light["+array_index+"].spot_direction",
		    "light["+array_index+"].spot_cutoff",
		    "light["+array_index+"].spot_exponent"
	    };
		
		return names;
	}

	public Type getType() {
		return Type.LIGHT;
	}
	
	public String getName() {
		return name;
	}
}
