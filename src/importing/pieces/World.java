package importing.pieces;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.GL11;

public class World {
	ArrayList<String> data_tag;
	float[] background = new float[3];
	//Texture goes here
	private HashMap<Integer,Mesh> meshes;
	private HashMap<Integer,Object_3D> objects;
	static int blah = 0;
	
	public World() {
		meshes = new HashMap<Integer, Mesh>();
		objects = new HashMap<Integer, Object_3D>();
	}
	
	public void addMesh(Mesh _mesh) {
		meshes.put(_mesh.getReference(), _mesh);
	}
	
	public void addObject(Object_3D obj) {
		objects.put(obj.getReference(), obj);
	}
	
	public void addData(String data) {
		data_tag.add(data);
	}
	
	public void addBackground(float r, float g, float b) {
		background[0]=r;
		background[1]=g;
		background[2]=b;
	}
	
	public Mesh getMesh(int index) {
		return meshes.get(index);
	}
	
	public int getNumberOfObjects()
	{
		return objects.keySet().toArray().length;
	}
	
	public Object_3D getObject(int index) {
		return objects.get(objects.keySet().toArray()[index]);
	}
	
	public void draw() throws LWJGLException
	{  
		//System.out.print("WORLD DRAW CALLED =" + objects.size() + "\n");

	    for( Map.Entry<Integer, Object_3D>  entry : objects.entrySet() ) {  
	    	//System.out.print("=============" + entry.getKey() + "===========\n");
	    	drawObject(entry.getValue());
	    }   
	}
	
	public void drawObject(Object_3D obj) throws LWJGLException
	{
		blah++;
		//System.out.print("*******" + blah + "**********\n");
		GL11.glPushMatrix();
		
		obj.getTransform().draw();
		meshes.get(obj.getMeshRef()).draw();
		
		for(int i = 0; i < obj.getNumberOfObjects(); i++)
		{
			//System.out.print("+++++++++_++++" + obj.getObject(i).getReference() + "++" + i +"+++++++_++++\n");
			drawObject(obj.getObject(i));
		}
		
		GL11.glPopMatrix();
	}
}
