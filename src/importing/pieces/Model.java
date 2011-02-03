//TODO: Maybe add transforms?
package importing.pieces;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import render.Renderer;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

public class Model {
	ArrayList<Mesh> meshes;
	Vector3f max, min, center;
	int vbo_id;
	
	public Model(){
		meshes = new ArrayList<Mesh>();
		init();
	}
	public Model(Mesh[] mesh_array){
		meshes = new ArrayList<Mesh>();
		for(Mesh m: mesh_array){
			meshes.add(m);
		}
		init();
	}
	public Model(ArrayList<Mesh> mesh_array) {
		meshes = new ArrayList<Mesh>();
		for(Mesh m: mesh_array){
			meshes.add(m);
		}
		init();
	}
	
	//Copy Constructor
	public Model(Model model) {
		this.meshes = new ArrayList<Mesh>();
		for(Mesh m: model.meshes){
			this.meshes.add(new Mesh(m));
		}
		init();
		verify();
	}
	
	public void init() {
		max = new Vector3f();
		min = new Vector3f();
		center = new Vector3f();
	}
	
	/*Setters*/
	public void addMesh(Mesh m){meshes.add(m);}
	
	/*Getters*/
	public Mesh getMesh(int i){ return meshes.get(i);}
	
	public int getMeshCount() { return meshes.size();}
	
	public void draw(){
		//http://lwjgl.org/wiki/index.php?title=Using_Vertex_Buffer_Objects_(VBO)
		//if the renderer supports VBOs definitely use them; if it doesn't
		//we fall-back to immediate mode
		if(Renderer.supportsVBO()) {
			for(Mesh m: meshes){
				for(Face f: m.getFaces()) {
					f.draw_vbo();
				}
			}
		} else {
			for(Mesh m: meshes){
		
				GL11.glPushMatrix();
				m.draw();
				GL11.glPopMatrix();
			}
		}
	}
	
	/* Verify */
	//This function will verify whether the file had normals defined
	//and also check for shading groups and normalize if possible
	public void verify() {	
		ArrayList<Vector3f> maxes = new ArrayList<Vector3f>();
		ArrayList<Vector3f> mins = new ArrayList<Vector3f>();
		
		//for each mesh we get the max and min width,height,depth
		//we also calculate normals since we're going through the 
		//whole list anyway
		for(Mesh m: meshes) {
			maxes.add(m.getMaximums());
			mins.add(m.getMinimums());
			m.calcNormals();
		}
		
		max = new Vector3f();
		min = new Vector3f();
		
		//create a vector for making a surrounding shape that
		//matches the largest and smallest points exactly
		max = maxes.get(0);
		min = mins.get(0);
		for(int i = 1; i < maxes.size(); i++){
			if(maxes.get(i).x > max.x){
				max.x = maxes.get(i).x;
			}
			if(maxes.get(i).y > max.y){
				max.y = maxes.get(i).y;
			}
			if(maxes.get(i).z > max.z){
				max.z = maxes.get(i).z;
			}
			
			if(mins.get(i).x < min.x){
				min.x = mins.get(i).x;
			}
			if(mins.get(i).y < min.y){
				min.y = mins.get(i).y;
			}
			if(mins.get(i).z < min.z){
				min.z = mins.get(i).z;
			}
		}	
		//find the center of the model using our min/max values
		center.add(max,min);
		center.scale(0.5f);
		
		//if we support VBOs we need to precompute the thing now
		//that we have normals and the model is fully loaded
		if(Renderer.supportsVBO()) {
			for(Mesh m: meshes) {
				for(Face f: m.getFaces()) {
					f.createFaceBufferVNT();
				}
			}
		}
	}
	
	/*Export*/
	public void saveXGL(String filename){
		StringBuffer data = new StringBuffer();
		
		data.append("<WORLD>\n");
		for(int i = 0; i < meshes.size(); i++){
			data.append(meshes.get(i).toXGLString(i));
		}
		data.append("</WORLD>\n");
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter( filename));
			out.write(data.toString());
			out.close();
			System.out.println("Export succeeded");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("EXPORT FAILED\n\n");
		}
	}
	
	/*Debug*/
	public String toString(){
		String ret = "Model{\n";
		ret += "	# of Meshes:" + meshes.size() + "\n";
		for( Integer i = 0; i < meshes.size(); i++){
			ret += "	Mesh " + i.toString() + "{\n";
			ret += meshes.get(i).toString();
			ret += "	}\n";	
		}
		ret += "}\n";
		return ret;
	}
	
	public CollisionShape createCollisionShape() {
		Vector3f shape = new Vector3f();
		shape.sub(max,min);
		//shape.scale(0.5f);
		return new BoxShape(shape);
	}

	public int getModelID() {
		return vbo_id;
}
	public ArrayList<Float> getColor() {
		return meshes.get(0).getMaterial().getColor();
	}
}
