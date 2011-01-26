//TODO: Maybe add transforms?
package importing.pieces;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

import debug.CubeShape;

public class Model {
	ArrayList<Mesh> meshes;
	Vector3f max, min, center;
	
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
		for(Mesh m: meshes){
			GL11.glPushMatrix();
			if(center != null) {
				GL11.glPushMatrix();
					GL11.glTranslatef(max.x, max.y, max.z);
					CubeShape.drawTestCube(0.05f);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
					GL11.glTranslatef(min.x, min.y, min.z);
					CubeShape.drawTestCube(0.05f);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
					GL11.glTranslatef(center.x, center.y, center.z);
					CubeShape.drawTestCube(0.05f);
				GL11.glPopMatrix();
			}	
			m.draw();
			GL11.glPopMatrix();
		}
	}
	
	/* Verify */
	//This function will verify whether the file had normals defined
	//and also check for shading groups and normalize if possible
	public void verify() {	
		ArrayList<Vector3f> maxes = new ArrayList<Vector3f>();
		ArrayList<Vector3f> mins = new ArrayList<Vector3f>();
		for(Mesh m: meshes) {
			maxes.add(m.getMaximums());
			mins.add(m.getMinimums());
			m.calcNormals();
		}
		
		max = new Vector3f();
		min = new Vector3f();
		
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
		System.out.print("Max="+max+":Min="+min);
		
		//find the center of the model using our min/max values
		center.add(max,min);
		center.scale(0.5f);
		System.out.println(":Center="+center);
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
		return new BoxShape(shape);
	}
}
