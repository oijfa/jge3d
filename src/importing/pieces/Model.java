//TODO: Maybe add transforms?
package importing.pieces;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

public class Model {
	private ArrayList<Mesh> meshes;
	private volatile Vector3f max, min, center;
	private boolean hasVBO = false;
	private int modelVBOID;
	private int modelVBOindexID;
	private FloatBuffer vertex_buffer;
	private IntBuffer index_buffer;
	private Integer pointIndex = 0;
	
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
		//verify();
	}
	
	public void init() {
		max = new Vector3f();
		min = new Vector3f();
		center = new Vector3f();
	}
	
	/*Setters*/
	public void addMesh(Mesh m){meshes.add(m);}
	
	/*Getters*/
	public Mesh getMesh(int i){ return meshes.get(i); }
	public Vector3f getCenter(){ return center; }
	public int getMeshCount() { return meshes.size(); }
	
	public void draw(){
		//http://lwjgl.org/wiki/index.php?title=Using_Vertex_Buffer_Objects_(VBO)
		//if the renderer supports VBOs definitely use them; if it doesn't
		//we fall-back to immediate mode
		//System.out.println("VBO:" + hasVBO);
		if(hasVBO) {
			GL11.glPushMatrix();
			draw_vbo();
			GL11.glPopMatrix();
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
		
		System.out.println("MAX: " + String.valueOf(max));
		System.out.println("MIN: " + String.valueOf(min));
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

	public ArrayList<Byte> getColor() {
		return meshes.get(0).getMaterial().getColor();
	}
	
	//*******************VBO METHODS**************************
	public static int createVBOID(int i) {
		IntBuffer buffer = BufferUtils.createIntBuffer(i);
		ARBVertexBufferObject.glGenBuffersARB(buffer);
		return buffer.get(0);
	}
	public static void bufferData(int id, FloatBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,0);
		buffer.rewind();
	}
	public static void bufferElementData(int id, IntBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
		buffer.rewind();
	}

	public void createVBO() {
		//if we support VBOs we need to precompute the thing now
		//that we have normals and the model is fully loaded
		int num_meshes=meshes.size();
		int num_faces=0;
		int num_vertices = meshes.get(0).getFace(0).getVertexCount();
		for(Mesh m: meshes) {
			num_faces+=m.getFaceCount();
		}
		vertex_buffer = BufferUtils.createFloatBuffer(
			num_meshes*num_faces*12*num_vertices
		);
		index_buffer = BufferUtils.createIntBuffer(
			num_meshes*num_faces*num_vertices
		);

		for(Mesh m: meshes) {
			for(Face f: m.getFaces()) {
				vertex_buffer.put(f.createFaceBufferVNTC(m.location));
				index_buffer.put(f.createIndexBufferVNTC(pointIndex));
				pointIndex += 3;
			}
		}
		//NEVER FLIP AGAIN PAST THIS POINT UNLESS YOU'RE LOADING IN COMPLETELY NEW DATA
		vertex_buffer.flip();
		index_buffer.flip();
		
		/*
		System.out.println("Vertex Buffer has:");
		int i = 0;
		while(vertex_buffer.hasRemaining()){
			System.out.println(String.valueOf(i) + vertex_buffer.get());
		}
		
		System.out.println("Index buffer has:");
		i = 0;
		while(index_buffer.hasRemaining()){
			System.out.println(String.valueOf(i) + index_buffer.get());
		}
		*/
		
		modelVBOID = createVBOID(1);
		bufferData(modelVBOID, vertex_buffer);
		modelVBOindexID = createVBOID(1);
	    bufferElementData(modelVBOindexID, index_buffer);
		hasVBO=true;
		System.out.println("Model VBO created");
	}
	public void draw_vbo() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		 
		//Bind the index of the object
		ARBVertexBufferObject.glBindBufferARB(
			ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,
			modelVBOID
		);

		ARBVertexBufferObject.glBindBufferARB(
			ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_BINDING_ARB, 
			modelVBOindexID
		);
		
		//vertices
		int offset = 0 * 4; // 0 as its the first in the chunk, i.e. no offset. * 4 to convert to bytes.
		GL11.glVertexPointer(3, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);
		 
		// normals
		offset = 3 * 4; // 3 components is the initial offset from 0, then convert to bytes
		GL11.glNormalPointer(GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);
		
		// texture coordinates
		offset = (3 + 3) * 4;
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);				
		
		//colors
		offset = (3 + 3 + 2) * 4; // (6*4) is the number of byte to skip to get to the colour chunk
		GL11.glColorPointer(4, GL11.GL_FLOAT, Face.VERTEX_STRIDE, offset);

		//Draw the bound indices
		/*
		GL12.glDrawRangeElements(
			GL11.GL_TRIANGLES, 
			index_buffer.get(0), 
			index_buffer.get(index_buffer.capacity()-1), 
			index_buffer.capacity(),
			GL11.GL_UNSIGNED_INT,
			0
		);
		
		OR
		
		GL12.glDrawRangeElements(
			GL11.GL_TRIANGLES, 
			index_buffer.get(0), 
			index_buffer.get(index_buffer.capacity()-1),
			index_buffer
		);
		
		*/

		index_buffer.rewind();
		int first = index_buffer.get(0);
		index_buffer.rewind();
		int last = index_buffer.get(index_buffer.limit()-1);
		index_buffer.rewind();
		System.out.println("Model#: "+modelVBOID+" ModelIndex#: "+modelVBOindexID+" FirstIndex: "+first+" LastIndex: "+last);
		
		GL12.glDrawRangeElements(
			GL11.GL_TRIANGLES, 
			first, 
			last,
			index_buffer
		);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	}
	public void destroyVBO() {
		ARBVertexBufferObject.glDeleteBuffersARB(modelVBOID);	
	}
	//*****************END VBO METHODS***********************
}
