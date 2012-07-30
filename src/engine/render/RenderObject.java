package engine.render;

import engine.entity.Entity;

public interface RenderObject {
	public void drawProgrammablePipe(Entity ent);
	public void drawProgrammablePipe(Entity ent, Shader shader);
	public void drawFixedPipe(Entity ent);
}
