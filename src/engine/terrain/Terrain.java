package engine.terrain;

import javax.vecmath.Vector3f;

import engine.Engine;
import engine.entity.Entity;
import engine.render.Model;
import engine.render.model_pieces.Mesh;

public class Terrain {
	private Engine engine;
	private DynamicMatrix terrain;
	private Entity ent;

	public Terrain(Engine engine) {
		this.engine = engine;
	}

	public void createTerrain(int land_size) {
		//New and improved method that doesn't work...
		ent = engine.addEntity("terrain", 0f, true, "singlebox", "default");
		terrain = new DynamicMatrix();
		
		Model base_model = engine.getModelByName("singlebox");
		Model full_model = new Model(base_model.getShader());
		
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
		
		ent.setModel(full_model);
	}
	
	public void setPosition(Vector3f pos) {
		ent.setPosition(pos);
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

	public Entity getEntity() {
		return ent;
	}
}
