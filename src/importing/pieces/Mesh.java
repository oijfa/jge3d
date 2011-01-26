package importing.pieces;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

public class Mesh {
	ArrayList<Face> faces;
	Material mat;
	Vector3f location;
	Vector3f forward;
	Vector3f up;
	
	public Mesh(){
		meshInit();
	}
	public Mesh(ArrayList<Face> _faces){
		meshInit();
		for(Face f : _faces){
			this.faces.add(new Face(f));
		}
	}
	public Mesh(Material m){
		meshInit();
		this.mat = new Material(m);
	}
	public Mesh(ArrayList<Face> _faces, Material m){
		meshInit();
		for(Face f : _faces){
			this.faces.add(new Face(f));
		}
		this.mat = new Material(m);
	}
	public Mesh(Vector3f loc){
		meshInit();
		location.x = loc.x;
		location.y = loc.y;
		location.z = loc.z;
	}
	public Mesh(ArrayList<Face> _faces, Vector3f loc){
		meshInit();
		for(Face f : _faces){
			this.faces.add(new Face(f));
		}
		location.x = loc.x;
		location.y = loc.y;
		location.z = loc.z;
	}
	public Mesh(Material m, Vector3f loc){
		meshInit();
		
		mat = new Material(m);
		location.x = loc.x;
		location.y = loc.y;
		location.z = loc.z;
	}

	public Mesh(ArrayList<Face> _faces, Material m, Vector3f loc){
		meshInit();
		
		for(Face f : _faces){
			this.faces.add(new Face(f));
		}
		mat = new Material(m);
		location = new Vector3f(loc.x,loc.y,loc.z);
	}
	
	//Copy Constructor
	public Mesh(Mesh m) {
		meshInit();

		for(Face f : m.faces){
			this.faces.add(new Face(f));
		}
		
		this.location = new Vector3f(m.location.x,m.location.y,m.location.z);
		this.forward = new Vector3f(m.forward.x,m.forward.y,m.forward.z);
		this.up = new Vector3f(m.up.x,m.up.y,m.up.z);
		
		this.mat = new Material(m.mat);
	}
	
	private void meshInit(){
		location = new Vector3f();
		forward = new Vector3f();
		up = new Vector3f();
		
		faces = new ArrayList<Face>();
		mat = null;
		location = new Vector3f(0.0f,0.0f,0.0f);
		forward = new Vector3f(0.0f,0.0f,1.0f);
		up = new Vector3f(0.0f,1.0f,0.0f);
	}
	/*Setters*/
	public void setMaterial(Material m){ mat = m; }
	public void addFace(Face f){ faces.add(f); }
	public void transform(Vector3f loc, Vector3f forw, Vector3f up) throws Exception{
			this.location = new Vector3f(loc.x,loc.y,loc.z);
			this.forward = new Vector3f(forw.x,forw.y,forw.z);
			this.up = new Vector3f(up.x,up.y,up.z);
	}
	
	/*Getters*/
	public Material getMaterial(){ return mat; }
	public Face getFace(int i){ return faces.get(i); }
	public ArrayList<Face> getFaces() { return faces; }
	
	public void draw(){
		//Set Material
		
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, mat.getAmbientAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, mat.getDiffuseAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, mat.getSpecularAsBuffer());
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, mat.getEmissionAsBuffer());
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, mat.getShine());
		
		//Transform
		GL11.glTranslatef(location.x, location.y, location.z);
		//TODO:  Rotate somehow based on forward and up vectors?
		//GL11.glRotatef(angle, x, y, z);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		//Draw faces
		for(Face f : faces){
			f.draw();
		}
		GL11.glEnd();
	}
	
	/*Debug*/
	public String toString(){
		String ret = "";
		ret += "		location: (" + location.x + "," + location.y + "," + location.z + ")\n";
		ret += "		forward: (" + forward.x + "," + forward.y + "," + forward.z + ")\n";
		ret += "		up: (" + up.x + "," + up.y + "," + up.z + ")\n";
		ret += "		Material{\n";
		ret += mat.toString();
		ret += "		}\n";	
		ret += "		# of Faces: " + faces.size() + "\n";	
		for( Integer i = 0; i < faces.size(); i++){
			ret += "		Face" + i.toString() + "{\n";
			ret += faces.get(i).toString();
			ret += "		}\n";
		}
		return ret;
	}

	public int getFaceCount() {
		return faces.size();
	}
	public StringBuffer toXGLString(int ref) {
		StringBuffer data = new StringBuffer();
		data.append(mat.toXGLString(ref));
		data.append("<MESH ID=\"" + ref + "\">");
		for(int i = 0; i < faces.size(); i++){
			data.append(faces.get(i).toXGLString(i * 3, ref));
		}
		data.append("</MESH>");
		data.append("<OBJECT>\n");
		data.append("<TRANSFORM>\n");
			data.append("<FORWARD>" + forward.x + ", " + forward.y + ", " + forward.z + "</FORWARD>\n");
			data.append("<UP>" + up.x + ", " + up.y + ", " + up.z + "</UP>\n");
			data.append("<POSITION>" + location.x + ", " + location.y + ", " + location.z + "</POSITION>\n");
		data.append("</TRANSFORM>\n");
		data.append("<MESHREF>" + ref + "</MESHREF>");
		data.append("</OBJECT>\n");
		return data;
	}
}
