/*
 * 	Not sure exactly what we want to do with this class yet.  I've just implemented
 * 		a skeleton that we can flesh out as we need to.
 * 
 * 	//TODO:	Add draw function that can draw the thing
 * 	//TODO:	Figure what else needs to be added?  Maybe should just revert to the
 * 			World holding everything, and renaming it.  But not sure.
 */
package importing.pieces;

import java.util.ArrayList;

public class Model {
	ArrayList<World> pieces;
	
	Model(World[] _pieces){
		pieces = new ArrayList<World>();
		
		for(World w: _pieces)
			this.pieces.add(w);
	}
	
	public Model(World w){
		pieces = new ArrayList<World>();
		
		this.pieces.add(w);
	}
	
	public void addWorld(World w){
		this.pieces.add(w);
	}
}
