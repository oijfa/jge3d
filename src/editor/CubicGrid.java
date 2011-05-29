package editor;

public class CubicGrid<E> {
	private E items[];
	private Integer size;
	
	@SuppressWarnings("unchecked")
	public CubicGrid(int dim){
		//Can't instantiate array of E, but can do this:
		items = (E[]) new Object[dim*dim*dim];
		size=dim;
	}
	
	public void set(int x, int y, int z, E item){
		items[x+(size*y)+(size*size*z)] = item;
	}
	
	public E get(int x, int y, int z){
		return items[x+(size*y)+(size*size*z)];
	}
	
	public Integer size() {
		return size;
	}
}
