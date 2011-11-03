package engine.render;

import java.util.ArrayList;
import java.util.HashMap;

public class Animation extends Model {
	HashMap<String, ArrayList<AnimationFrame>> animation = new HashMap<String,ArrayList<AnimationFrame>>();
	String last_animation_name;
	long last_update;
	int last_frame;
	
	public Animation() {
		 last_update = System.nanoTime();
	}
	
	public void addFrame(String animation_name, int vboid, int duration) {
		animation.get(animation_name).add(new AnimationFrame(vboid,duration));
	}
	
	public void nextFrame(String animation_name) {
		AnimationFrame next_frame = animation.get(animation_name).get(last_frame);
		if( (last_update+next_frame.getDuration()) <= System.nanoTime()) {
			last_update = System.nanoTime();
			if(last_animation_name.equals(animation_name)) {
				if(last_frame == animation.get(animation_name).size()) {
					last_frame = 0;
				} else {
					last_frame++;
				}
			} else {
				last_frame = 0;
				last_animation_name = animation_name;
			}
		}
	}
}
