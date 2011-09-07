package engine.stars;

import java.util.Random;

import javax.vecmath.Vector3f;

import engine.Engine;
import engine.entity.Entity;
import engine.render.Model;
import engine.render.model_pieces.Mesh;

public class Stars {
	private Entity ent;
	
	public Stars(Engine engine, int rows, int columns, int space_between_levels, int num_levels, int starting_distance) {
		ent = engine.addEntity("stars", 0f, true, "singlebox", "default");
		
		Model base_model = engine.getModelByName("singlebox");
		Model full_model = new Model(base_model.getShader());

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
								/*Color setting (Needs model cloning to work)
								Float r = (float) Math.random();
								Float g = (float) Math.random();
								Float b = (float) Math.random();
								System.out.println(r+";"+g+";"+b);
								star.getModel().getMesh(0).setMaterial(
									new Material(
										new Vector3f(r,g,b),
										new Vector3f(r,g,b),
										new Vector3f(r,g,b),
										new Vector3f(r,g,b),
										1.0f
									)
								);*/
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
		
		ent.setModel(full_model);
		ent.applyImpulse(new Vector3f(0,-100,0), new Vector3f(0,0,0));
	}

	public Entity getEntity() {
		return ent;
	}
}
