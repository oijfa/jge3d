package engine.render;

import engine.entity.Entity;

public interface RenderObject {
	public void drawProgrammablePipe(Entity ent);
	public void drawFixedPipe(Entity ent);
}
