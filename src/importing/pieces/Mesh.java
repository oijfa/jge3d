package importing.pieces;


import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Mesh {
	private HashMap<Integer,Material> materials;
	private HashMap<Integer,Point> points;
	private HashMap<Integer,Normal> normals;
	private HashMap<Integer,Patch> patches;
	private int face_size=3;
	private boolean per_vertex_normals = false;
	private int reference;
		
	public Mesh() {
		materials = new HashMap<Integer, Material>();
		points = new HashMap<Integer, Point>();
		normals = new HashMap<Integer, Normal>();
		patches = new HashMap<Integer, Patch>();
	}
	
	public void setVertexNormals(boolean vertnorms){
		per_vertex_normals = vertnorms;
	}
	public boolean getVertexNormals(){
		return per_vertex_normals;
	}
	
	public void addPoint(Point point) {
		points.put(point.getReference(),point);
	}
	
	public void addNormal(Normal normal) {
		normals.put(normal.getReference(),normal);
	}
	
	public void addPatch(Patch patch) {
		patches.put(patch.getReference(),patch);
	}
	
	public void addMaterial(Material material) {
		materials.put(material.getReference(),material);
	}
	
	public Point getPoint(int index) {
		return points.get(index);
	}
	
	public Normal getNormal(int index) {
		return normals.get(index);
	}

	public Patch getPatch(int index) {
		return patches.get(index);
	}
	
	public Material getMaterial(int index) {
		return materials.get(index);
	}
	
	
	public void draw() throws LWJGLException
	{
		//System.out.print("===========Drawing Mesh: " + reference + "==================\n");
		int polytype;
		Vector3f point = new Vector3f();
		Vector3f[] normal = new Vector3f[3];
		Vector3f face_normal = new Vector3f();
		Vector3f vert_normal = new Vector3f();
		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();

		//Determine shape
		if (face_size == 3) {
			polytype = GL11.GL_TRIANGLES;
		} else if (face_size == 4) {
			polytype = GL11.GL_QUADS;
		} else {
			polytype = GL11.GL_POLYGON;
		}
		
		GL11.glBegin(polytype);	
			
		//float rand1 = (float) Math.random();
		//float rand2 = (float) Math.random();
		//float rand3 = (float) Math.random();
			
		GL11.glColor3f(0.5f, 0.5f, 0.5f);
		for ( Map.Entry<Integer, Patch> patchEntry : patches.entrySet()) {
			
			for (Face face : patchEntry.getValue().getFaces())
			{
				//rand1 = (float) Math.random();
				//rand2 = (float) Math.random();
				//rand3 = (float) Math.random();
				//GL11.glColor3f(rand1,rand2,rand3);
				GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, materials.get(face.getMaterial()).getShine());
					
				if(per_vertex_normals) {
					for(int i=0; i<3;i++) {
						
						
						vert_normal = points.get(face.getPointRefs().get(i)).getPosition();
						
						GL11.glNormal3f(
								vert_normal.x,
								vert_normal.y,
								vert_normal.z
						);
						
						point = points.get(face.getPointRefs().get(i)).getPosition();
						GL11.glVertex3f(
							point.x,
							point.y,
							point.z
						);
					}

					
				} else {
					for(int i=0; i<3;i++) {
						normal[i] = points.get(face.getPointRefs().get(i)).getPosition();
					}
					
					
					a.sub(normal[0],normal[1]);
					b.sub(normal[1],normal[2]);
					
					face_normal.cross( a, b );
					
					GL11.glNormal3f(
							face_normal.x,
							face_normal.y,
							face_normal.z
					);
					
					for(int i=0; i<face.getPointRefs().size();i++) {
						point = points.get(face.getPointRefs().get(i)).getPosition();
						GL11.glVertex3f(
							point.x,
							point.y,
							point.z
						);
					}
				}
			}
		}
		GL11.glEnd();
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public int getReference() {
		return reference;
	}
}
