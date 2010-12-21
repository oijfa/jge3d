/*
 * 	Abstract class.  Defines all of the methods that the FileLoader expects
 * 		from a parser.  Any class that reads in a model file should extend this
 * 		so that the FileLoader will know what to do with it.
 */

package importing;

import importing.pieces.Model;

public abstract class Parser {
	protected Model model;
	
	public void setWorld(Model _model) {this.model = _model;}
	public Model getWorld() {return model;}

	public abstract void readFile(String fileName) throws Exception;
	public abstract Model createModel();
}
