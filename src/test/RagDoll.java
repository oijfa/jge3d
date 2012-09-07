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

public class RagDoll extends Entity {
    private HashMap<String, RigidBody> limbs;
    private HashMap<String, ConeTwistConstraint> constraints;
    
	public RagDoll(float mass, boolean collide, Model model, Shader shader) {
		super(mass, collide, model, shader);
		setLimbs(new HashMap<String,RigidBody>());
		setConstraints(new HashMap<String,ConeTwistConstraint>());
		createRagDoll();
	}

    private void createRagDoll() {
    	RigidBody shoulders = createLimb(0.2f, 1.0f, new Vector3f(0.00f, 1.5f, 0), true);
    	RigidBody uArmL = createLimb(0.2f, 0.5f, new Vector3f(-0.75f, 0.8f, 0), false);
    	RigidBody uArmR = createLimb(0.2f, 0.5f, new Vector3f(0.75f, 0.8f, 0), false);
    	RigidBody lArmL = createLimb(0.2f, 0.5f, new Vector3f(-0.75f, -0.2f, 0), false);
        RigidBody lArmR = createLimb(0.2f, 0.5f, new Vector3f(0.75f, -0.2f, 0), false);
        RigidBody body = createLimb(0.2f, 1.0f, new Vector3f(0.00f, 0.5f, 0), false);
        RigidBody hips = createLimb(0.2f, 0.5f, new Vector3f(0.00f, -0.5f, 0), true);
        RigidBody uLegL = createLimb(0.2f, 0.5f, new Vector3f(-0.25f, -1.2f, 0), false);
        RigidBody uLegR = createLimb(0.2f, 0.5f, new Vector3f(0.25f, -1.2f, 0), false);
        RigidBody lLegL = createLimb(0.2f, 0.5f, new Vector3f(-0.25f, -2.2f, 0), false);
        RigidBody lLegR = createLimb(0.2f, 0.5f, new Vector3f(0.25f, -2.2f, 0), false);

        join(body, shoulders, new Vector3f(0f, 1.4f, 0));
        join(body, hips, new Vector3f(0f, -0.5f, 0));

        join(uArmL, shoulders, new Vector3f(-0.75f, 1.4f, 0));
        join(uArmR, shoulders, new Vector3f(0.75f, 1.4f, 0));
        join(uArmL, lArmL, new Vector3f(-0.75f, .4f, 0));
        join(uArmR, lArmR, new Vector3f(0.75f, .4f, 0));

        join(uLegL, hips, new Vector3f(-.25f, -0.5f, 0));
        join(uLegR, hips, new Vector3f(.25f, -0.5f, 0));
        join(uLegL, lLegL, new Vector3f(-.25f, -1.7f, 0));
        join(uLegR, lLegR, new Vector3f(.25f, -1.7f, 0));
    }

    private RigidBody createLimb(float width, float height, Vector3f location, boolean rotate) {
    	RigidBody node;
        int axis = rotate ? 0 : 1;
        switch(axis) {
        	case 0: node = createRigidBody(1.0f,  new CapsuleShapeX(width, height));break;
        	case 1: node = createRigidBody(1.0f,  new CapsuleShape(width, height));break;
        	default: node = createRigidBody(1.0f,  new CapsuleShape(width, height));break;
        }
        return node;
    }

    private ConeTwistConstraint join(RigidBody A, RigidBody B, Vector3f connectionPoint) {
    	Matrix3f mat = new Matrix3f();
    	mat.m00 = connectionPoint.x;
    	mat.m11 = connectionPoint.y;
    	mat.m22 = connectionPoint.z;
    	
    	Transform transform = new Transform(mat);
        ConeTwistConstraint joint = new ConeTwistConstraint(A, B, transform, transform);
        joint.setLimit(1f, 1f, 0);
        return joint;
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

	public HashMap<String, RigidBody> getLimbs() {
		return limbs;
	}

	public void setLimbs(HashMap<String, RigidBody> limbs) {
		this.limbs = limbs;
	}

	public HashMap<String, ConeTwistConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(HashMap<String, ConeTwistConstraint> constraints) {
		this.constraints = constraints;
	}
}
