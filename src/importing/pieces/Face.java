//TODO: In draw function, do something with normals

package importing.pieces;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

public class Face {
	ArrayList<float[]> vertices;
	ArrayList<float[]> vertexNormals;
	float[] normal;
	
	public Face(){ 
		vertices = new ArrayList<float[]>();
		vertexNormals = new ArrayList<float[]>();
		normal =  new float[3];
		normal[0] = 0.0f;
		normal[1] = 0.0f;
		normal[2] = 1.0f;
	}
	
	public Face(float[][] verts, float[][] vertnorms, float[] norm){
		for(int i = 0; i < verts.length; i++){
			vertices.add(verts[i]);
		}
		for(int i = 0; i < vertnorms.length; i++){
			vertexNormals.add(vertnorms[i]);
		}
		normal = norm;
	}
	
	public Face(ArrayList<float[]> verts, ArrayList<float[]> vertnorms, float[] norm){
		vertices = verts;
		vertexNormals = vertnorms;
		normal = norm;
	}
	
	public Face(float[][] verts, float[][] vertnorms){
		vertices = (ArrayList<float[]>) Arrays.asList(verts);
		vertexNormals = (ArrayList<float[]>) Arrays.asList(vertnorms);
		normal =  new float[3];
		normal[0] = 0.0f;
		normal[1] = 0.0f;
		normal[2] = 1.0f;
	}

	//Copy Constructor
	public Face(Face f) {
		this.vertices =  new ArrayList<float[]>();
		vertexNormals =  new ArrayList<float[]>();
		this.normal = new float[3];
		
		for(@SuppressWarnings("unused") float[] fa: f.vertices){
			this.vertices.add(new float[3]);
		}
		for(@SuppressWarnings("unused") float[] fa: f.vertexNormals){
			this.vertexNormals.add(new float[3]);
		}
		
		for(int i = 0; i < 3; i++){
			this.normal[i] = f.normal[i];
			for(int j = 0; j < f.vertices.size(); j++){
				(this.vertices.get(j))[i] = (f.vertices.get(j))[i]; 
			}
			for(int j = 0; j < f.vertexNormals.size(); j++){
				(this.vertexNormals.get(j))[i] = (f.vertexNormals.get(j))[i];
			}
		}
	}

	/*Setters*/
	public void setVertices(float[][] verts){
		for(int i = 0; i < verts.length; i++){
			vertices.add(verts[i]);
		}
	}
	public void setVertexNormals(float[][] vertnorms){
		for(int i = 0; i < vertnorms.length; i++){
			vertexNormals.add(vertnorms[i]);
		}
	}
	public void setVertices(ArrayList<float[]> verts){
		vertices = verts;
	}
	public void setVertexNormals(ArrayList<float[]> vertnorms){
		vertexNormals = vertnorms;
	}
	public void setNorm(float[] norm){normal = norm;}
	public void addVertex(float[] fs){vertices.add(fs);}
	public void addVertexNorm(float[] fs){vertexNormals.add(fs);}
	
	/*Getters*/
	public float[] getNorm(){return normal;}

	public void draw() {
		
		for(int i = 0; i < vertices.size(); i++){
			float[] f = vertices.get(i);
			float[] n = vertexNormals.get(i);
			GL11.glVertex3f(f[0], f[1], f[2]);
			GL11.glNormal3f(n[0], n[1], n[2]);
		}
	}
	
	/*Debug*/
	public String toString(){
		String ret = "";
		for(int i = 0; i < vertices.size(); i++){
			ret += "			vert " + String.valueOf(i) + ": (" + vertices.get(i)[0] + "," + vertices.get(i)[1] + "," + vertices.get(i)[2] + ")\n";
		}
		for(int i = 0; i < vertexNormals.size(); i++){
			ret += "			vert norm " + String.valueOf(i) + ": (" + vertexNormals.get(i)[0] + "," + vertexNormals.get(i)[1] + "," + vertexNormals.get(i)[2] + ")\n";
		}
		ret += "			normal: (" + normal[0] + "," + normal[1] + "," + normal[2] + ")\n";	
		return ret;
	}
}
