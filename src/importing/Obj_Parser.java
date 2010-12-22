/*
 * 	Reads in obj files, a fairly simplistic model format.
 * 
 * 	//TODO: Rework so that it works with FileLoader.
 * 
 */
package importing;
import importing.pieces.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;


public class Obj_Parser extends Parser{
	private ArrayList<Float[]> vertexsets = new ArrayList<Float[]>(); // Vertex Coordinates
	private ArrayList<Float[]> vertexsetsnorms = new ArrayList<Float[]>(); // Vertex Coordinates Normals
	private ArrayList<Float[]> vertexsetstexs = new ArrayList<Float[]>(); // Vertex Coordinates Textures
	private ArrayList<int[]> faces = new ArrayList<int[]>(); // Array of Faces (vertex sets)
	private ArrayList<int[]> facestexs = new ArrayList<int[]>(); // Array of of Faces textures
	private ArrayList<int[]> facesnorms = new ArrayList<int[]>(); // Array of Faces normals
	//private ArrayList<float[]> groups = new ArrayList<float[]>(); // Array of groups
	
	private int numpolys = 0;
	
	//// Statistics for drawing ////
	public float toppoint = 0;		// y+
	public float bottompoint = 0;	// y-JM
	public float leftpoint = 0;		// x-
	public float rightpoint = 0;	// x+
	public float farpoint = 0;		// z-
	public float nearpoint = 0;		// z+	
	
	//private Model model;
	
