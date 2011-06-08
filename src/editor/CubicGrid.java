package editor;

import javax.vecmath.Vector3f;

import engine.importing.XGL_Parser;
import engine.importing.pieces.Mesh;

public class CubicGrid<E> {
	private E items[];
	private Integer size;
	
	@SuppressWarnings("unchecked")
	public CubicGrid(int dim){
		//Can't instantiate array of E, but can do this:
		items = (E[]) new Object[dim*dim*dim];
		size=dim;
	}
	
	public void set(int x, int y, int z, E item){
		items[x+(size*y)+(size*size*z)] = item;
	}
	
	public E get(int x, int y, int z){
		return items[x+(size*y)+(size*size*z)];
	}
	
	public Integer size() {
		return size;
	}
	
	public void setFromXML() {
		//TODO: parse the XML
	}
	
	public engine.importing.pieces.Model getModel(String path_to_shape) {
		XGL_Parser parser = new XGL_Parser();
		engine.importing.pieces.Model base_model = new engine.importing.pieces.Model();
		engine.importing.pieces.Model full_model = new engine.importing.pieces.Model();
		Mesh mesh = new Mesh();
		
		try {
			parser.readFile(path_to_shape);
			base_model = parser.createModel();
			
			for(int z=0;z<size;z++) {
				for(int y=0;y<size;y++) {
					for(int x=0;x<size;x++) {
						mesh = base_model.getMesh(0);
						mesh.transform(
							new Vector3f(x,y,z),
							new Vector3f(0,0,1),
							new Vector3f(0,1,0)
						);
						full_model.addMesh(mesh);
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return full_model;
	}
	
	public String toString() {
		String value = new String();
		for(int z=0;z<size;z++) {
			for(int y=0;y<size;y++) {
				for(int x=0;x<size;x++) {
					value+="<cell>\n" +
						"\t<position>\n" +
						"\t\t<x>" + x + "</x>\n" +
						"\t\t<y>" + y + "</y>\n" +
						"\t\t<z>" + z + "</z>\n" +
						"\t</position>\n" +
						"\n\t<color>" +
						this.get(x, y, z).toString() +
						"\t</color>\n" +
						"</cell>\n";
				}
			}
		}
		return value;
	}
}
