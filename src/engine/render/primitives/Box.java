package engine.render.primitives;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Face;
import engine.render.model_pieces.Mesh;
import engine.render.ubos.Material;

public class Box extends Entity {
	private Model model;
	private Vector3f halfExtent;
	
	public Box(float mass, boolean collidable, Vector3f dimensions, Shader shader) {
		this.halfExtent = dimensions;
		halfExtent.scale(0.5f);
		model = new Model(shader);
		createModel();
		CollisionShape shape = new BoxShape(halfExtent);
		shape.setMargin(0.04f);
		model.setCollisionShape(shape);
		this.setProperty(Entity.MODEL, model);
		initialSetup((String)data.get(Entity.NAME), mass, collidable, model, shader);
	}
	
	public Model createModel() {
		if(halfExtent != null) {
			Mesh mesh = new Mesh();
			
			//left side top triangle
			Vector3f[] vertices = new Vector3f[3];
			vertices[0] = new Vector3f(-halfExtent.x,halfExtent.y,-halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,-halfExtent.y,-halfExtent.z);
			vertices[2] = new Vector3f(-halfExtent.x,halfExtent.y,halfExtent.z);
			mesh.addFace(new Face(vertices));
			//left side bottom triangle
			vertices[0] = new Vector3f(-halfExtent.x,-halfExtent.y,-halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(-halfExtent.x,halfExtent.y,halfExtent.z);
			mesh.addFace(new Face(vertices));
			
			//right side top triangle
			vertices[0] = new Vector3f(halfExtent.x,halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			//right side bottom triangle
			vertices[0] = new Vector3f(halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,-halfExtent.y,-halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			
			//top side left triangle
			vertices[0] = new Vector3f(-halfExtent.x,halfExtent.y,-halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			//top side right triangle
			vertices[0] = new Vector3f(-halfExtent.x,halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			
			//bottom side left triangle
			vertices[0] = new Vector3f(-halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,-halfExtent.y,-halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,-halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			//bottom side right triangle
			vertices[0] = new Vector3f(-halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,-halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			
			//front side top triangle
			vertices[0] = new Vector3f(-halfExtent.x,halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,halfExtent.z);
			mesh.addFace(new Face(vertices));
			//front side bottom triangle
			vertices[0] = new Vector3f(-halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,-halfExtent.y,halfExtent.z);
			vertices[2] = new Vector3f(halfExtent.x,halfExtent.y,halfExtent.z);
			mesh.addFace(new Face(vertices));
			
			//rear side top triangle
			vertices[0] = new Vector3f(halfExtent.x,-halfExtent.y,-halfExtent.z);
			vertices[1] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			vertices[2] = new Vector3f(-halfExtent.x,-halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			//rear side bottom triangle
			vertices[0] = new Vector3f(halfExtent.x,halfExtent.y,-halfExtent.z);
			vertices[1] = new Vector3f(-halfExtent.x,halfExtent.y,-halfExtent.z);
			vertices[2] = new Vector3f(-halfExtent.x,-halfExtent.y,-halfExtent.z);
			mesh.addFace(new Face(vertices));
			mesh.setMaterial(new Material());
			
			mesh.calcNormals(true);
			
			model.addMesh(mesh);
		}
		
		return model;
	}
}
