package editor.window;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConeTwistConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.Transform;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.entity.Entity;

import engine.window.components.Window;
import engine.window.components.XYZAdjuster;

public class ConstraintMenu extends Window implements ActionListener {
	private ArrayList<ActionListener> action_listeners;
	private DialogLayout dialoglayout;
	private TypedConstraint constraint;
	
    private XYZAdjuster connection_point;
    private EntityComboBox entityA;
    private EntityComboBox entityB;
    
	public ConstraintMenu() {
		super();
		
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Constraint Menu");

		this.setTheme("constraintmenu");
		
		connection_point = new XYZAdjuster("Offset");
		connection_point.setTheme("xyzadjuster");
		
		dialoglayout = new DialogLayout();
		dialoglayout.setTheme("dialoglayout");

		// Reset temp row for vertical
		Group h_grid = dialoglayout.createParallelGroup(
			connection_point,
			entityA,
			entityB
		);
		
		Group v_grid = dialoglayout.createSequentialGroup(
			connection_point,
			entityA,
			entityB
		);
		
		// All Dialog layout groups must have both a HorizontalGroup and
		// VerticalGroup
		// Otherwise "incomplete" exception is thrown and layout is not applied
		dialoglayout.setHorizontalGroup(h_grid);
		dialoglayout.setVerticalGroup(v_grid);
		
		dialoglayout.setSize(350,800);
		
		//this.add(layout);
		this.add(dialoglayout);
		
		addCallbacks();
	}
	
	private void addCallbacks() {
		connection_point.addActionListener(this);
		entityA.addActionListener(this);
		entityB.addActionListener(this);
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}
	private void fireActionEvent(String event) {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this, event));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent("constraint");
	}
	
	public TypedConstraint getConstraint() {
		return constraint;
	}
	public void updateConstraint() {
		Vector3f adjusted_posA = new Vector3f((Vector3f)entityA.getEntity().getProperty(Entity.POSITION));
    	Vector3f adjusted_posB = new Vector3f((Vector3f)entityB.getEntity().getProperty(Entity.POSITION));
    	Transform posA = new Transform();
    	Transform posB = new Transform();
    	
    	adjusted_posA.sub(connection_point.getVector());
    	adjusted_posB.sub(connection_point.getVector());
    	
    	posA.setIdentity();
    	posB.setIdentity();
    	
    	posA.origin.set(adjusted_posA);
    	posB.origin.set(adjusted_posB);
    	
    	posA.invXform(connection_point.getVector(), posA.origin);
    	posB.invXform(connection_point.getVector(), posB.origin);

    	ConeTwistConstraint joint = new ConeTwistConstraint(
        	(RigidBody)entityA.getEntity().getProperty(Entity.COLLISION_OBJECT), 
        	(RigidBody)entityB.getEntity().getProperty(Entity.COLLISION_OBJECT), 
        	posA, 
        	posB      	
        );
        
        joint.setLimit(0, -BulletGlobals.SIMD_EPSILON, BulletGlobals.SIMD_EPSILON);
        joint.setLimit(1, -BulletGlobals.SIMD_EPSILON, BulletGlobals.SIMD_EPSILON);
        joint.setLimit(2, -BulletGlobals.SIMD_EPSILON, BulletGlobals.SIMD_EPSILON);
        joint.setLimit(3, -BulletGlobals.SIMD_PI, BulletGlobals.SIMD_PI);
        joint.setLimit(4, -BulletGlobals.SIMD_PI, BulletGlobals.SIMD_PI);
        joint.setLimit(5, -BulletGlobals.SIMD_PI, BulletGlobals.SIMD_PI);
		constraint = joint;
	}
	public void setConstraint(TypedConstraint constraint) {
		this.constraint = constraint; 
	}
}
