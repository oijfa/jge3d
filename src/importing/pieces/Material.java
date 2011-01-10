//TODO: Remove reference variable, its useless
package importing.pieces;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Material {
	//For all of these:  x = red, y = green, b = blue
	private float[] ambientReflect; 	
	private float[] diffuseReflect;	
	private float[] specularReflect;	//default 0.0f, 0.0f, 0.0f
	private float[] emission; //default 0.0f, 0.0f, 0.0f
	static private FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
	private float alpha; //default 1.0f
	private float shine; //default 0.0f
	private int reference;
	
	public Material()
	{
		ambientReflect = new float[3];
		diffuseReflect = new float[3];
		
		specularReflect = new float[3];
		
		emission = new float[3];	
		
		alpha = 1.0f;
		shine = 0.0f;
	}
	
	public Material(float[] ambient, float[] diffuse)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		specularReflect = new float[3];
		
		emission = new float[3];
		
		for(int i = 0; i < 3; i++){
			specularReflect[i] = 0.0f;
			emission[i] = 0.0f;
		}
		
		alpha = 1.0f;
		
		shine = 0.0f;
	}
	
	public Material( float[] ambient, float[] diffuse, float[] specular)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else{
			specularReflect = new float[3];
			for(int i = 0; i < 3; i++){
				specularReflect[i] = 0.0f;
			}
		}
		
		emission = new float[3];
		for(int i = 0; i < 3; i++){
			emission[i] = 0.0f;
		}
		
		alpha = 1.0f;
		
		shine = 0.0f;
	}
	
	public Material( float[] ambient, float[] diffuse, float[] specular, float[] emis)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else{
			specularReflect = new float[3];
			for(int i = 0; i < 3; i++){
				specularReflect[i] = 0.0f;
			}
		}
		
		if( emis != null)
			emission = emis;
		else{
			emission = new float[3];
			for(int i = 0; i < 3; i++){
				emission[i] = 0.0f;
			}
		}
		
		alpha = 1.0f;
		
		shine = 0.0f;
	}
	
	public Material( float[] ambient, float[] diffuse, float[] specular, float[] emis, float a)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else{
			specularReflect = new float[3];
			for(int i = 0; i < 3; i++){
				specularReflect[i] = 0.0f;
			}
		}
		
		if( emis != null)
			emission = emis;
		else{
			emission = new float[3];
			for(int i = 0; i < 3; i++){
				emission[i] = 0.0f;
			}
		}
		
		if( !Float.isNaN(a) )
			alpha = a;
		else
			alpha = 1.0f;
		
		shine = 0.0f;
	}
	
	public Material( float[] ambient, float[] diffuse, float[] specular, float[] emis, float a, float s)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else{
			specularReflect = new float[3];
			for(int i = 0; i < 3; i++){
				specularReflect[i] = 0.0f;
			}
		}
		
		if( emis != null)
			emission = emis;
		else{
			emission = new float[3];
			for(int i = 0; i < 3; i++){
				emission[i] = 0.0f;
			}
		}
		
		if( !Float.isNaN(a) )
			alpha = a;
		else
			alpha = 1.0f;
		
		if( !Float.isNaN(s) )
			shine = s;
		else
			shine = 0.0f;
	}
	
	//Deep Copy Constructor
	public Material(Material mat) {
		this.ambientReflect = new float[3]; 	
		this.diffuseReflect = new float[3];	
		this.specularReflect = new float[3];
		this.emission = new float[3]; 
		for(int i = 0; i < 3; i++){
			this.ambientReflect[i] = mat.ambientReflect[i];
			this.diffuseReflect[i] = mat.diffuseReflect[i];
			this.specularReflect[i] = mat.specularReflect[i];
			this.emission[i] = mat.emission[i];
		}
		this.alpha = mat.alpha;
		this.shine = mat.shine;
	}

	public float[] getAmbient() {
		return ambientReflect;
	}
	
	public FloatBuffer getAmbientAsBuffer(){
		buffer.clear();
		buffer.put(ambientReflect);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
	
	
	public float[] getDiffuse() {
		return diffuseReflect;
	}
	public FloatBuffer getDiffuseAsBuffer(){
		buffer.clear();
		buffer.put(diffuseReflect);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
	
	public float[] getSpecular() {
		return specularReflect;
	}
	public FloatBuffer getSpecularAsBuffer(){
		buffer.clear();
		buffer.put(specularReflect);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
	
	
	public float[] getEmission() {
		return emission;
	}
	public FloatBuffer getEmissionAsBuffer(){
		buffer.clear();
		buffer.put(emission);
		buffer.put(1.0f);
		buffer.flip();
		return buffer;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public float getShine() {
		return shine;
	}
	
	public int getReference()
	{
		return reference;
	}
	
	public void setEmission(float[] emiss)
	{
		emission = emiss;
	}
	
	public void setAlpha( float alph )
	{
		alpha = alph;
	}
	
	public void setAlpha( float[] alph )
	{
			alpha = alph[0];
	}
	
	public void setShine( float _shine)
	{
		shine = _shine;
	}
	
	public void setShine( float[] _shine)
	{
		shine = _shine[0];
	}

	public void setAmbient(float[] floats) {
		ambientReflect = floats;
	}
	
	public void setDiffuse(float[] d)
	{
		diffuseReflect = d;
	}
	
	public void setSpecular(float[] s)
	{
		specularReflect = s;
	}
	
	public String toString(){
		String ret = "";
		ret += "			ambientReflect: (" + ambientReflect[0] + "," + ambientReflect[1] + "," + ambientReflect[2] + ")\n";
		ret += "			diffuseReflect: (" + diffuseReflect[0] + "," + diffuseReflect[1] + "," + diffuseReflect[2] + ")\n";
		ret += "			specularReflect: (" + specularReflect[0] + "," + specularReflect[1] + "," + specularReflect[2] + ")\n";
		ret += "			emission: (" + emission[0] + "," + emission[1] + "," + emission[2] + ")\n";
		ret += "			alpha: " + String.valueOf(alpha) + "\n";
		ret += "			shine: " + String.valueOf(shine) + "\n";
		return ret;
	}
	public StringBuffer toXGLString(int ref){
		return null;
	}
}
