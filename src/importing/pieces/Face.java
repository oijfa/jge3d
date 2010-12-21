package importing.pieces;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	/*Getters*/
	public float[] getNorm(){return normal;}

	public void draw() {
		// TODO Auto-generated method stub
		
	}
}
