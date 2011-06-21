package engine.window.tree;

import de.matthiasmann.twl.Color;

public class ColoredTextString {
	private final String str;
	final Color color;

	public ColoredTextString(String str, byte r, byte g, byte b) {
		this.str = str;
		this.color = new Color(r, g, b, (byte) 0xFFFF);

	}

	@Override
	public String toString() {
		return str;
	}

	public Color getColor() {
		return this.color;
	}
}
