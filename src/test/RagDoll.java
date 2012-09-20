package test;

import java.util.HashMap;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CapsuleShapeX;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConeTwistConstraint;
import com.bulletphysics.linearmath.Transform;

import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.primitives.Box;

public class RagDoll extends Entity {
    private HashMap<String, ConeTwistConstraint> constraints;
    private Shader shader;
    private Vector3f position;
    
	public RagDoll(float mass, boolean collide, Vector3f location, Model model, Shader shader) {
		setProperty(Entity.COLLIDABLE, false);
		setProperty(Entity.SHOULD_DRAW, false);
		setProperty(Entity.POSITION, location);
		position = location;
		setConstraints(new HashMap<String,ConeTwistConstraint>());
		this.shader = shader;
		createRagDoll();
	}

	private void createRagDoll() {
    	this.addSubEntity("shoulders",createLimb("shoulders", 1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  1.5f, 0), true));
    	this.addSubEntity("uArmL",createLimb("uArmL",    	  1.0f, 0.2f, 0.5f, new Vector3f(-0.75f, 0.8f, 0), false));
    	this.addSubEntity("uArmR",createLimb("uArmR",    	  1.0f, 0.2f, 0.5f, new Vector3f(0.75f,  0.8f, 0), false));
    	this.addSubEntity("lArmL",createLimb("lArmL",    	  1.0f, 0.2f, 0.5f, new Vector3f(-0.75f,-0.2f, 0), false));
    	this.addSubEntity("lArmR",createLimb("lArmR",   	  1.0f, 0.2f, 0.5f, new Vector3f(0.75f, -0.2f, 0), false));
    	this.addSubEntity("body", createLimb("body",		  1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  0.5f, 0), false));
    	this.addSubEntity("hips", createLimb("hips",    	  1.0f, 0.2f, 0.5f, new Vector3f(0.00f, -0.5f, 0), true));
    	this.addSubEntity("uLegL",createLimb("uLegL",  	 	  1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-1.2f, 0), false));
    	this.addSubEntity("uLegR",createLimb("uLegR",		  1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -1.2f, 0), false));
    	this.addSubEntity("lLegL",createLimb("lLegL",     	  1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-2.2f, 0), false));
    	this.addSubEntity("lLegR",createLimb("lLegR",     	  1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -2.2f, 0), false));
        
        constraints.putAll(join(subEntities.getItem("body"), subEntities.getItem("shoulders"), new Vector3f(0f, 1.4f, 0)));
        constraints.putAll(join(subEntities.getItem("body"), subEntities.getItem("hips"), new Vector3f(0f, -0.5f, 0)));

        constraints.putAll(join(subEntities.getItem("uArmL"), subEntities.getItem("shoulders"), new Vector3f(-0.75f, 1.4f, 0)));
        constraints.putAll(join(subEntities.getItem("uArmR"), subEntities.getItem("shoulders"), new Vector3f(0.75f, 1.4f, 0)));
        constraints.putAll(join(subEntities.getItem("uArmL"), subEntities.getItem("lArmL"), new Vector3f(-0.75f, .4f, 0)));
        constraints.putAll(join(subEntities.getItem("uArmR"), subEntities.getItem("lArmR"), new Vector3f(0.75f, .4f, 0)));

        constraints.putAll(join(subEntities.getItem("uLegL"), subEntities.getItem("body"), new Vector3f(-.25f, -0.5f, 0)));
        constraints.putAll(join(subEntities.getItem("uLegR"), subEntities.getItem("body"), new Vector3f(.25f, -0.5f, 0)));
        constraints.putAll(join(subEntities.getItem("uLegL"), subEntities.getItem("lLegL"), new Vector3f(-.25f, -1.7f, 0)));
        constraints.putAll(join(subEntities.getItem("uLegR"), subEntities.getItem("lLegR"), new Vector3f(.25f, -1.7f, 0)));
    }

    private Entity createLimb(String name, float width, float height, float mass, Vector3f location, boolean rotate) {
    	RigidBody node;
        int axis = rotate ? 1 : 0;
        switch(axis) {
        	case 0: node = createRigidBody(mass,  new CapsuleShapeX(width, height));break;
        	case 1: node = createRigidBody(mass,  new CapsuleShape(width, height));break;
        	default: node = createRigidBody(mass,  new CapsuleShape(width, height));break;
        }
        
        Box box = new Box(
			mass,
			true,
			new Vector3f(
				width, 
				height, 
				width
			),
			shader
		);
        
    	Entity ent = new Entity(
    		name, 
    		mass, 
    		true, 
    		(Model)box.getProperty(Entity.MODEL),
    		shader
    	);
    	ent.setProperty(Entity.COLLISION_OBJECT, node);
    	
    	Vector3f adjusted_pos = new Vector3f(position);
    	adjusted_pos.add(location);
    	
    	ent.setProperty(Entity.POSITION, adjusted_pos);
    	
        return ent;
    }

    private  HashMap<String, ConeTwistConstraint> join(Entity A, Entity B, Vector3f connectionPoint) {
    	Matrix3f mat = new Matrix3f();
    	mat.m00 = connectionPoint.x;
    	mat.m11 = connectionPoint.y;
    	mat.m22 = connectionPoint.z;
    	
    	Transform transform = new Transform(mat);
        ConeTwistConstraint joint = new ConeTwistConstraint(
        	(RigidBody)A.getProperty(Entity.COLLISION_OBJECT), 
        	(RigidBody)B.getProperty(Entity.COLLISION_OBJECT), 
        	transform, 
        	transform
        );
        joint.setLimit(100f, 100f, 0);
        
        HashMap<String, ConeTwistConstraint> join = new HashMap<String, ConeTwistConstraint>();
        join.put(A.getProperty(Entity.NAME)+":"+B.getProperty(Entity.NAME),joint);
        
        return join;
    }
/*
    @Override
    public void update(float tpf) {
        if (applyForce) {
            shoulders.getControl(RigidBodyControl.class).applyForce(upforce, Vector3f.ZERO);
        }
    }
*/

	public HashMap<String, ConeTwistConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(HashMap<String, ConeTwistConstraint> constraints) {
		this.constraints = constraints;
	}
}
