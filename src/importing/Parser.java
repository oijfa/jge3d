/*
 * 	Abstract class.  Defines all of the methods that the FileLoader expects
 * 		from a parser.  Any class that reads in a model file should extend this
 * 		so that the FileLoader will know what to do with it.
 */

package importing;

import importing.pieces.Model;
import importing.pieces.World;

public abstract class Parser {
	protected World world;
	
	public void setWorld(World world) {this.world = world;}
	public World getWorld() {return world;}

	public abstract void readFile(String fileName);
	public abstract Model createModel();
}
