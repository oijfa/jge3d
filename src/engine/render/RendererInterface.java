package engine.render;

import engine.entity.Camera;
import engine.entity.EntityListListener;
import engine.entity.EntityListener;
import engine.window.WindowManager;

public abstract class RendererInterface implements EntityListListener, EntityListener{
	public abstract void draw();
	public abstract WindowManager getWindowManager();
	public abstract void initGL();
	public abstract void setCamera(Camera camera);
	public abstract void parseRenderQueue();
}
