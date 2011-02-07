package window.tree;

public class ColoredTextString {
	    private final String str;
	    final String color;

	    public ColoredTextString(String str, String colorTheme) {
	        this.str = str;
	        this.color = colorTheme;
	    }

	    @Override
	    public String toString() {
	        return str;
	    }
}