	public Obj_Parser(BufferedReader ref, boolean centerit) {
		try {
			loadobject(ref);
			if (centerit) {
				centerit();
			}
			numpolys = faces.size();
			cleanup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cleanup() {
		vertexsets.clear();
		vertexsetsnorms.clear();
		vertexsetstexs.clear();
		faces.clear();
		facestexs.clear();
		facesnorms.clear();
	}
	
	private void loadobject(BufferedReader br) throws Exception {
		int linecounter = 0;
		try {
			String newline;
			
			while (((newline = br.readLine()) != null)) {
				linecounter++;
				newline = newline.trim();
				if (newline.length() > 0) {
					if (newline.charAt(0) == 'v' && newline.charAt(1) == ' ') {
						readVertex(newline.substring(2));
					} else if (newline.charAt(0) == 'v' && newline.charAt(1) == 't') {
						readTextureCoordinate(newline.substring(3));
					} else if (newline.charAt(0) == 'v' && newline.charAt(1) == 'n') {
						readVertexNormal(newline.substring(3));
					} else if (newline.charAt(0) == 'f' && newline.charAt(1) == ' ') {
						readFace(newline.substring(3));
					} else if (newline.charAt(0) == 'g') {
						//DO NOTHING FUUU
					}
				}
			}
			
		} catch (IOException e) {
			System.out.println("Failed to read file: " + br.toString());
			//System.exit(0);			
		} catch (NumberFormatException e) {
			System.out.println("Malformed OBJ (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
			//System.exit(0);
		}
		
	}
	
	private void centerit() {
		float xshift = (rightpoint-leftpoint) /2f;
		float yshift = (toppoint - bottompoint) /2f;
		float zshift = (nearpoint - farpoint) /2f;
		
		for (int i=0; i < vertexsets.size(); i++) {
			Float[] coords = new Float[4];
			
			coords[0] = ((vertexsets.get(i)))[0] - leftpoint - xshift;
			coords[1] = ((vertexsets.get(i)))[1] - bottompoint - yshift;
			coords[2] = ((vertexsets.get(i)))[2] - farpoint - zshift;
			
			vertexsets.set(i,coords); // = coords;
		}
		
	}
	
	public float getXWidth() {
		float returnval = 0;
		returnval = rightpoint - leftpoint;
		return returnval;
	}
	
	public float getYHeight() {
		float returnval = 0;
		returnval = toppoint - bottompoint;
		return returnval;
	}
	
	public float getZDepth() {
		float returnval = 0;
		returnval = nearpoint - farpoint;
		return returnval;
	}
	
	public int numpolygons() {
		return numpolys;
	}
	
	public void draw() {
		for (int i=0;i<faces.size();i++) {
			int[] tempfaces = (faces.get(i));
			int[] tempfacesnorms = (facesnorms.get(i));
			int[] tempfacestexs = (facestexs.get(i));
			
			//// Quad Begin Header ////
			int polytype;
			if (tempfaces.length == 3) {
				polytype = GL11.GL_TRIANGLES;
			} else if (tempfaces.length == 4) {
				polytype = GL11.GL_QUADS;
			} else {
				polytype = GL11.GL_POLYGON;
			}
			GL11.glBegin(polytype);	
				GL11.glColor3f(0.5f,0.5f,0.5f);
				for (int w=0;w<tempfaces.length;w++) {
					if (tempfacesnorms[w] != 0) {
						float normtempx = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
						float normtempy = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
						float normtempz = (vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
						GL11.glNormal3f(normtempx, normtempy, normtempz);
					}
					
					if (tempfacestexs[w] != 0) {
						float textempx = (vertexsetstexs.get(tempfacestexs[w] - 1))[0];
						float textempy = (vertexsetstexs.get(tempfacestexs[w] - 1))[1];
						float textempz = (vertexsetstexs.get(tempfacestexs[w] - 1))[2];
						GL11.glTexCoord3f(textempx,1f-textempy,textempz);
					}
					
					float tempx = (vertexsets.get(tempfaces[w] - 1))[0];
					float tempy = (vertexsets.get(tempfaces[w] - 1))[1];
					float tempz = (vertexsets.get(tempfaces[w] - 1))[2];
					GL11.glVertex3f(tempx,tempy,tempz);
				}
			GL11.glEnd();
		}

	}
	
	private void readVertex(String data) throws Exception{
		Float[] coords = new Float[4];
		String[] coordstext = new String[4];
		coordstext = data.split("\\s+");
		
		if( coordstext.length != 4 )
			throw new Exception();
	
		for (int i = 1;i < coordstext.length;i++) {
			coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
		}
		
		vertexsets.add(coords);
	}

	private void readTextureCoordinate(String data) throws Exception{
		Float[] coords = new Float[4];
		String[] coordstext = new String[4];
		coordstext = data.split("\\s+");
		
		if( coordstext.length != 4 )
			throw new Exception();
		
		for (int i = 1;i < coordstext.length;i++) {
			coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
		}
		vertexsetstexs.add(coords);
	}
	
	private void readVertexNormal(String data) throws Exception{
		Float[] coords = new Float[4];
		String[] coordstext = new String[4];
		coordstext = data.split("\\s+");
		for (int i = 1;i < coordstext.length;i++) {
			coords[i-1] = Float.valueOf(coordstext[i]).floatValue();
		}
		vertexsetsnorms.add(coords);
	}
	
	private void readFace(String data) throws Exception{
		int[] v = new int[3];
		int[] vt = new int[3];
		int[] vn = new int[3];
		String[] coordstext = data.split("\\s+");
		
		//Loop through data, finding vertexes, vertex textures, vertex normals
		for (int i = 1;i < coordstext.length;i++) {
			String fixstring = coordstext[i].replaceAll("//","/0/");
			String[] tempstring = fixstring.split("/");
			v[i-1] = Integer.valueOf(tempstring[0]).intValue();
			if (tempstring.length > 1) {
				vt[i-1] = Integer.valueOf(tempstring[1]).intValue();
			} else {
				vt[i-1] = 0;
			}
			if (tempstring.length > 2) {
				vn[i-1] = Integer.valueOf(tempstring[2]).intValue();
			} else {
				vn[i-1] = 0;
			}
		}
		
		Float[][] verts = new Float[v.length][3];
		for(int i = 0; i < v.length; i++){
			verts[i] = vertexsets.get(v[i]);
		}
		Float[][] vertnorms = new Float[vn.length][3];
		for(int i = 0; i < vn.length; i++){
			vertnorms[i] = vertexsetsnorms.get(vn[i]);
		}
		//Face face = new Face(verts, vertnorms);
	}
	
	@Override
	public void readFile(String fileName) {/*TODO*/ }

	@Override
	public Model createModel() {return null; /*new Model(world);*/}
}