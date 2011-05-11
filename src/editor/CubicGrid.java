package editor;

import java.util.ArrayList;


public class CubicGrid<E> {
	private ArrayList<E> items;
	private int size;
	
	public CubicGrid(int dim){
		items = new ArrayList<E>(dim*dim*dim);
	}
	
	public void set(int x, int y, int z, E item){
		items.set(x+(size*y)+(size*size*z),item);
	}
	
	public E get(int x, int y, int z){
		return items.get(x+(size*y)+(size*size*z));
	}
}
