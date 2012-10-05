package topdownshooter;

import engine.entity.Actor;
import engine.render.Model;
import engine.render.Shader;

public class Player extends Actor {

	public Player(String name, float mass, float step_height, Model model,
			Shader shader) {
		super(name, mass, step_height, model, shader);
	}
	
	
}
