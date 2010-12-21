package importing.pieces;

public class Material {
	//For all of these:  x = red, y = green, b = blue
	private float[] ambientReflect; 	
	private float[] diffuseReflect;	
	private float[] specularReflect;	//default 0.0f, 0.0f, 0.0f
	private float[] emission; //default 0.0f, 0.0f, 0.0f
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
	
	public float[] getAmbient() {
		return ambientReflect;
	}
	
	public float[] getDiffuse() {
		return diffuseReflect;
	}
	
	public float[] getSpecular() {
		return specularReflect;
	}
	
	public float[] getEmission() {
		return emission;
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
}
