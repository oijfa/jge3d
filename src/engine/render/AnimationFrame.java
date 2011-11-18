package engine.render;

public class AnimationFrame {
	private Model model;
	private int duration;
	
	public AnimationFrame(Model model, int duration) {
		this.model = model;
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Model getModel() {
		return model;
	}
}
