//TODO: Maybe add transforms?
package importing.pieces;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class Model {
	ArrayList<Mesh> meshes;
	public Model(){meshes = new ArrayList<Mesh>();}
	public Model(Mesh[] mesh_array){
		meshes = new ArrayList<Mesh>();
		for(Mesh m: mesh_array){
			meshes.add(m);
		}
	}
	public Model(ArrayList<Mesh> mesh_array) {
		meshes = new ArrayList<Mesh>();
		for(Mesh m: mesh_array){
			meshes.add(m);
		}
	}
	
	//Copy Constructor
	public Model(Model model) {
		this.meshes = new ArrayList<Mesh>();
		for(Mesh m: model.meshes){
			this.meshes.add(new Mesh(m));
		}
	}
	/*Setters*/
	public void addMesh(Mesh m){meshes.add(m);}
	
	/*Getters*/
	public Mesh getMesh(int i){ return meshes.get(i);}
	
	public void draw(){
		for(Mesh m: meshes){
			GL11.glPushMatrix();
			m.draw();
			GL11.glPopMatrix();
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
}
