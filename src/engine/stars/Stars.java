package engine.stars;

import java.util.Random;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import engine.Engine;
import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Mesh;
import engine.render.ubos.Material;

public class Stars {
	private Entity ent;
	
	public Stars(Engine engine, int rows, int columns, int space_between_levels, int num_levels, int starting_distance) {
		Model base_model = (Model) engine.resource_manager.getResource("singlebox","models");
		Model full_model = new Model((Shader)engine.resource_manager.getResource("topdowndefault", "shaders"));
		
		for(int i=0;i<num_levels;i++) {
			for(int j=0;j<rows;j++) {
				for(int k=0;k<columns;k++) {
					Random rand = new Random();
					if(rand.nextInt(50000)==10){
						for(Mesh mesh: base_model.getMeshes()) {
							Mesh new_mesh =	new Mesh(mesh);
							try {
								new_mesh.transform(
									new Vector3f(j-starting_distance,k-starting_distance,i*-space_between_levels-starting_distance),
									new Vector3f(0,0,1),
									new Vector3f(0,1,0)
								);
								//Color setting (Needs model cloning to work)
								Float r = (float) Math.random();
								Float g = (float) Math.random();
								Float b = (float) Math.random();
								Float a = 1.0f;
								mesh.setMaterial(
									new Material(
										new Vector4f(r,g,b,a),
										new Vector4f(r,g,b,a),
										new Vector4f(r,g,b,a),
										1.0f,
										1.0f
									)
								);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							full_model.addMesh(new_mesh);		
						}
					}
				}
			}
		}
		
		ent = new Entity("Stars",1,true,full_model,(Shader)engine.resource_manager.getResource("topdowndefault", "shaders"));
		ent = engine.addEntity(ent);
		ent.activate();
		
	}

	public Entity getEntity() {
		return ent;
	}
}
