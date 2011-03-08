package terrain;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class DynamicMatrix {
	ArrayList<Vector3f> table;
	int rows=1;
	int columns=1;
	
	public DynamicMatrix() {
		table = new ArrayList<Vector3f>();
		//table.add(new Vector3f(0,(float)Math.random(),0));
	}
	
	public void expand() {
		Vector3f rand = new Vector3f();
	
		//Add the new row and column to our matrix
		//add column
		for(int i=0;i<rows-1;i++) {
			rand = randomVector(columns-1,i);
			table.add(rows*(i+1)-1,rand);
			//System.out.println("Column:"+(columns-1)+","+i);
		}
		//add row
		for(int i=0;i<columns-1;i++) {
			rand = randomVector(i,rows-1);
			table.add(rand);
			//System.out.println("Row:"+i+","+(rows-1));
		}
		table.add(randomVector(columns-1,rows-1));

		System.out.println("Size: "+rows+","+columns+";Capacity:"+table.size()+"\n===============");
		
		//Step across the entire structure and aver
		//the current element with a random vector
		for(int i=0;i<(columns*rows);i++) {
			Vector3f current = table.get(i);
			current.add(new Vector3f(0,(float)Math.random(),0));
			table.set(i,current);
			System.out.print(table.get(i));
			if( (i<(rows-1)) || ((i+1)%rows != 0) )
				System.out.print(",");
			else
				System.out.println();
		}
		rows++;
		columns++;
	}
	
	public void smooth() {
		for(int i=0; i < table.size(); i++) {
			table.get(i).add(averageWithNeighbor(i));
		}
	}
		
	
	public Vector3f get(int column, int row) {
		return table.get(row*(rows-1)+(column));
	}
	
	public Vector3f get(int i) {
		return table.get(i);
	}
	
	public Vector3f put(int column, int row, Vector3f data) {
		return table.set(row*column,data);
	}
	
	public Vector3f randomVector(int column, int row) {
		return new Vector3f(
			row,
			(float)Math.random(),
			column
		);
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getSize() {
		return table.size();
	}
	
	public Vector3f averageWithNeighbor(int i) {
		Vector3f avg_vec;
		
		avg_vec =
			new Vector3f(
				0,
				(
					table.get(i-1).y +
					table.get(i).y +
					table.get(i+1).y +
					table.get(i-rows).y +
					table.get(i+rows).y
				) / 5,
				0
			);
		
		return avg_vec;
	}
}