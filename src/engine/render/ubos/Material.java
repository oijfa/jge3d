package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class Material implements UBOInterface {
    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;
    private float shininess;
    private float alpha;

    private static final String name = "amaterial";
    private static final String names[] = {
	    "amaterial.ambient",
	    "amaterial.diffuse",
	    "amaterial.specular",
	    "amaterial.shininess",
	    "amaterial.alpha"
    };
    
    public Material() {
        ambient = new Vector4f(1f,1f,1f,255f);
    	diffuse = new Vector4f(1f,1f,1f,255f);
    	specular = new Vector4f(1f,1f,1f,255f);
    	shininess = 1f;
    	alpha = 255.0f;
    }
    
    public Material(Material material) {
	    this.ambient=material.ambient;
		this.diffuse=material.diffuse;
		this.specular=material.specular;
		this.shininess=material.shininess;
		this.alpha=material.alpha;
	}
    
	public Material(
    	Vector4f ambient,
    	Vector4f diffuse,
    	Vector4f specular,
    	float shininess,
    	float alpha) {
			this.ambient=ambient;
			this.diffuse=diffuse;
			this.specular=specular;
			this.shininess=shininess;
			this.alpha=alpha;
	}
	
	public FloatBuffer createBuffer(int block_size, IntBuffer offsets) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(block_size/4);
		float[] copy_array = new float[block_size/4];
		
		int i=0;
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
		copy_array[offsets.get(i)/4 + 0] = shininess;
		
		i++;
		copy_array[offsets.get(i)/4 + 0] = alpha;
		/*
		for(int j=0;j<copy_array.length;j++) {
			System.out.println(j + ":" + block_size/4 + ":" + copy_array[j]);
		}
		*/
		
		buf.put(copy_array);
		buf.flip();
		
		return buf;
	}
	
	public int getSize() {
		return getNames().length;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public Type getType() {
		return Type.MATERIAL;
	}
	
	public String getName() {
		return name;
	}
	
	public StringBuffer toXGLString(int ref) {
		StringBuffer data = new StringBuffer();
		data.append("<MAT ID=\"" + ref + "\">\n");
		data.append(
			"<AMB>" + String.valueOf(ambient.x) + ", "
			+ String.valueOf(ambient.y) + ", "
			+ String.valueOf(ambient.z) + "</AMB>\n"
		);
		data.append(
			"<DIFF>" + String.valueOf(diffuse.x) + ", "
			+ String.valueOf(diffuse.y) + ", "
			+ String.valueOf(diffuse.z) + "</DIFF>\n"
		);
		data.append(
			"<SPEC>" + String.valueOf(specular.x) + ", "
			+ String.valueOf(specular.y) + ", "
			+ String.valueOf(specular.z) + "</SPEC>\n"
		);
		/*
		data.append(
			"<EMISS>" + String.valueOf(emission.x) + ", "
			+ String.valueOf(emission.y) + ", " + String.valueOf(emission.z)
			+ "</EMISS>\n"
		);
		*/
		data.append("<SHINE>" + String.valueOf(shininess) + "</SHINE>\n");
		data.append("<ALPHA>" + String.valueOf(alpha) + "</ALPHA>\n");
		data.append("</MAT>\n");
		return data;
	}
	
    public Vector4f getAmbient() {
    	return ambient;
    }
    
    public Vector4f getDiffuse() {
    	return diffuse;
    }
    
    public Vector4f getSpecular() {
    	return specular;
    }
    
    public float getShininess() {
    	return shininess;
    }
    
    public float getAlpha() {
    	return alpha;
    }
    
    public void setAmbient(Vector4f ambient) {
    	this.ambient=ambient;
    }
    
    public void setDiffuse(Vector4f diffuse) {
    	this.diffuse=diffuse;
    }
    
    public void setSpecular(Vector4f specular) {
    	this.specular=specular;
    }
    
    public void setShininess(float shininess) {
    	this.shininess=shininess;
    }
    
    public void setAlpha(float alpha) {
    	this.alpha=alpha;
    }
    
    public FloatBuffer getAmbientAsBuffer() {
		float[] temp = new float[3];
		temp[0] = ambient.x;
		temp[1] = ambient.y;
		temp[2] = ambient.z;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.clear();
		buffer.put(temp);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
    
	public FloatBuffer getDiffuseAsBuffer() {
		float[] temp = new float[3];
		temp[0] = diffuse.x;
		temp[1] = diffuse.y;
		temp[2] = diffuse.z;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.clear();
		buffer.put(temp);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}

	public FloatBuffer getSpecularAsBuffer() {
		float[] temp = new float[3];
		temp[0] = specular.x;
		temp[1] = specular.y;
		temp[2] = specular.z;

		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.clear();
		buffer.put(temp);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
	
	public ArrayList<Float> getFloatColor() {
		ArrayList<Float> color = new ArrayList<Float>();

		color.add(average(ambient.x, diffuse.x, specular.x));
		color.add(average(ambient.y, diffuse.y, specular.y));
		color.add(average(ambient.z, diffuse.z, specular.z));

		return color;
	}
	
	private float average(float a, float b, float c) {
		return (a + b + c) / 3;
	}
	
	public ArrayList<Byte> getColor() {
		ArrayList<Byte> color = new ArrayList<Byte>();

		color.add(
			byteAverage(ambient.x, diffuse.x,specular.x)
		);
		color.add(
			byteAverage(ambient.y, diffuse.y,specular.y)
		);
		color.add(
			byteAverage(ambient.z, diffuse.z,specular.z)
		);

		return color;
	}
	
	private byte byteAverage(float a, float b, float c) {
		return (byte) (((a * 255) + (b * 255) + (c * 255)) / 3);
	}
}
