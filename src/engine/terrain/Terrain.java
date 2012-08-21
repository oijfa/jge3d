package engine.terrain;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Mesh;
import engine.render.ubos.Material;

public class Terrain extends Entity {
	private DynamicMatrix terrain;
	private	Model base_model;
	private Model full_model;

	public Terrain(float mass, boolean collide, Model base_model, Shader shader) {
		super(mass, collide, base_model, shader);
		this.base_model = new Model(base_model);
		this.base_model.setShader(shader);
		this.full_model = new Model(shader);
	}
	
	public Terrain(float mass, boolean collide, Model base_model) {
		super(mass, collide, base_model, base_model.getShader());
		this.base_model = base_model;
		this.full_model = new Model(base_model.getShader());
	}

	public void createTerrain(int land_size) {
		terrain = new DynamicMatrix();
		
		full_model.deleteMeshes();
		
		base_model.getMesh(0).setMaterial(
			new Material(
				new Vector4f(13.0f,13.0f,13.0f,255.0f),
				new Vector4f(13.0f,13.0f,13.0f,255.0f),
				new Vector4f(13.0f,13.0f,13.0f,255.0f),
				100.0f,
				255.0f
			)
		);
		
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
		this.setProperty("model",full_model);
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
