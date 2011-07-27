package engine.terrain;

import engine.Engine;
import engine.importing.FileLoader;

import engine.render.Model;

import engine.entity.Entity;

public class Terrain {
	private Engine engine;
	private DynamicMatrix terrain;

	public Terrain(Engine engine) {
		this.engine = engine;
	}

	public void createTerrain(int land_size) {
		Model model = FileLoader.loadFile("resources/models/misc/singlebox.xgl");

		terrain = new DynamicMatrix();
		for (int i = 0; i < land_size; i++)
			terrain.expand();
		
		for (int i = 0; i < terrain.getSize(); i++) {
			Entity ent = new Entity(0.0f, true, new Model(model));
			ent.setPosition(terrain.get(i));
			engine.addEntity(ent);
		}
	}
	
	public void lower(int down) {
		for (int i = 0; i < terrain.getSize(); i++) {
			terrain.get(i).z -= down;
		}
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
