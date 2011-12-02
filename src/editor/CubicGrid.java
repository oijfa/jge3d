package editor;

import javax.vecmath.Vector3f;

import de.matthiasmann.twl.Color;

import engine.render.Model;
import engine.render.model_pieces.Material;
import engine.render.model_pieces.Mesh;

public class CubicGrid<E> {
	private E items[];
	private Integer size;
	private Model full_model;
	private Model base_model;

	public CubicGrid(Model base_model) {
		this.base_model = base_model;
		full_model = new Model(base_model.getShader());
	}

	public void set(int x, int y, int z, E item) {
		items[x + (size * y) + (size * size * z)] = item;
	}

	public E get(int x, int y, int z) {
		return items[x + (size * y) + (size * size * z)];
	}

	public Integer getSize() {
		return size;
	}

	public void setFromXML() {
		// TODO: parse the XML
	}

	@SuppressWarnings("unchecked")
	public void setSize(int dim) {
		items = (E[]) new Object[dim * dim * dim];
		size = dim;
	}
	
	@SuppressWarnings("unchecked")
	public Model getModel() {
		try {
			Color mat_color;
			full_model.deleteMeshes();
			
			for (int z = 0; z < size; z++) {
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						if( ((Block<Integer>)get(x,y,z)).getActive()) {
							for(Mesh copymesh: base_model.getMeshes()) {
								Mesh mesh = new Mesh(copymesh);
								mesh.transform(
									new Vector3f(x, -y, z),
									new Vector3f(0, 0, 1), 
									new Vector3f(0, 1, 0)
								);
								mat_color = ((Block<Integer>) this.get(x, y, z)).getColor();
								
								mesh.setMaterial(
									new Material(
										new Vector3f(
											mat_color.getRedFloat(), 
											mat_color.getGreenFloat(), 
											mat_color.getBlueFloat()
										),
										new Vector3f(
											mat_color.getRedFloat(), 
											mat_color.getGreenFloat(), 
											mat_color.getBlueFloat()
										),
										new Vector3f(
											mat_color.getRedFloat(), 
											mat_color.getGreenFloat(), 
											mat_color.getBlueFloat()
										),
										new Vector3f(
											1, 
											1, 
											1
										),
										1f
									)
								);
								
								//mesh.calcNormals();
								full_model.addMesh(mesh);
							}
						}
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
		for (int z = 0; z < size; z++) {
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					value += "<cell>\n" + "\t<position>\n" + "\t\t<x>" + x
						+ "</x>\n" + "\t\t<y>" + y + "</y>\n" + "\t\t<z>" + z
						+ "</z>\n" + "\t</position>\n" + "\n\t<color>"
						+ this.get(x, y, z).toString() + "\t</color>\n"
						+ "</cell>\n";
				}
			}
		}
		return value;
	}
}
