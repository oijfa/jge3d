package input;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;

import entity.Camera;
import entity.EntityList;

public class Input {
	private Camera camera;
	private EntityList objectList;
	private int deltaX;
	private int deltaY;
	
	//ANGLES
	private static final float LEFT_RIGHT_INC = 0.00001f;
	private static final float UP_DOWN_INC = 0.00001f;
	
	//DISTANCE
	private static final float IN_OUT_INC = 0.1f;
	
	public Input (EntityList objectList){
		this.objectList = objectList;
	}
	
	public void init(){
		try {
			Keyboard.create();
			//Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Keyboard.poll();
		Keyboard.enableRepeatEvents(false);
	}
	
	public void run(){
		Keyboard.poll();
		if(camera != null) {
			handleMouse();
			if(Mouse.isInsideWindow()) {
				handleKeyboard();
			}
		} else {
			camera = (Camera) objectList.getItem(Camera.CAMERA_NAME);
		}
			
	}
	
	private void handleKeyboard(){
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.incrementRotation(LEFT_RIGHT_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			camera.incrementRotation(-LEFT_RIGHT_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			camera.incrementDeclination(UP_DOWN_INC);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.incrementDeclination(-UP_DOWN_INC);
		}
	}
	
	private void handleMouse(){
		while(Mouse.next())	{
			Mouse.poll();
			
			//update the changes in position
			deltaX = Mouse.getEventDX();
			deltaY = Mouse.getEventDY();

			switch(Mouse.getEventButton()) {
				case -1://Mouse Movement
					if(Mouse.isInsideWindow()) {
						//Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
						if(Mouse.isButtonDown(0)) {
							//Pan camera Z
							camera.incrementDistance(-1.0f*deltaY);	
						}
						
						if(Mouse.isButtonDown(1)) {
							//Change angle of camera
	
						}
						
						if(Mouse.isButtonDown(2)) {
							//Change Perspective
							camera.incrementDeclination(-deltaY*.01f);
							camera.incrementRotation(-deltaX*.01f);
						}
					}
					break;
				case 0://Left Button
					if( Mouse.isButtonDown(0) )	{
					} else {
					}
					break;
				case 1://Right Button
					if( !(Mouse.isButtonDown(1)) ) {
					}
					break;
				case 2://Middle Button
					if( !(Mouse.isButtonDown(1)) ) {
					}
					break;
			}

			switch(Mouse.getDWheel()) {
				case -120: 
					camera.incrementDistance(IN_OUT_INC);
					break;
				case  120: 
					camera.incrementDistance(-IN_OUT_INC);
					break;
			}
		}
	}
	
	public void mouseFunc(int button, int state, int x, int y) {
		//printf("button %i, state %i, x=%i,y=%i\n",button,state,x,y);
		//button 0, state 0 means left mouse down

		Vector3f rayTo = new Vector3f(getRayTo(x, y,camera));

		switch (button) {
			case 2: {
				if (state == 0) {
					shootBox(rayTo);
				}
				break;
			}
			case 1: {
				if (state == 0) {
					// apply an impulse
					if (dynamicsWorld != null) {
						CollisionWorld.ClosestRayResultCallback rayCallback = new CollisionWorld.ClosestRayResultCallback(cameraPosition, rayTo);
						dynamicsWorld.rayTest(cameraPosition, rayTo, rayCallback);
						if (rayCallback.hasHit()) {
							RigidBody body = RigidBody.upcast(rayCallback.collisionObject);
							if (body != null) {
								body.setActivationState(CollisionObject.ACTIVE_TAG);
								Vector3f impulse = new Vector3f(rayTo);
								impulse.normalize();
								float impulseStrength = 10f;
								impulse.scale(impulseStrength);
								Vector3f relPos = new Vector3f();
								relPos.sub(rayCallback.hitPointWorld, body.getCenterOfMassPosition(new Vector3f()));
								body.applyImpulse(impulse, relPos);
							}
						}
					}
				}
				else {
				}
				break;
			}
			case 0: {
				if (state == 0) {
					// add a point to point constraint for picking
					physics.

				}
				else {

					if (pickConstraint != null && dynamicsWorld != null) {
						dynamicsWorld.removeConstraint(pickConstraint);
						// delete m_pickConstraint;
						//printf("removed constraint %i",gPickingConstraintId);
						pickConstraint = null;
						pickedBody.forceActivationState(CollisionObject.ACTIVE_TAG);
						pickedBody.setDeactivationTime(0f);
						pickedBody = null;
					}
				}
				break;
			}
			default: {
			}
		}
	}
	
	public void mouseMotionFunc(int x, int y) {
		if (pickConstraint != null) {
			// move the constraint pivot
			Point2PointConstraint p2p = (Point2PointConstraint) pickConstraint;
			if (p2p != null) {
				// keep it at the same picking distance

				Vector3f newRayTo = new Vector3f(getRayTo(x, y));
				Vector3f eyePos = new Vector3f(cameraPosition);
				Vector3f dir = new Vector3f();
				dir.sub(newRayTo, eyePos);
				dir.normalize();
				dir.scale(BulletStats.gOldPickingDist);

				Vector3f newPos = new Vector3f();
				newPos.add(eyePos, dir);
				p2p.setPivotB(newPos);
			}
		}
	}

}
