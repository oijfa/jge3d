package importing.pieces;

import javax.vecmath.Vector3f;

public class Material {
	//For all of these:  x = red, y = green, b = blue
	private Vector3f ambientReflect; 	
	private Vector3f diffuseReflect;	
	private Vector3f specularReflect;	//default 0.0f, 0.0f, 0.0f
	private Vector3f emission; //default 0.0f, 0.0f, 0.0f
	private float alpha; //default 1.0f
	private float shine; //default 0.0f
	private int reference;
	
	public Material()
	{
		ambientReflect = new Vector3f();
		diffuseReflect = new Vector3f();
		
		specularReflect = new Vector3f();
		
		emission = new Vector3f();	
		
		alpha = 1;
		shine = 0;
	}
	
	public Material( Vector3f ambient, Vector3f diffuse)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		specularReflect = new Vector3f(0,0,0);
		
		emission = new Vector3f(0,0,0);
		
		alpha = 1;
		
		shine = 0;
	}
	
	public Material( Vector3f ambient, Vector3f diffuse, Vector3f specular)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else
			specularReflect = new Vector3f(0,0,0);
		
		emission = new Vector3f(0,0,0);
		
		alpha = 1;
		
		shine = 0;
	}
	
	public Material( Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f emis)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else
			specularReflect = new Vector3f(0,0,0);
		
		if( emis != null)
			emission = emis;
		else
			emission = new Vector3f(0,0,0);
		
		alpha = 1;
		
		shine = 0;
	}
	
	public Material( Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f emis, float a)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else
			specularReflect = new Vector3f(0,0,0);
		
		if( emis != null)
			emission = emis;
		else
			emission = new Vector3f(0,0,0);
		
		if( !Float.isNaN(a) )
			alpha = a;
		else
			alpha = 1;
		
		shine = 0;
	}
	
	public Material( Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f emis, float a, float s)
	{
		ambientReflect = ambient;
		diffuseReflect = diffuse;
		
		if( specular != null)
			specularReflect = specular;
		else
			specularReflect = new Vector3f(0,0,0);
		
		if( emis != null)
			emission = emis;
		else
			emission = new Vector3f(0,0,0);
		
		if( !Float.isNaN(a) )
			alpha = a;
		else
			alpha = 1;
		
		if( !Float.isNaN(s) )
			shine = s;
		else
			shine = 0;
	}
	
	public Vector3f getAmbient() {
		return ambientReflect;
	}
	
	public Vector3f getDiffuse() {
		return diffuseReflect;
	}
	
	public Vector3f getSpecular() {
		return specularReflect;
	}
	
	public Vector3f getEmission() {
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
	
	public void setEmission(Vector3f emiss)
	{
		emission = emiss;
	}
	
	public void setAlpha( float alph )
	{
		alpha = alph;
	}
	
	public void setShine( float _shine)
	{
		shine = _shine;
	}
	
	public void setReference( int i)
	{
		reference = i;
	}

	public void setAmbient(Vector3f a) {
		ambientReflect = a;
	}
	
	public void setDiffuse(Vector3f d)
	{
		diffuseReflect = d;
	}
	
	public void setSpecular(Vector3f s)
	{
		specularReflect = s;
	}
}
