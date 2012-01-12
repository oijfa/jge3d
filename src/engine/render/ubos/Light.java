package engine.render.ubos;

import java.nio.ByteBuffer;
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
    private static final int size = 24;
    private static final String name = "light";
    private static final String names[] = {
	    "Lights.light.position",
	    "Lights.light.ambient",
	    "Lights.light.diffuse",
	    "Lights.light.specular",
	    "Lights.light.constant_attenuation",
	    "Lights.light.linear_attenuation",
	    "Lights.light.quadratic_attenuation",
	    "Lights.light.spot_direction",
	    "Lights.light.spot_cutoff",
	    "Lights.light.spot_exponent"
    };
    
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
		float light_buffer[] = new float[size];

		//position
		light_buffer[0] = position.x;
		light_buffer[1] = position.y;
		light_buffer[2] = position.z;
		light_buffer[3] = position.w;

		//ambient
		light_buffer[4] = ambient.x;
		light_buffer[5] = ambient.y;
		light_buffer[6] = ambient.z;
		light_buffer[7] = ambient.w;

		//diffuse
		light_buffer[8] = diffuse.x;
		light_buffer[9] = diffuse.y;
		light_buffer[10] = diffuse.z;
		light_buffer[11] = diffuse.w;
		
		//specular
		light_buffer[12] = specular.x;
		light_buffer[13] = specular.y;
		light_buffer[14] = specular.z;
		light_buffer[15] = specular.w;

		//attenuation
		light_buffer[16] = constantAtt;
		light_buffer[17] = linearAtt;
		light_buffer[18] = quadraticAtt;
	    
		//spot direction
		light_buffer[19] = spotDirection.x;
		light_buffer[20] = spotDirection.y;
		light_buffer[21] = spotDirection.z;
	    
		//shininess
	    light_buffer[22] = spotExponent;
	    light_buffer[23] = spotCutoff;
	    
		return FloatBuffer.wrap(light_buffer);
	}
	
	public int getSize() {
		return size;
	}
	
	public static String[] getNames() {
		return names;
	}
	
	public ByteBuffer getNamesAsBuffer() {
		String name_buffer = new String();
		for(String name: names) {
			name_buffer += name;
		} 
		return ByteBuffer.wrap(name_buffer.getBytes());
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
