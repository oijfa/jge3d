//TODO: Maybe add transforms?
package importing.pieces;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

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
				GL11.glTranslatef(center.x, center.y, center.z);
				CubeShape.drawTestCube(0.15f);
			}	
			m.draw();
			GL11.glPopMatrix();
		}
	}
	
	/* Verify */
	//This function will verify whether the file had normals defined
	//and also check for shading groups and normalize if possible
	public void verify() {		
		for(Mesh m: meshes) {
			for(Face f: m.getFaces()) {
				if(f.getVertices().size() > 0) {
					//find the max and min vertices for each dimension
					if(f.getVertex(0).x>max.x)
						max.x = f.getVertex(0).x;
					if(f.getVertex(1).x>max.x)
						max.x = f.getVertex(1).x;
					if(f.getVertex(2).x>max.x)
						max.x = f.getVertex(2).x;
					if(f.getVertex(0).x<min.x)
						min.x = f.getVertex(0).x;
					if(f.getVertex(1).x<min.x)
						min.x = f.getVertex(1).x;
					if(f.getVertex(2).x<min.x)
						min.x = f.getVertex(2).x;
					
					if(f.getVertex(0).y>max.y)
						max.y = f.getVertex(0).y;
					if(f.getVertex(1).y>max.y)
						max.y = f.getVertex(1).y;
					if(f.getVertex(2).y>max.y)
						max.y = f.getVertex(2).y;
					if(f.getVertex(0).y<min.y)
						min.y = f.getVertex(0).y;
					if(f.getVertex(1).y<min.y)
						min.y = f.getVertex(1).y;
					if(f.getVertex(2).y<min.x)
						min.y = f.getVertex(2).y;
					
					if(f.getVertex(0).z>max.z)
						max.z = f.getVertex(0).z;
					if(f.getVertex(1).z>max.z)
						max.z = f.getVertex(1).z;
					if(f.getVertex(2).x>max.x)
						max.z = f.getVertex(2).z;
					if(f.getVertex(0).z<min.z)
						min.z = f.getVertex(0).z;
					if(f.getVertex(1).z<min.z)
						min.z = f.getVertex(1).z;
					if(f.getVertex(2).z<min.z)
						min.z = f.getVertex(2).z;
					
					//if there are no vertices defined in the file we
					//need to find them from the face
					if( f.getNormals().size() <= 0 ){
						ArrayList<Vector3f> normal_set = new ArrayList<Vector3f>();
						Vector3f
							vertex0 = new Vector3f(),
							vertex1 = new Vector3f(),
							vertex2 = new Vector3f(),
							line1 = new Vector3f(),
							line2 = new Vector3f(),
							normal_vert = new Vector3f()
						;
	
						//Copy the verts so we don't scrub the originals
						//with our math
						vertex0 = f.getVertex(0);
						vertex1 = f.getVertex(1);
						vertex2 = f.getVertex(2);
						
						//Find two vectors so we can get the orientation
						//of the face
						line1.sub(vertex0,vertex2);
						line2.sub(vertex0,vertex1);
						normal_vert.cross(line1, line2);
						
						//To normalize we must find the length of the normal
						//based on the cross product of our vectors
						normal_vert.normalize();
	
						//Since we only have enough information to do
						//a face vertex we just copy the data for each vert
						for(int i=0; i<3;i++){normal_set.add(normal_vert);}
						f.setVertexNormals(normal_set);
					}
				}
			}
		}	
		System.out.print("Max="+max+":Min="+min);
		//find the center of the model using our min/max values
		max.add(min);
		max.scale(0.5f);
		center = max;
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
}
