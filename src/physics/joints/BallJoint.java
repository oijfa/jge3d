package physics.joints;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;

import entity.Entity;

public class BallJoint extends Point2PointConstraint {
	Entity entA;
	Entity entB;
	
 	public BallJoint(Entity rbA, Vector3f pivotInA){
		super(rbA,pivotInA);
		intiBallJoint(rbA,null);
	}
	public BallJoint(Entity rbA, Entity rbB, Vector3f pivotInA, Vector3f pivotInB){
		super(rbA,rbB,pivotInA,pivotInB);
		entA = rbA;
		entB = rbB;
		intiBallJoint(rbA,rbB);
	}
	public BallJoint(){
		super();
		intiBallJoint(null,null);
	}
	private void intiBallJoint(Entity a, Entity b){
		entA = a;
		entB = b;
	}
}
