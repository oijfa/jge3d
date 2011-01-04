package window;

import window.components.Tree;
import de.matthiasmann.twl.ResizableFrame;

public class EntityMenu extends ResizableFrame {
	private final Tree textree;
	public EntityMenu() {
		setTitle("Entity Editor");
		
		textree = new Tree();
		textree.setTheme("textree");

	}
}
