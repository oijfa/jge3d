package editor.window;

import engine.window.components.ContainerBox;

public class ToolBox extends ContainerBox {	
	public ToolBox() {
		super("ToolBox");
		addWindow(new LayerMenu());
		addWindow(new PerspectiveMenu());
		addWindow(new MirrorMenu());
	}
}
