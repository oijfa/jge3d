package engine.render;

import engine.entity.Camera;
import engine.window.WindowManager;

public interface RendererInterface {
	public void draw();
	public WindowManager getWindowManager();
	public void initGL();
	public void setCamera(Camera camera);
}
