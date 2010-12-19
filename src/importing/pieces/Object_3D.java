package importing.pieces;


import java.util.HashMap;

public class Object_3D {
	private Transform transform;
	//private HashMap<Integer,Mesh> mesh;
	private HashMap<Integer,Object_3D> objects;
	private int meshref;
	private int reference;
	private String name;
	//Data data;
	//Name name;
	
	public Object_3D() {objects = new HashMap<Integer, Object_3D>();}
	
	public void addTransform(Transform _transform) {transform = _transform;}
	
	public Transform getTransform() {return transform;}
	
	public void setMeshRef(int mr) {meshref = mr;}
	
	public int getMeshRef() {return meshref;}
	
	//public void addMesh(Mesh mesh) {meshes.put(Mesh.getNextID(),mesh);}
	
	public void addObject(Object_3D obj) {objects.put(obj.getReference(), obj);}

	public int getNumberOfObjects() {return objects.size();}
	
	public Object_3D getObject(int index){return objects.get(objects.keySet().toArray()[index]);}

	public void setReference(int reference) {this.reference = reference;}

	public int getReference() {return reference;}

	public void setName(String textContent) {this.name = textContent;}
	
	public String getName(){return this.name;}
}
