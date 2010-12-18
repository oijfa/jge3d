package importing.pieces;

public class Texture {
	int width;
	int height;
	String data;
	boolean alpha;
	
	boolean replace;
	boolean modulate;
	boolean decal;
	
	boolean repeat;
	boolean clamp;
	
	Texture(int w, int h, String raw, boolean a, boolean r, boolean m, boolean d, boolean rep, boolean c)
	{
		width = w;
		height = h;
		data = raw;
		alpha = a;
		replace = r;
		modulate = m;
		decal = d;
		repeat = rep;
		clamp = c;
	}
}
