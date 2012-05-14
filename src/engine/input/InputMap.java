package engine.input;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.input.Keyboard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import engine.entity.Camera;
import engine.entity.EntityList;
import engine.entity.Actor;
import engine.input.components.KeyMapException;
import engine.resource.Resource;
import engine.resource.ResourceManager;
import de.matthiasmann.twl.Event;

public class InputMap implements Resource {
	HashMap<String,String> key_map;

	final HashMap<String, Integer> lwjgl_keyboard_enums;
	final HashMap<String, String> enums_to_function;

	EntityList entity_list;

	public InputMap() throws KeyMapException{
		key_map = new HashMap<String,String>();

		lwjgl_keyboard_enums = new HashMap<String,Integer>();
		enums_to_function = new HashMap<String,String>();

		for(Field f : Keyboard.class.getFields()){
			if(f.getName().contains("KEY_")){
				try {
					lwjgl_keyboard_enums.put(f.getName(), (Integer)f.get(null));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {}
			}
		}
	}

	void parseKeyboardSettings(ArrayList<Node> key_settings) throws KeyMapException{
		for(Node n : key_settings){
			String id = ((Element) n).getAttribute("ID");
			String event = ((Element) n).getAttribute("EVENT");
			
			try {
				if(this.getClass().getMethod(n.getTextContent()) != null){
					enums_to_function.put(
						String.valueOf(lwjgl_keyboard_enums.get("KEY_" + id.toUpperCase())) + "KEY_" + event.toUpperCase(),
						n.getTextContent()
					);
				}
			} catch (Exception e) {
				throwKeyMapException(e);
			}
		}
	}

	void parseMouseSettings(ArrayList<Node> mouse_settings) throws KeyMapException{
		for(Node n : mouse_settings){
			String event = ((Element) n).getAttribute("EVENT");
			if(n.getTextContent() != ""){
				try {
					if(this.getClass().getMethod(n.getTextContent()) != null){
						enums_to_function.put(
							"0MOUSE_" + event,  
							n.getTextContent()
						);
					}
				} catch (Exception e) {
					throwKeyMapException(e);
				}
			}
		}
	}
  
  	private ArrayList<Node> findChildrenByName(Node root, String... names) {
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < names.length; i++) {
			Node e = root.getFirstChild();
			while (e != null) {
				if (e.getNodeName().equals(names[i])) {
					list.add(e);
				}
				e = e.getNextSibling();
			}
		}
		return list;
	}
  	
  	
	public void rotateCamLeft(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).addMovement(new Vector3f(-10,0,0));
		stopMovement();
	}
	public void rotateCamRight(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).addMovement(new Vector3f(10,0,0));
		stopMovement();
	}
	public void rotateCamUp(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).addMovement(new Vector3f(0,10,0));
		stopMovement();
	}
	public void rotateCamDown(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).addMovement(new Vector3f(0,-10,0));
		stopMovement();
	}
	public void stopCamLeft(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).subMovement(new Vector3f(-10,0,0));
		stopMovement();
	}
	public void stopCamRight(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).subMovement(new Vector3f(10,0,0));
		stopMovement();
	}
	public void stopCamUp(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).subMovement(new Vector3f(0,10,0));
		stopMovement();
	}
	public void stopCamDown(){
		((Camera)entity_list.getItem(Camera.CAMERA_NAME)).subMovement(new Vector3f(0,-10,0));
		stopMovement();
	}
	
	
	/*public void moveCameraForward(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementPosition();}
	public void moveCameraBack(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraLeft(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraRight(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraUp(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraDown(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}*/
	
	public void zoomCameraIn(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDistance(-0.25d);}
	public void zoomCameraOut(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDistance(0.25d);}
	
	public void playerForward(){
		Vector3f move_dir = new Vector3f();
		move_dir.sub(
			entity_list.getItem("player").getPosition(),
			entity_list.getItem(Camera.CAMERA_NAME).getPosition()	
			
		);		
		move_dir.normalize();
		move_dir.scale(0.15f);
		((Actor)entity_list.getItem("player")).moveActor(move_dir);
		//((Actor)entity_list.getItem("player")).moveActor(new Vector3f(0,0,-0.1f));
	}
	public void playerBack(){
		Vector3f move_dir = new Vector3f();
		move_dir.sub(
			entity_list.getItem("player").getPosition(),
			entity_list.getItem(Camera.CAMERA_NAME).getPosition()	
			
		);		
		move_dir.normalize();
		move_dir.scale(-0.15f);
		((Actor)entity_list.getItem("player")).moveActor(move_dir);
		//((Actor)entity_list.getItem("player")).moveActor(new Vector3f(0,0,0.1f));
	}
	public void playerLeft(){
		Vector3f move_dir = new Vector3f();
		Vector3f cross_dir = new Vector3f();
		
		cross_dir.sub(
			entity_list.getItem("player").getPosition(),
			entity_list.getItem(Camera.CAMERA_NAME).getPosition()
		);
		move_dir.cross(
			cross_dir,
			((Camera)entity_list.getItem(Camera.CAMERA_NAME)).getUp()
		);		
				
		move_dir.normalize();
		move_dir.scale(-0.15f);
		((Actor)entity_list.getItem("player")).moveActor(move_dir);
		//((Actor)entity_list.getItem("player")).moveActor(new Vector3f(-0.1f,0,0));
	}
	public void playerRight(){
		Vector3f move_dir = new Vector3f();
		Vector3f cross_dir = new Vector3f();
		
		cross_dir.sub(
			entity_list.getItem("player").getPosition(),
			entity_list.getItem(Camera.CAMERA_NAME).getPosition()
		);
		move_dir.cross(
			cross_dir,
			((Camera)entity_list.getItem(Camera.CAMERA_NAME)).getUp()
		);		
				
		move_dir.normalize();
		move_dir.scale(0.15f);
		((Actor)entity_list.getItem("player")).moveActor(move_dir);
		//((Actor)entity_list.getItem("player")).moveActor(new Vector3f(0.1f,0,0));
	}

	//STOP MOVEMENT CODE
	public void playerStopForward(){
		if( !((Actor)entity_list.getItem("player")).getWalkDirection().equals(new Vector3f(0,0,0)) ) {
			Vector3f move_dir = new Vector3f();
			move_dir.sub(
				entity_list.getItem("player").getPosition(),
				entity_list.getItem(Camera.CAMERA_NAME).getPosition()	
				
			);		
			move_dir.normalize();
			move_dir.scale(-0.15f);
			((Actor)entity_list.getItem("player")).moveActor(move_dir);
		}
	}
	
	public void playerStopBack(){
		if( !((Actor)entity_list.getItem("player")).getWalkDirection().equals(new Vector3f(0,0,0)) ) {
			Vector3f move_dir = new Vector3f();
			move_dir.sub(
				entity_list.getItem("player").getPosition(),
				entity_list.getItem(Camera.CAMERA_NAME).getPosition()	
				
			);		
			move_dir.normalize();
			move_dir.scale(0.15f);
			((Actor)entity_list.getItem("player")).moveActor(move_dir);
		}
	}
	public void playerStopLeft(){
		if( !((Actor)entity_list.getItem("player")).getWalkDirection().equals(new Vector3f(0,0,0)) ) {
			Vector3f move_dir = new Vector3f();
			Vector3f cross_dir = new Vector3f();
			
			cross_dir.sub(
				entity_list.getItem("player").getPosition(),
				entity_list.getItem(Camera.CAMERA_NAME).getPosition()
			);
			move_dir.cross(
				cross_dir,
				((Camera)entity_list.getItem(Camera.CAMERA_NAME)).getUp()
			);		
					
			move_dir.normalize();
			move_dir.scale(0.15f);
			((Actor)entity_list.getItem("player")).moveActor(move_dir);
		}
	}
	public void playerStopRight(){
		if( !((Actor)entity_list.getItem("player")).getWalkDirection().equals(new Vector3f(0,0,0)) ) {
			Vector3f move_dir = new Vector3f();
			Vector3f cross_dir = new Vector3f();
			
			cross_dir.sub(
				entity_list.getItem("player").getPosition(),
				entity_list.getItem(Camera.CAMERA_NAME).getPosition()
			);
			move_dir.cross(
				cross_dir,
				((Camera)entity_list.getItem(Camera.CAMERA_NAME)).getUp()
			);		
					
			move_dir.normalize();
			move_dir.scale(-0.15f);
			((Actor)entity_list.getItem("player")).moveActor(move_dir);
		}
	}
	
	public void stopMovement() {
		if( ((Actor)entity_list.getItem("player")) != null)
			((Actor)entity_list.getItem("player")).stopActor();
		//((Actor)entity_list.getItem("player")).moveActor(new Vector3f(0,0,-0.1f));
	}
	
	public void playerJump(){((Actor)entity_list.getItem("player")).jump();}	
  
  	public boolean handleEvent(Event e) throws KeyMapException{
		String[] function_names = {
		    enums_to_function.get(String.valueOf(e.getKeyCode())), 
		    enums_to_function.get(String.valueOf(e.getKeyCode()) + e.getType())
  		};
		//System.out.println(String.valueOf(e.getKeyCode()) + String.valueOf(e.getType()));
		for(String function_name : function_names){
			/*
			System.out.println(
				function_name + " == " +
				String.valueOf(e.getKeyCode()) + " == " +
				e.getType()
			);
			*/
			if( function_name != null){
				try {
					InputMap.class.getMethod(function_name).invoke(this,(Object[])null);
				} catch (Exception ex) {
					throwKeyMapException(ex);
				}
			}
		}
		return true;
	}
  	
  	public void debug() {
  		System.out.println(lwjgl_keyboard_enums.toString());
  		System.out.println("###");
  		System.out.println(key_map.toString());
  	}
  	
  	private void throwException(String message) throws Exception {
	    KeyMapException e = new KeyMapException();
	    e.initCause(new Throwable(message));
	    throw e;
  	}
  	
  	private void throwKeyMapException(Exception ex) throws KeyMapException{ throw new KeyMapException(ex); }

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromFile(ResourceManager resource_manager, InputStream is, String extension) throws Exception {
		Document dom;
  		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    
  		//Create Dom Structure
  		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		dom = db.parse(is);
	
		Element root_element = dom.getDocumentElement();
		if(root_element.getNodeName().equalsIgnoreCase("keymap")){
			ArrayList<Node> keyboards = findChildrenByName(root_element, "keyboard");
			for(Node keyboard : keyboards){
				ArrayList<Node> key_settings = findChildrenByName(keyboard, "key");
				parseKeyboardSettings(key_settings);
			}

			ArrayList<Node> mice = findChildrenByName(root_element,"mouse");
			for(Node mouse : mice){
				ArrayList<Node> mouse_settings = findChildrenByName(mouse, "event");
				parseMouseSettings(mouse_settings);
			}
		}else{
			throwException("KeyMap tag should be root element.");
		}
	}

	public void setEntityList(EntityList entity_list2) {
		this.entity_list = entity_list2;
	}
}
