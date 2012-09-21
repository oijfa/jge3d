package test;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CapsuleShapeX;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConeTwistConstraint;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.Transform;

import engine.entity.Entity;
import engine.render.Model;
import engine.render.Shader;
import engine.render.primitives.Box;

public class RagDoll extends Entity {
    private Shader shader;
    private Vector3f position;
    
	public RagDoll(float mass, boolean collide, Vector3f location, Model model, Shader shader) {
		setProperty(Entity.COLLIDABLE, false);
		setProperty(Entity.SHOULD_DRAW, false);
		setProperty(Entity.POSITION, location);
		position = location;
		this.shader = shader;
		createRagDoll();
	}

	private void createRagDoll() {
    	this.addSubEntity("shoulders",createLimb("shoulders", 1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  1.5f, 0), true));
    	//this.addSubEntity("uArmL",	  createLimb("uArmL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.75f, 0.8f, 0), false));
    	//this.addSubEntity("uArmR",	  createLimb("uArmR",     1.0f, 0.2f, 0.5f, new Vector3f(0.75f,  0.8f, 0), false));
    	//this.addSubEntity("lArmL",	  createLimb("lArmL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.75f,-0.2f, 0), false));
    	//this.addSubEntity("lArmR",	  createLimb("lArmR",	  1.0f, 0.2f, 0.5f, new Vector3f(0.75f, -0.2f, 0), false));
    	this.addSubEntity("body", 	  createLimb("body",	  1.0f, 0.2f, 1.0f, new Vector3f(0.00f,  0.5f, 0), false));
    	//this.addSubEntity("hips", 	  createLimb("hips",      1.0f, 0.2f, 0.5f, new Vector3f(0.00f, -0.5f, 0), true));
    	//this.addSubEntity("uLegL",	  createLimb("uLegL",  	  1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-1.2f, 0), false));
    	//this.addSubEntity("uLegR",	  createLimb("uLegR",	  1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -1.2f, 0), false));
    	//this.addSubEntity("lLegL",	  createLimb("lLegL",     1.0f, 0.2f, 0.5f, new Vector3f(-0.25f,-2.2f, 0), false));
    	//this.addSubEntity("lLegR",	  createLimb("lLegR",     1.0f, 0.2f, 0.5f, new Vector3f(0.25f, -2.2f, 0), false));
        
    	HashMap<String, TypedConstraint> constraints = new HashMap<String, TypedConstraint>();
    	
        constraints.putAll(join(subEntities.getItem("body"), subEntities.getItem("shoulders"), new Vector3f(0f, 1.4f, 0)));
        //constraints.putAll(join(subEntities.getItem("body"), subEntities.getItem("hips"), new Vector3f(0f, -0.5f, 0)));

        //constraints.putAll(join(subEntities.getItem("uArmL"), subEntities.getItem("shoulders"), new Vector3f(-0.75f, 1.4f, 0)));
        //constraints.putAll(join(subEntities.getItem("uArmR"), subEntities.getItem("shoulders"), new Vector3f(0.75f, 1.4f, 0)));
        //constraints.putAll(join(subEntities.getItem("uArmL"), subEntities.getItem("lArmL"), new Vector3f(-0.75f, .4f, 0)));
        //constraints.putAll(join(subEntities.getItem("uArmR"), subEntities.getItem("lArmR"), new Vector3f(0.75f, .4f, 0)));

        //constraints.putAll(join(subEntities.getItem("uLegL"), subEntities.getItem("body"), new Vector3f(-.25f, -0.5f, 0)));
        //constraints.putAll(join(subEntities.getItem("uLegR"), subEntities.getItem("body"), new Vector3f(.25f, -0.5f, 0)));
        //constraints.putAll(join(subEntities.getItem("uLegL"), subEntities.getItem("lLegL"), new Vector3f(-.25f, -1.7f, 0)));
        //constraints.putAll(join(subEntities.getItem("uLegR"), subEntities.getItem("lLegR"), new Vector3f(.25f, -1.7f, 0)));
        
