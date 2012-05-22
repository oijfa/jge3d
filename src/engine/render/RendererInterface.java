package engine.render;

import engine.entity.Camera;
import engine.window.WindowManager;

public interface RendererInterface {
	public void draw();
	public void setWindowManager(WindowManager wm);
	public WindowManager getWindowManager();
	public void initGL();
	public void setCamera(Camera camera);
}
