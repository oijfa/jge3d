/*
 * 	This Class is for loading in Model files.  It will choose what file parser to use
 * 		based on the file extension, which will load the file's data into a generic
 * 		Model object that the renederer/physics classes can understand.
 * 
 * 	//TODO:  XGL parser isn't exactly complete
 * 	//TODO:  Implement parsers for formats other than shitty xgl.
 */
package importing;

import importing.pieces.Model;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class FileLoader {
	public FileLoader(){}
	
	public static Model loadFile(String[] filePaths) throws LWJGLException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		String[] temp;
		Parser parser = null;
		for(int i = 0; i < filePaths.length; i++){
			//Choose the parser to use based on the file extension
			temp = filePaths[i].split("\\.");
			if( temp[temp.length-1].toLowerCase().equals("xgl"))
			{
				parser = new XGL_Parser();
			}else{
				System.out.println("No Parser implemented for that file type.");
			}
			
			try {
				parser.readFile(filePaths[i]);
			} catch (Exception e) {
				System.out.println("Unable to load Model file " + filePaths[i]);
				e.printStackTrace();
			}
		}
		return parser.createModel();
	}
	
	/*	
	 * 	Create a display list from the parser, rather than returning a model
	 * 		This type of thing turns out to be very inefficient when there are lots of 
	 * 		things on the screen, but its fairly useful as a debug/test thing.
	 */
	static int drawToList(Parser parser) throws LWJGLException
	{
		
		Display.makeCurrent();
		
		int listID = GL11.glGenLists(1);
		GL11.glNewList(listID,GL11.GL_COMPILE);
		
		parser.getWorld().draw();
		//drawDebug();

		GL11.glEndList();
		Display.releaseContext();
		
		return listID;
		
	}
	
	//Draw a pyramid.  Wheeeeeeee  (or well, I assume that's what its drawing)
	public static void drawDebug()
	{
		GL11.glBegin(GL11.GL_TRIANGLES);
		 
		// Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
	 
		// Right Side Facing Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		// Left Side Facing Front
		GL11.glColor3f(0.0f, 1.0f, 1.0f); 
		GL11.glVertex3f(0.0f, 1.0f, 0.0f);
		GL11.glColor3f(0.0f, 0.0f, 1.0f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
	 
		// Bottom
		GL11.glColor3f(0.0f, 0.0f, 0.0f); 
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.1f, 0.1f, 0.1f); 
		GL11.glVertex3f(1.0f, -1.0f, 1.0f);
		GL11.glColor3f(0.2f, 0.2f, 0.2f); 
		GL11.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		GL11.glEnd();
	}
}