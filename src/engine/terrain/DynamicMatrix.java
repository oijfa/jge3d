package engine.terrain;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class DynamicMatrix {
	private ArrayList<Vector3f> table;
	private int rows = 1;
	private int columns = 1;

	/* CONSTRUCTORS */
	public DynamicMatrix() {
		table = new ArrayList<Vector3f>();
	}

	/* OPERATION METHODS */
	public void expand() {
		Vector3f rand = new Vector3f();

		// Add the new row and column to our matrix
		// add column
		// 0 0 0 X
		// 0 0 0 X
		// 0 0 0 X
		// 0 0 0 0
		for (int i = 0; i < rows - 1; i++) {
			rand = randomizeNoBreaks(columns - 1, i);
			table.add(rows * (i + 1) - 1, rand);
		}
		// add row like:
		// 0 0 0 0
		// 0 0 0 0
		// 0 0 0 0
		// X X X 0
		for (int i = 0; i < columns - 1; i++) {
			rand = randomizeNoBreaks(i, rows - 1);
			table.add(rand);
		}
		// add the lower right corner of our table like:
		// 0 0 0 0
		// 0 0 0 0
		// 0 0 0 0
		// 0 0 0 X
		rand = randomizeNoBreaks(columns - 1, rows - 1);
		table.add(rand);

		rows++;
		columns++;
	}

	public void addNoise() {
		// Step across the entire structure and randomize the Y
		for (int i = 0; i < (columns * rows); i++) {
			Vector3f current = table.get(i);
			current.add(randomizeNoBreaks(indexToRowCol(i)[0],
				indexToRowCol(i)[1]));
			table.set(i, current);
		}
	}

	public Vector3f randomVector(int column, int row) {
		// We don't want to randomize the x and z as that would make the terrain
		// overlap
		// instead just take in those from the matrix position and randomize Y
		return new Vector3f(row, (float) Math.random(), column);
	}

	public float randomInt() {
		// We don't want to randomize the x and z as that would make the terrain
		// overlap
		// instead just take in those from the matrix position and randomize Y
		float rand = (float) Math.random();
		float out = 0;
		if (rand <= .333) out = -1;
		else if (rand > .333 && rand <= .666) out = 0;
		else out = 1;
		return out;
	}

	public Vector3f averageWithNeighbors(int i) {
		float avg = 0;
		int count = 0;

		// Make sure that a neighbor exists
		// if it does then add its Y position to the avg
		// and increment the count
		if (i - 1 > 0) {
			avg += table.get(i - 1).y;
			++count;
		}
		if (i >= 0 && i <= table.size()) {
			avg += table.get(i).y;
			++count;
		}
		if (i + 1 < table.size()) {
			avg += table.get(i + 1).y;
			++count;
		}
		if (i - rows > 0) {
			avg += table.get(i - rows).y;
			++count;
		}
		if (i + rows < table.size()) {
			avg += table.get(i + rows).y;
			++count;
		}

		// return a new vector containing the average Y
		// position based on neighbors
		return new Vector3f(0, avg / count, 0);
	}

	public Vector3f randomizeNoBreaks(int column, int row) {
		int i = rowColToIndex(column, row);
		Float left = 0f, up = 0f, y = 0f;
		Boolean found_left = false, found_up = false;

		// Make sure that a neighbor exists
		// if it does then store its value
		if (i - 1 > 0) {
			left = table.get(i - 1).y;
			found_left = true;
		}
		if (i - rows > 0) {
			up = table.get(i - rows).y;
			found_up = true;
		}

		// Depending on what's available we need to figure out
		// what our next value is without leaving a leak in the
		// terrain
		if (found_left && found_up) y = (up + left) / 2;
		else if (!found_left && found_up) y = up + randomInt();
		else if (found_left && !found_up) y = left + randomInt();
		else y = randomInt();
		// return a new vector containing the average Y
		// position based on neighbors
		return new Vector3f(column, y, row);
	}

	public void smooth() {
		// apply the average neighbors method to all cells
		for (int i = 0; i < table.size(); i++) {
			table.get(i).add(averageWithNeighbors(i));
		}
	}

	public int rowColToIndex(int column, int row) {
		// Convert from a table row/column to an array index
		return row * rows + column;
	}

	public int[] indexToRowCol(int i) {
		// Convert from an array index to a table row/column
		int out[] = new int[2];

		out[0] = i / rows;
		out[1] = i % columns;

		return out;
	}

	/* GETTERS */
	public Vector3f get(int column, int row) {
		return table.get(row * (rows - 1) + (column));
	}

	public Vector3f get(int i) {
		return table.get(i);
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

	/* SETTERS */
	public Vector3f put(int column, int row, Vector3f data) {
		return table.set(row * column, data);
	}

	/* DEBUG */
	public void debug() {
		// Prints out the current table
		System.out.println();
		System.out.println("Size: " + rows + "," + columns + ";Capacity:"
			+ table.size() + "\n===============");
		for (int i = 0; i < (columns * rows); i++) {
			System.out.print(table.get(i));
			if ((i < (rows - 1)) || ((i + 1) % rows != 0)) System.out
				.print(",");
			else System.out.println();
		}
	}
}