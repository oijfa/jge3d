//TODO: In draw function, do something with normals

package importing.pieces;

import java.util.ArrayList;
import java.util.Arrays;

import javax.vecmath.Vector3f;

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
	
	public Vector3f[] getVertices(){
		ArrayList<Vector3f> vertexlist = new ArrayList<Vector3f>();
		Vector3f vertex = new Vector3f();
		for(int i=0; i<vertices.size();i++) {
			vertex.x = vertices.get(i)[0];
			vertex.y = vertices.get(i)[1];
			vertex.z = vertices.get(i)[2];
			vertexlist.add(vertex);
		}
		
		return (Vector3f[])vertexlist.toArray();
	}
	
	public Vector3f getVertex(int i){
		Vector3f vertex = new Vector3f();
		
		vertex.x = vertices.get(i)[0];
		vertex.y = vertices.get(i)[1];
		vertex.z = vertices.get(i)[2];
		
		return vertex;
	}

	public void draw() {
		/*
		float[] norm = calculateNormal(vertices.get(0),vertices.get(1),vertices.get(2));
		GL11.glNormal3f(norm[0], norm[1], norm[2]);
		*/
		for(int i = 0; i < vertices.size(); i++){
			float[] f = vertices.get(i);
			float[] n = vertexNormals.get(i);
			GL11.glVertex3f(f[0], f[1], f[2]);
			GL11.glNormal3f(n[0], n[1], n[2]);	
		}
	}
	
	@SuppressWarnings("unused")
	private float[] calculateNormal(float[] p1, float[] p2, float[] p3){
		int x = 0;
		int y = 1;
		int z = 2;
		
		float[] U = new float[3];
		float[] V = new float[3];
		float[] norm = new float[3];
		
		for(int i = 0; i < 3; i++){
			U[i] = p2[i] - p3[i];
			V[i] = p3[i] - p1[i];
		}
		
		norm[x] = (U[y]*V[z]) - (U[z]*V[y]);
		norm[y] = (U[z]*V[x]) - (U[x]*V[z]);
		norm[z] = (U[x]*V[y]) - (U[y]*V[x]);
		
		return norm;
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
	
	public int getVertexCount() {
		return vertices.size();
	}
}
