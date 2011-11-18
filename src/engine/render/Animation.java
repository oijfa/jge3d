package engine.render;

import java.util.ArrayList;
import java.util.HashMap;

import com.bulletphysics.collision.dispatch.CollisionObject;

public class Animation implements RenderObject {
	HashMap<String, ArrayList<AnimationFrame>> animation = new HashMap<String,ArrayList<AnimationFrame>>();
	String last_animation_name;
	long last_update;
	int last_frame;
	
	public Animation() {
		 last_update = System.nanoTime();
	}
	
	public void addFrame(String animation_name, Model model, int duration) {
		animation.get(animation_name).add(new AnimationFrame(model,duration));
	}
	
	private void nextFrame(String animation_name) {
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

	@Override
	public void draw(CollisionObject collision_object) {
		animation.get(last_animation_name).get(last_frame).getModel().draw(collision_object);
		nextFrame(last_animation_name);
	}
	
	public void setAnimation(String name) {
		last_animation_name = name;
	}
	
	public StringBuffer animationToXML() {
		StringBuffer output = new StringBuffer();
		output.append("<animatedmodel>\n");
		for(String frame_name: animation.keySet()) {
			output.append("\t<animation>\n");
			output.append("\t\t<name>" + frame_name + "</name>\n");
			for(int i=0; i<animation.get(frame_name).size(); i++) {
				output.append("\t\t<frame>\n");
				output.append("\t\t\t<position>" + i + "</position>\n");
				output.append(animation.get(frame_name).get(i).getModel().toXGLString() + "\n");
				output.append("\t\t</frame>\n");
			}
			output.append("\t</animation>\n");
		}
		output.append("</animatedmodel>\n");
		return output;
	}
}
