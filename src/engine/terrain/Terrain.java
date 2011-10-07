package engine.terrain;

import javax.vecmath.Vector3f;

import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Mesh;

public class Terrain extends Entity {
	private DynamicMatrix terrain;
	private	Model base_model;
	private Model full_model;

	public Terrain(float mass, boolean collide, Model base_model, Shader shader) {
		super(mass, collide, base_model, shader);
		this.base_model = base_model;
		this.base_model.setShader(shader);
		this.full_model = new Model(shader);
	}

	public void createTerrain(int land_size) {
		terrain = new DynamicMatrix();
		//Create the base matrix for the terrain
		for (int i = 0; i < land_size; i++)
			terrain.expand();
		
		for (int i = 0; i < terrain.getSize(); i++) {
			for(Mesh mesh: base_model.getMeshes()) {
				Mesh new_mesh =	new Mesh(mesh);
				try {
					new_mesh.transform(
						terrain.get(i),
						new Vector3f(0,0,1),
						new Vector3f(0,1,0)
					);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				full_model.addMesh(new_mesh);		
			}
		}
		
		this.setModel(full_model);
	}

	public String toString() {
		String text = new String();
		for (int i = 0; i < terrain.getColumns(); i++) {
			for (int j = 0; j < terrain.getColumns(); j++) {
				text += terrain.get(i, j) + ",";
			}
			text += "\n";
		}
		return text;

	}
}
