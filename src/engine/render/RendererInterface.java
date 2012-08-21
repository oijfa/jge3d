package engine.render;

import engine.entity.Camera;
import engine.entity.EntityListListener;
import engine.window.WindowManager;

public interface RendererInterface extends EntityListListener{
	public void draw();
	public WindowManager getWindowManager();
	public void initGL();
	public void setCamera(Camera camera);
	public void parseRenderQueue();
}
