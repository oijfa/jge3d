package importing.pieces;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

public class Mesh {
	ArrayList<Face> faces;
	Material mat;
	Float[] location;
	
	public Mesh(){
		faces = new ArrayList<Face>();
		location = new Float[3];
		
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
		}
	}
	public Mesh(Face[] _faces){
		location = new Float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
		}
	}
	public Mesh(Material m){
		faces = new ArrayList<Face>();
		location = new Float[3];
		
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
		}
	}
	public Mesh(Face[] _faces, Material m){
		location = new Float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
		}
	}
	public Mesh(Float[] loc){
		faces = new ArrayList<Face>();
		location = new Float[3];
		
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
		}
	}
	public Mesh(Face[] _faces, Float[] loc){
		location = new Float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
		}
	}
	public Mesh(Material m, Float[] loc){
		faces = new ArrayList<Face>();
		location = new Float[3];
		
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
		}
	}
	public Mesh(Face[] _faces, Material m, Float[] loc){
		location = new Float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
		}
	}
	
	/*Setters*/
	public void setMaterial(Material m){ mat = m; }
	public void addFace(Face f){ faces.add(f); }
	
	/*Getters*/
	public Material getMaterial(){ return mat; }
	public Face getFace(int i){ return faces.get(i); }
	
	public void draw(){
		//Set Material
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, FloatBuffer.wrap(mat.getAmbient()));
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, FloatBuffer.wrap(mat.getDiffuse()));
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, FloatBuffer.wrap(mat.getSpecular()));
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, FloatBuffer.wrap(mat.getEmission()));
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, mat.getShine());
		
		//Draw faces
		for(Face f : faces){
			f.draw();
		}
	}
}
