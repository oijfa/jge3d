package engine.input;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import engine.entity.Camera;
import engine.entity.EntityList;
import engine.entity.Player;
import engine.input.components.KeyMapException;
import de.matthiasmann.twl.Event;

public class InputMap {
  HashMap<String,String> key_map;
  
  final HashMap<String, Integer> lwjgl_key_enums;
  final HashMap<String, String> enums_to_function; 
  
  EntityList entity_list;
  
  public InputMap(String filename, EntityList ent_list) throws KeyMapException{
    key_map = new HashMap<String,String>();
    
    lwjgl_key_enums = new HashMap<String,Integer>();
    enums_to_function = new HashMap<String,String>();
    
    entity_list = ent_list;

    for(Field f : Keyboard.class.getFields()){
    	if(f.getName().contains("KEY_")){
    		try {
    			lwjgl_key_enums.put(f.getName(), (Integer)f.get(null));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {}
    	}
    }

    parseXml(filename);
    /*
    System.out.println("Outputting read key map: ");
    for( Entry e : enums_to_function.entrySet()){
    	System.out.println("	;" + e.getKey() + "; == ;" + e.getValue() + ";");
    }*/
  }
  
  	private void parseXml(String filePath) throws KeyMapException{
  		Document dom;
  		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    
  		//Create Dom Structure
  		DocumentBuilder db;
  		try {
  			db = dbf.newDocumentBuilder();
  			
  			try {
  				dom = db.parse(InputMap.class.getResourceAsStream("keymaps/" + filePath));
  				Element root_element = dom.getDocumentElement();
  				if(root_element.getNodeName().equalsIgnoreCase("keymap")){
  					ArrayList<Node> key_settings = findChildrenByName(root_element, "key");
  					for(Node n : key_settings){
  						String id = ((Element) n).getAttribute("ID");
  						String event = ((Element) n).getAttribute("EVENT");
  						
  						try {
  							if(this.getClass().getMethod(n.getTextContent()) != null){
  								enums_to_function.put(
  									String.valueOf(lwjgl_key_enums.get(id.toUpperCase())) + event.toUpperCase(),
  									n.getTextContent()
  								);
  							}
						} catch (Exception e) {
							throwKeyMapException(e);
						}
  					}
  				}else{
  					throwException("KeyMap tag should be root element.");
  				}
  			} catch (SAXException e) {
				e.printStackTrace();
				throwException("Sax Exception");
		  	} catch (IOException e) {
		  		e.printStackTrace();
		  		throwException("IO Exception");
		  	}
		}catch (ParserConfigurationException e) {
			throwException("ParserConfigurationException");
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
  	
	public void rotateCamLeft(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementRotation(-0.05d);}
	public void rotateCamRight(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementRotation(0.05d);}
	public void rotateCamUp(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(0.05d);}
	public void rotateCamDown(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	
	/*public void moveCameraForward(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementPosition();}
	public void moveCameraBack(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraLeft(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraRight(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraUp(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}
	public void moveCameraDown(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-0.05d);}*/
	
	public void zoomCameraIn(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDistance(-0.25d);}
	public void zoomCameraOut(){((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDistance(0.25d);}
	
	public void playerForward(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0,0,-0.1f));}
	public void playerBack(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0,0,0.1f));}
	public void playerLeft(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(-0.1f,0,0));}
	public void playerRight(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0.1f,0,0));}

	public void playerStopForward(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0,0,0.1f));}
	public void playerStopBack(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0,0,-0.1f));}
	public void playerStopLeft(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(0.1f,0,0));}
	public void playerStopRight(){((Player)entity_list.getItem("player")).movePlayer(new Vector3f(-0.1f,0,0));}
	
	public void playerJump(){((Player)entity_list.getItem("player")).jump();}	
  
  	public boolean handleEvent(Event e) throws KeyMapException{
		String[] function_names = {
		    enums_to_function.get(String.valueOf(e.getKeyCode())), 
		    enums_to_function.get(String.valueOf(e.getKeyCode()) + e.getType())
  		};
		Keyboard.getEventKey();
		for(String function_name : function_names){
			
			System.out.println(
				function_name + " == " +
				String.valueOf(e.getKeyCode()) + " == " +
				e.getType()
			);
			
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
  		System.out.println(lwjgl_key_enums.toString());
  		System.out.println("###");
  		System.out.println(key_map.toString());
  	}
  	
  	private void throwException(String message) throws KeyMapException{
	    KeyMapException e = new KeyMapException();
	    e.initCause(new Throwable(message));
	    throw e;
  	}
  	
  	private void throwKeyMapException(Exception ex) throws KeyMapException{ throw new KeyMapException(ex); }
}
