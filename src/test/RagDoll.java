package test;

import java.util.HashMap;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

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
    private HashMap<String, Entity> limbs;
    private HashMap<String, ConeTwistConstraint> constraints;
    private Shader shader;
    
	public RagDoll(float mass, boolean collide, Model model, Shader shader) {
		//super(mass, collide, model, shader);
		setProperty(Entity.COLLIDABLE, false);
		setProperty(Entity.SHOULD_DRAW, false);
		setLimbs(new HashMap<String,Entity>());
		setConstraints(new HashMap<String,ConeTwistConstraint>());
		createRagDoll();
		this.shader = shader;
	}

	private void createRagDoll() {
    	limbs.put("shoulders",createLimb("shoulders", 1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  1.5f, 0), true));
    	limbs.put("uArmL",createLimb("uArmL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.75f, 0.8f, 0), false));
    	limbs.put("uArmR",createLimb("uArmR",     1.0f, 0.2f, 0.5f, new Vector3f(0.75f,  0.8f, 0), false));
    	limbs.put("lArmL",createLimb("lArmL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.75f,-0.2f, 0), false));
    	limbs.put("lArmR",createLimb("lArmR",     1.0f, 0.2f, 0.5f, new Vector3f(0.75f, -0.2f, 0), false));
    	limbs.put("body", createLimb("body",      1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  0.5f, 0), false));
    	limbs.put("hips", createLimb("hips",      1.0f, 0.2f, 0.5f, new Vector3f(0.00f, -0.5f, 0), true));
    	limbs.put("uLegL",createLimb("uLegL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-1.2f, 0), false));
    	limbs.put("uLegR",createLimb("uLegR",     1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -1.2f, 0), false));
    	limbs.put("lLegL",createLimb("lLegL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-2.2f, 0), false));
    	limbs.put("lLegR",createLimb("lLegR",     1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -2.2f, 0), false));
        
        constraints.putAll(join(limbs.get("body"), limbs.get("shoulders"), new Vector3f(0f, 1.4f, 0)));
        constraints.putAll(join(limbs.get("body"), limbs.get("hips"), new Vector3f(0f, -0.5f, 0)));

        constraints.putAll(join(limbs.get("uArmL"), limbs.get("shoulders"), new Vector3f(-0.75f, 1.4f, 0)));
        constraints.putAll(join(limbs.get("uArmR"), limbs.get("shoulders"), new Vector3f(0.75f, 1.4f, 0)));
        constraints.putAll(join(limbs.get("uArmL"), limbs.get("lArmL"), new Vector3f(-0.75f, .4f, 0)));
        constraints.putAll(join(limbs.get("uArmR"), limbs.get("lArmR"), new Vector3f(0.75f, .4f, 0)));

        constraints.putAll(join(limbs.get("uLegL"), limbs.get("body"), new Vector3f(-.25f, -0.5f, 0)));
        constraints.putAll(join(limbs.get("uLegR"), limbs.get("body"), new Vector3f(.25f, -0.5f, 0)));
        constraints.putAll(join(limbs.get("uLegL"), limbs.get("lLegL"), new Vector3f(-.25f, -1.7f, 0)));
        constraints.putAll(join(limbs.get("uLegR"), limbs.get("lLegR"), new Vector3f(.25f, -1.7f, 0)));
    }

    private Entity createLimb(String name, float width, float height, float mass, Vector3f location, boolean rotate) {
    	RigidBody node;
        int axis = rotate ? 0 : 1;
        switch(axis) {
        	case 0: node = createRigidBody(1.0f,  new CapsuleShapeX(width, height));break;
        	case 1: node = createRigidBody(1.0f,  new CapsuleShape(width, height));break;
        	default: node = createRigidBody(1.0f,  new CapsuleShape(width, height));break;
        }
        
    	Entity ent = new Entity(
    		name, 
    		mass, 
    		true, 
    		new Box(
    			new Vector3f(
    				width, 
    				height, 
    				width
    			)
    		),
    		shader
    	);
    	
    	ent.setProperty(Entity.COLLISION_OBJECT, node);
    	
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
        joint.setLimit(1f, 1f, 0);
        
        HashMap<String, ConeTwistConstraint> join = new HashMap<String, ConeTwistConstraint>();
        join.put(A.getProperty(Entity.NAME)+":"+B.getProperty(Entity.NAME),joint);
        return join;
    }
/*
    public void onAction(String string, boolean bln, float tpf) {
        if ("Pull ragdoll up".equals(string)) {
            if (bln) {
                shoulders.getControl(RigidBodyControl.class).activate();
                applyForce = true;
            } else {
                applyForce = false;
            }
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (applyForce) {
            shoulders.getControl(RigidBodyControl.class).applyForce(upforce, Vector3f.ZERO);
        }
    }
*/

	public HashMap<String, Entity> getLimbs() {
		return limbs;
	}

	public void setLimbs(HashMap<String, Entity> limbs) {
		this.limbs = limbs;
	}

	public HashMap<String, ConeTwistConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(HashMap<String, ConeTwistConstraint> constraints) {
		this.constraints = constraints;
	}
}
