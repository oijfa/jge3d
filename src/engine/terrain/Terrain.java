package engine.terrain;

import engine.Engine;

public class Terrain {
	private Engine engine;
	private DynamicMatrix terrain;

	public Terrain(Engine engine) {
		this.engine = engine;
	}

	public void createTerrain(int land_size) {
		engine.addModel("singlebox", "resources/models/misc/singlebox.xgl");

		terrain = new DynamicMatrix();
		for (int i = 0; i < land_size; i++)
			terrain.expand();
		
		for (int i = 0; i < terrain.getSize(); i++) {
		  engine.addEntity("terrain" + String.valueOf(i), 0f, true, "singlebox", "default");
		  engine.getEntity("terrain" + String.valueOf(i)).setPosition(terrain.get(i));
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
