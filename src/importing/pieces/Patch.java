package importing.pieces;


import java.util.ArrayList;
import java.util.HashMap;

public class Patch {
	private ArrayList<Face> faces;
	private HashMap<Integer, Material> material;
	private int reference;
	
	public Patch() {
		faces = new ArrayList<Face>();
		material = new HashMap<Integer, Material>();
	}
	
	public void addFace(Face face) {
		faces.add(face);
	}
	
	public void addMaterial(int index, Material mat) {
		material.put(index, mat);
	}
	public ArrayList<Face> getFaces()
	{
		return faces;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public int getReference() {
		return reference;
	}
}
