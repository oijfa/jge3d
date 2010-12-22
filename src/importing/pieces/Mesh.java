package importing.pieces;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

public class Mesh {
	ArrayList<Face> faces;
	Material mat;
	float[] location;
	float[] forward;
	float[] up;
	
	public Mesh(){
		faces = new ArrayList<Face>();
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Face[] _faces){
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Material m){
		faces = new ArrayList<Face>();
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Face[] _faces, Material m){
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(float[] loc){
		faces = new ArrayList<Face>();
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Face[] _faces, float[] loc){
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Material m, float[] loc){
		faces = new ArrayList<Face>();
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	public Mesh(Face[] _faces, Material m, float[] loc){
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		faces = (ArrayList<Face>) Arrays.asList(_faces );
		mat = m;
		for(int i = 0; i < 3; i++){
			location[i] = loc[i];
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	
	//Copy Constructor
	public Mesh(Mesh m) {
		this.faces = new ArrayList<Face>();
		for(Face f : m.faces){
			this.faces.add(new Face(f));
		}
		
		this.location = new float[3];
		this.forward = new float[3];
		this.up = new float[3];
		for(int i = 0; i < 3; i++ ){
			this.location[i] = m.location[i];
			this.forward[i] = m.forward[i];
			this.up[i] = m.up[i];
		}
		
		this.mat = new Material(m.mat);
	}
	
	public Mesh(ArrayList<Face> _faces) {
		location = new float[3];
		forward = new float[3];
		up = new float[3];
		
		faces = _faces;
		mat = null;
		for(int i = 0; i < 3; i++){
			location[i] = 0.0f;
			forward[i] = 0.0f;
			up[i] = 0.0f;
		}
		forward[2] = 1.0f;
		up[1] = 1.0f;
	}
	/*Setters*/
	public void setMaterial(Material m){ mat = m; }
	public void addFace(Face f){ faces.add(f); }
	public void transform(float[] _location, float[] _forward, float[] _up) throws Exception{
		if( _location.length == 3 && _forward.length == 3 && _up.length == 3){
			this.location = _location;
			this.forward = _forward;
			this.up = _up;
		}else{
			Exception e = new Exception();
			e.initCause(new Throwable("Vectors were not given to transform for a mesh."));
			throw e;
		}
	}
	
	/*Getters*/
	public Material getMaterial(){ return mat; }
	public Face getFace(int i){ return faces.get(i); }
	
	public void draw(){
		//Set Material
		
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, mat.getAmbientAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, mat.getDiffuseAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, mat.getSpecularAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, mat.getEmissionAsBuffer());
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, mat.getShine());
		
		//Transform
		GL11.glTranslatef(location[0], location[1], location[2]);
		//TODO:  Rotate somehow based on forward and up vectors?
		//GL11.glRotatef(angle, x, y, z);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		//Draw faces
		for(Face f : faces){
			f.draw();
		}
		GL11.glEnd();
	}
}
