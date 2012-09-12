package engine.render.primitives;

import javax.vecmath.Vector3f;

import engine.render.Model;
import engine.render.model_pieces.Face;
import engine.render.model_pieces.Mesh;
import engine.render.ubos.Material;

public class Box extends Model {
	private Vector3f dimensions;
	
	public Box(Vector3f dimensions) {
		super();
		this.dimensions = dimensions;
		createModel();
	}
	
	public Model createModel() {
		if(dimensions != null) {
			Mesh mesh = new Mesh();
			
			//left side top triangle
			Vector3f[] vertices = new Vector3f[3];
			vertices[0] = new Vector3f(-dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//left side bottom triangle
			vertices[0] = new Vector3f(-dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			
			//right side top triangle
			vertices[0] = new Vector3f(dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//right side bottom triangle
			vertices[0] = new Vector3f(dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			
			//top side left triangle
			vertices[0] = new Vector3f(-dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//top side right triangle
			vertices[0] = new Vector3f(dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			mesh.addFace(new Face(vertices));
			
			//bottom side left triangle
			vertices[0] = new Vector3f(dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//bottom side right triangle
			vertices[0] = new Vector3f(-dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			
			//front side top triangle
			vertices[0] = new Vector3f(-dimensions.x/2,dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//front side bottom triangle
			vertices[0] = new Vector3f(-dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,-dimensions.y/2,dimensions.z/2);
			vertices[2] = new Vector3f(dimensions.x/2,dimensions.y/2,dimensions.z/2);
			mesh.addFace(new Face(vertices));
			
			//rear side top triangle
			vertices[0] = new Vector3f(dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			vertices[1] = new Vector3f(dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			mesh.addFace(new Face(vertices));
			//rear side bottom triangle
			vertices[0] = new Vector3f(dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			vertices[1] = new Vector3f(-dimensions.x/2,dimensions.y/2,-dimensions.z/2);
			vertices[2] = new Vector3f(-dimensions.x/2,-dimensions.y/2,-dimensions.z/2);
			mesh.addFace(new Face(vertices));
			mesh.setMaterial(new Material());
			this.addMesh(mesh);
		}
		
		return this;
	}

}
