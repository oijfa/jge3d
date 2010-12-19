package importing.pieces;

import javax.vecmath.Vector3f;

public class Point {
	private Vector3f position;
	private int reference;
	
	public Point(Vector3f _pos) {
		position = _pos;
	}
	
	public Point()
	{
		position = new Vector3f(0,0,0);
		setReference(0);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f _point)
	{
		position = _point;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public int getReference() {
		return reference;
	}
}