        setProperty(Entity.CONSTRAINTS, constraints);
    }

    private Entity createLimb(String name, float mass, float width, float height, Vector3f location, boolean rotate) {
    	RigidBody node;
    	Box box;
    	//mass=0;
        int axis = rotate ? 0 : 1;
        switch(axis) {
        	case 0: //X axis
        		node = createRigidBody(mass,  new CapsuleShapeX(width, height));
    			box = new Box(
        			mass,
        			true,
        			new Vector3f(
    					height, 
    					width, 
        				width
        			),
        			shader
        		);
    			break;
        	case 1: //Y axis 
        		 node = createRigidBody(mass,  new CapsuleShape(width, height));
        		 box = new Box(
        			mass,
        			true,
        			new Vector3f(
    					width, 
    					height, 
    					width
        			),
        			shader
        		);
        		break;
        	default: 
        		node = createRigidBody(mass,  new CapsuleShape(width, height));
        		box = new Box(
        			mass,
        			true,
        			new Vector3f(
        				width, 
        				height, 
        				width
        			),
        			shader
        		);
        		break;
        }
       
    	Entity ent = new Entity(
    		name, 
    		mass, 
    		true, 
    		(Model)box.getProperty(Entity.MODEL),
    		shader
    	);
    	ent.setProperty(Entity.COLLISION_OBJECT, node);
    	//ent.setProperty(Entity.GRAVITY, new Vector3f(0,-10f,0));
    	
    	Vector3f adjusted_pos = new Vector3f(position);
    	adjusted_pos.add(location);
    	
    	ent.setProperty(Entity.POSITION, adjusted_pos);
    	
        return ent;
    }

    private  HashMap<String, TypedConstraint> join(Entity A, Entity B, Vector3f connectionPoint) {
    	//Vector3f adjusted_pos = new Vector3f(position);
    	//adjusted_pos.add(connectionPoint);

    	Transform transform = new Transform();
    	transform.setIdentity();
    	transform.origin.set(connectionPoint);
    	/*
    	ConeTwistConstraint joint = new ConeTwistConstraint(
        	(RigidBody)A.getProperty(Entity.COLLISION_OBJECT), 
        	(RigidBody)B.getProperty(Entity.COLLISION_OBJECT), 
        	transform, 
        	transform      	
        );
    	joint.setLimit(1, 1, 0);
    	*/
    	
        Generic6DofConstraint joint = new Generic6DofConstraint(
        	(RigidBody)A.getProperty(Entity.COLLISION_OBJECT), 
        	(RigidBody)B.getProperty(Entity.COLLISION_OBJECT), 
        	transform, 
        	transform,
        	true        	
        );
        
        joint.setAngularLowerLimit(
        	new Vector3f(
        		-BulletGlobals.SIMD_EPSILON,
        		-BulletGlobals.SIMD_EPSILON,
        		-BulletGlobals.SIMD_EPSILON
        	)
        );
        joint.setAngularUpperLimit(
        	new Vector3f(
        		BulletGlobals.SIMD_EPSILON,
        		BulletGlobals.SIMD_EPSILON,
        		BulletGlobals.SIMD_EPSILON
        	)
        );
        
        /*
        joint.setAngularLowerLimit(
        	new Vector3f(
        		-BulletGlobals.SIMD_PI*0.8f,
        		-BulletGlobals.SIMD_EPSILON,
        		-BulletGlobals.SIMD_PI*0.5f
        	)
        );
        joint.setAngularLowerLimit(
        	new Vector3f(
        		BulletGlobals.SIMD_PI*0.8f,
        		BulletGlobals.SIMD_EPSILON,
        		BulletGlobals.SIMD_PI*0.5f
        	)
        );
        */
        
        HashMap<String, TypedConstraint> join = new HashMap<String, TypedConstraint>();
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
}
