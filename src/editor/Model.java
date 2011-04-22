package editor;
public class Model<E extends Number> {
	private CubicGrid<Block<E>> cubic_grid;
	private int size;
	private int scale;
	
	public void setSize(int s){
		CubicGrid<Block<E>> new_grid = new CubicGrid<Block<E>>(s);
		for(int z=0;z<size;z++) {
			for(int y=0;y<size;y++) {
				for(int x=0;x<size;x++){
					new_grid.set(x,y,z,cubic_grid.get(x,y,z));
				}
			}
		}
		cubic_grid = new_grid;
	}
	public void increaseScale(int steps){
		CubicGrid<Block<E>> new_grid = new CubicGrid<Block<E>>(scale);
		int x_corner;
		int y_corner;
		int z_corner;
		for(int z=0;z<size;z++) {
			for(int y=0;y<size;y++) {
				for(int x=0;x<size;x++){
					x_corner = x*size;
					y_corner = y*size;
					z_corner = z*size;
					//Set center
					new_grid.set(x_corner,y_corner,z_corner,cubic_grid.get(x,y,z));
					
					
					for(int i = 1; i < Math.pow(3, steps); i++){
						for(int j = 1; j < Math.pow(3, steps); j++){
							for(int k = 1; k < Math.pow(3, steps); k++){
								new_grid.set(x_corner,y_corner,z_corner,cubic_grid.get(x,y,z));
							}
						}
					}
				}
			}
		}
		cubic_grid = new_grid;
		scale++;
	}
	public void setBlock(Coordinate<E> coor, Block<E> block){/*TODO*/}
	
	public Block<E> getBlock(Coordinate<E> coor){
		return null;
	}
	
	public int getScale(){
		return scale;
	}
	
	public int getSize(){
		return size;
	}
}
