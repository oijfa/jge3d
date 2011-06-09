package game;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.matthiasmann.twl.Event;
import engine.entity.EntityList;
import engine.input.Input;
import engine.entity.Player;

public class GameInput extends Input {
	public GameInput(EntityList objectList) {
		super(objectList);
	}

	// ANGLES
	private static final double LEFT_RIGHT_INC = 0.00001f;
	private static final double UP_DOWN_INC = 0.00001f;

	// DISTANCE
	private static final double IN_OUT_INC = 1f;
	public boolean handleKeyboard(Event evt) {
		boolean event_caught=false;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.incrementRotation(LEFT_RIGHT_INC);
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.incrementRotation(-LEFT_RIGHT_INC);
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.incrementDeclination(UP_DOWN_INC);
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.incrementDeclination(-UP_DOWN_INC);
			event_caught=true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Player p1 = ((Player) objectList.getItem("player1"));
			p1.movePlayer(new Vector3f(10.0f,0.0f,0.0f));
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Player p1 = (Player) objectList.getItem("player1");
			p1.movePlayer(new Vector3f(-10.0f,0.0f,0.0f));
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Player p1 = (Player) objectList.getItem("player1");
			p1.movePlayer(new Vector3f(0.0f,10.0f,0.0f));
			event_caught=true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Player p1 = (Player) objectList.getItem("player1");
			p1.movePlayer(new Vector3f(0.0f,-10.0f,0.0f));
			event_caught=true;
		}
		
		return event_caught;
	}

	public boolean handleMouse(Event evt){
		boolean event_caught=true;
		//update the changes in position
		switch(evt.getMouseButton()){
			case 1:
				if(evt.getType() == Event.Type.MOUSE_BTNDOWN){
				  /*
					BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
					Entity ent = new Entity(1.0f,boxShape,true);
					ent.setPosition(camera.getPosition());
					Parser parser = new XGL_Parser();
					try{
						//parser.readFile("resources/models/misc/legoman.xgl");
						//parser.readFile("resources/models/misc/10010260.xgl");
						parser.readFile("resources/models/misc/singlebox.xgl");
						//parser.readFile("resources/models/misc/cath.xgl");
						//parser.readFile("resources/models/misc/0335-CATHODE_ASSEMBLY.obj");
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("Model loading failed");
					}
					ent.setModel(parser.createModel());
					Vector3f impulse = camera.getRayTo(Mouse.getEventX(), Mouse.getEventY());
					impulse.scale(0.02f);
					ent.setGravity(new Vector3f(0,0,0));
					ent.applyImpulse(impulse, camera.getPosition());
					objectList.enqueuePhysics(ent, QueueItem.ADD);
					*/
				}
				break;
			case 0://Left Button
				physics.drag(
					camera,
					Mouse.getEventButtonState()? 0 : 1,
					camera.getRayTo(Mouse.getEventX(),Mouse.getEventY())
				);
				break;
			case -1:
				
				break;
			default:
				System.out.println("unhandled mouse");
				event_caught=false;
				break;
		}

		if(event_caught==false) {
			switch(evt.getMouseWheelDelta()) {
				case -1: //mouse wheel up
					camera.incrementDistance(IN_OUT_INC);
					break;
				case  1: //mouse wheel down
					camera.incrementDistance(-IN_OUT_INC);
					break;
				default:
					System.out.println("unhandled mouse");
					event_caught=false;
					break;
			}
		}
		physics.motionFunc(camera,Mouse.getEventX(), Mouse.getEventY());
		return event_caught;
	}
}
