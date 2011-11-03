package engine.render;

public class AnimationFrame {
	private int vboid;
	private int duration;
	
	public AnimationFrame(int vboid, int duration) {
		this.vboid = vboid;
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}
}
