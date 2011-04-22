package editor;

public class Coordinate<E extends Number> implements Cloneable {
	private E x, y, z;

	public Coordinate(Coordinate<E> copy_this){
		this.x = copy_this.x;
		this.y = copy_this.y;
		this.z = copy_this.z;
	}
	
	public Coordinate<E> clone(){
		return new Coordinate<E>(this);
	}
	
	public void setX(E x) {this.x=x;}
	public void setY(E y) {this.y=y;}
	public void setZ(E z) {this.z=z;}
	
	public E getX() {return x;}
	public E getY() {return y;}
	public E getZ() {return z;}
}