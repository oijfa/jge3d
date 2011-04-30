package editor;
public class Model<E extends Number> {
	private CubicGrid<Block<E>> cubic_grid;
	private int size;
	private int scale; //3^scale == size of expanded pixel
	
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
		if( steps > 0){
			CubicGrid<Block<E>> new_grid = new CubicGrid<Block<E>>((int)(size+(size*Math.pow(2,steps))));
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
			scale+=steps;
		}
	}
	
	public void decreaseScale(int steps){
		if(steps > 0 && scale-steps > 0){
			int new_size = (int)(size-(size*Math.pow(2,steps)));
			CubicGrid<Block<E>> new_grid = new CubicGrid<Block<E>>(new_size);
			int x_corner;
			int y_corner;
			int z_corner;
			for(int z=0;z<new_size;z++) {
				for(int y=0;y<new_size;y++) {
					for(int x=0;x<new_size;x++){
						x_corner = (x*new_size);
						y_corner = (y*new_size);
						z_corner = (z*new_size);
						
						//Set center
						new_grid.set(x,y,z,averageCube(cubic_grid,x_corner,y_corner,z_corner,steps));		
					}
				}
			}
			cubic_grid = new_grid;
			scale-=steps;
		}
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
	
	private Block<E> averageCube(CubicGrid<Block<E>> cubic_grid,int x_center,int y_center,int z_center,int steps){
		x_center += (int)((Math.pow(3, steps)-1)/2);
		y_center += (int)((Math.pow(3, steps)-1)/2);
		z_center += (int)((Math.pow(3, steps)-1)/2);

		return cubic_grid.get(x_center, y_center, z_center);
	}
}
