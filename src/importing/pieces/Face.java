package importing.pieces;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Face {
	ArrayList<Vector3f> vertices;
	ArrayList<Vector3f> vertexNormals;
	FloatBuffer faceVNT;
	IntBuffer faceVBOid;
	
	Vector3f normal;
	//[(4 bytes * 3 coords) * 2 vectors(vert&norm)] + (2 texcoords * 4 bytes)
	public static final int VERTEX_STRIDE=48;
	
	public Face(){ 
		vertices = new ArrayList<Vector3f>();
		vertexNormals = new ArrayList<Vector3f>();
		normal = new Vector3f(0.0f,0.0f,1.0f);
	}
	
	public Face(Vector3f[] verts, Vector3f[] vertnorms, Vector3f norm){
		vertices = new ArrayList<Vector3f>();
		vertexNormals = new ArrayList<Vector3f>();
		for(int i = 0; i < verts.length; i++){
			vertices.add(verts[i]);
		}
		for(int i = 0; i < vertnorms.length; i++){
			vertexNormals.add(vertnorms[i]);
		}
		normal = norm;
	}
	
	public Face(ArrayList<Vector3f> verts, ArrayList<Vector3f> vertnorms, Vector3f norm){
		vertices = verts;
		vertexNormals = vertnorms;
		normal = norm;
	}
	
	public Face(Vector3f[] verts, Vector3f[] vertnorms){
		vertices = (ArrayList<Vector3f>) Arrays.asList(verts);
		vertexNormals = (ArrayList<Vector3f>) Arrays.asList(vertnorms);
		normal =  new Vector3f(0.0f,0.0f,1.0f);
	}

	//Copy Constructor
	public Face(Face f) {
		this.vertices =  new ArrayList<Vector3f>();
		vertexNormals =  new ArrayList<Vector3f>();
		this.normal = new Vector3f();
		
		for(@SuppressWarnings("unused") Vector3f fa: f.vertices){
			this.vertices.add(new Vector3f());
		}
		for(@SuppressWarnings("unused") Vector3f fa: f.vertexNormals){
			this.vertexNormals.add(new Vector3f());
		}
		
		this.normal = new Vector3f(f.normal.x,f.normal.y,f.normal.z);
		for(int j = 0; j < f.vertices.size(); j++){
			Vector3f temp = f.vertices.get(j);
			this.vertices.set(j, new Vector3f(temp.x,temp.y,temp.z)); 
		}
		for(int j = 0; j < f.vertexNormals.size(); j++){
			Vector3f temp = f.vertexNormals.get(j);
			this.vertexNormals.set(j,new Vector3f(temp.x,temp.y,temp.z));
		}
	}

	/*Setters*/
	public void setVertices(Vector3f[] verts){
		for(int i = 0; i < verts.length; i++){
			vertices.add(verts[i]);
		}
	}
	public void setVertexNormals(Vector3f[] vertnorms){
		for(int i = 0; i < vertnorms.length; i++){
			vertexNormals.add(vertnorms[i]);
		}
	}
	public void setVertices(ArrayList<Vector3f> verts){
		vertices = verts;
	}
	public void setVertexNormals(ArrayList<Vector3f> vertnorms){
		vertexNormals = vertnorms;
	}
	public void setNorm(Vector3f norm){normal = new Vector3f(norm.x,norm.y,norm.z);}
	public void addVertexNorm(Vector3f fs){vertexNormals.add(new Vector3f(fs.x,fs.y,fs.z));}
	
	/*Getters*/
	public Vector3f getNorm(){return new Vector3f(normal.x,normal.y,normal.z);}
	
	public ArrayList<Vector3f> getVertices(){
		ArrayList<Vector3f> vertexlist = new ArrayList<Vector3f>();
		for(int i=0; i<vertices.size();i++) {
			Vector3f temp = vertices.get(i);
			vertexlist.add(new Vector3f(temp.x,temp.y,temp.z));
		}
		return vertexlist;
	}
	
	//LWJGL wants floatbuffers so we just return them in the
	//native format here
	public void createFaceBufferVNTC() {
		int VBOElementID;
		//Make sure that the face is at least a triangle
		if(vertices.size() >= 3) {
			for(int i=0;i<vertices.size();i++) {
				VBOElementID = createVBOID();
				
				faceVNT = BufferUtils.createFloatBuffer(12);
				faceVBOid = BufferUtils.createIntBuffer(1);
				
				faceVNT.put(vertices.get(i).x);
				faceVNT.put(vertices.get(i).y);
				faceVNT.put(vertices.get(i).z);
				
				faceVNT.put(vertexNormals.get(i).x);
				faceVNT.put(vertexNormals.get(i).y);
				faceVNT.put(vertexNormals.get(i).z);
				
				faceVNT.put(0.0f);
				faceVNT.put(1.0f);
				
				faceVNT.put(1.0f);
				faceVNT.put(1.0f);
				faceVNT.put(1.0f);
				faceVNT.put(1.0f);
				
				System.out.println("VBOElementID: " + String.valueOf(VBOElementID));
				faceVBOid.put(VBOElementID);
				
			    bufferData(VBOElementID, faceVNT);
			    bufferElementData(VBOElementID, faceVBOid);
			}
		} else {
			System.out.println("Tried to parse face, but it has only " + vertices.size() + " verts");
		}
	}
	public FloatBuffer getVertBuffer(int i) {
		//Create a primitive float array to wrap in the float buffer
		float[] float_array = new float[3];
		
		//Copy each vert in xyz order to the primitive array
		float_array[0] = vertices.get(i).x;
		float_array[1] = vertices.get(i).y;
		float_array[2] = vertices.get(i).z;
		
		return FloatBuffer.wrap(float_array);
	}
	
	public ArrayList<Vector3f> getNormals(){ return vertexNormals; }
	
	public Vector3f getVertex(int i){
		Vector3f temp = vertices.get(i);
		return new Vector3f(temp.x,temp.y,temp.z);
	}

	public void draw() {
		for(int i = 0; i < vertices.size(); i++){
			Vector3f v = vertices.get(i);
			Vector3f n = vertexNormals.get(i);
			GL11.glVertex3f(v.x, v.y, v.z);
			GL11.glNormal3f(n.x, n.y, n.z);
		}
	}
	
	public void draw_vbo() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		 
		//Bind the index of the object
		ARBVertexBufferObject.glBindBufferARB( ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, faceVBOid.get(0) );
		
		//vertices
		int offset = 0 * 4; // 0 as its the first in the chunk, i.e. no offset. * 4 to convert to bytes.
		GL11.glVertexPointer(3, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);
		 
		// normals
		offset = 3 * 4; // 3 components is the initial offset from 0, then convert to bytes
		GL11.glNormalPointer(GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);
		
		// texture coordinates
		offset = (3 + 3 + 2) * 4;
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);				
		
		//colors
		offset = (3 + 3) * 4; // (6*4) is the number of byte to skip to get to the colour chunk
		GL11.glColorPointer(4, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);

		//Draw the bound indices
		GL12.glDrawRangeElements(
			GL11.GL_TRIANGLES, 
			faceVBOid.get(0), 
			faceVBOid.get(vertices.size()-1), 
			vertices.size(),
			GL11.GL_UNSIGNED_INT,
			0
		);
	}
	
	@SuppressWarnings("unused")
	private Vector3f calculateNormal(Vector3f p1, Vector3f p2, Vector3f p3){
		int x = 0;
		int y = 1;
		int z = 2;
		
		Vector3f U = new Vector3f();
		Vector3f V = new Vector3f();
		Vector3f norm = new Vector3f();
		
		U.x = p2.x - p3.x;
		U.y = p2.y - p3.y;
		U.z = p2.z - p3.z;
		
		V.x = p3.x - p1.x;
		V.y = p3.y - p1.y;
		V.z = p3.z - p1.z;
		
		norm.x = (U.y*V.z) - (U.z*V.y);
		norm.y = (U.z*V.x) - (U.x*V.z);
		norm.z = (U.x*V.y) - (U.y*V.x);
		
		return norm;
	}
	
	public void addVertex(Vector3f v) {
		vertices.add(new Vector3f(v.x,v.y,v.z));
	}
	
	/*Debug*/
	public String toString(){
		String ret = "";
		for(int i = 0; i < vertices.size(); i++){
			ret += "			vert " + String.valueOf(i) + ": (" + vertices.get(i).x + "," + vertices.get(i).y + "," + vertices.get(i).z + ")\n";
		}
		for(int i = 0; i < vertexNormals.size(); i++){
			ret += "			vert norm " + String.valueOf(i) + ": (" + vertexNormals.get(i).x + "," + vertexNormals.get(i).y + "," + vertexNormals.get(i).z + ")\n";
		}
		ret += "			normal: (" + normal.x + "," + normal.y + "," + normal.z + ")\n";	
		return ret;
	}
	
	public int getVertexCount() {
		return vertices.size();
	}

	public StringBuffer toXGLString(Integer startRef, int matRef) {
		StringBuffer data = new StringBuffer();
		for(int i = 0; i < vertices.size(); i++){
			data.append("<P ID=\"" + String.valueOf(startRef + i) + "\">" + vertices.get(i).x + ", " + vertices.get(i).y + ", " + vertices.get(i).z + "</P>\n");
			data.append("<N ID=\"" + String.valueOf(startRef + i) + "\">" + vertexNormals.get(i).x + ", " + vertexNormals.get(i).y + ", " + vertexNormals.get(i).z + "</N>\n");
		}
		data.append("<F>\n");
		data.append("<MATREF>" + matRef + "</MATREF>");
		data.append("<FV1><PREF>" + String.valueOf(startRef + 0) + "</PREF><NREF>" + String.valueOf(startRef + 0) + "</NREF></FV1>");
		data.append("<FV2><PREF>" + String.valueOf(startRef + 1) + "</PREF><NREF>" + String.valueOf(startRef + 1) + "</NREF></FV2>");
		data.append("<FV3><PREF>" + String.valueOf(startRef + 2) + "</PREF><NREF>" + String.valueOf(startRef + 2) + "</NREF></FV3>");
		data.append("</F>\n");
		return data;
	}

	public static int createVBOID() {
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		ARBVertexBufferObject.glGenBuffersARB(buffer);
		return buffer.get(0);
	}
	
	public static void bufferData(int id, FloatBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	}
	public static void bufferElementData(int id, IntBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	}
}
