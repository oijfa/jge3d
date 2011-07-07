package engine.input.components;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.input.Keyboard;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import engine.entity.Camera;
import engine.entity.EntityList;

public class KeyMap {
  HashMap<String,String> key_map;
  
  final HashMap<String, Integer> lwjgl_key_enums;
  final HashMap<Integer, String> enums_to_function; 
  
  EntityList entity_list;
  
  public KeyMap(String filename, EntityList ent_list) throws KeyMapException{
    key_map = new HashMap<String,String>();
    
    lwjgl_key_enums = new HashMap<String,Integer>();
    enums_to_function = new HashMap<Integer,String>();
    
    entity_list = ent_list;
    
    for(Field f : Keyboard.class.getFields()){
    	if(f.getName().contains("KEY_")){
    		try {
				lwjgl_key_enums.put(f.getName(), (Integer)f.get(null));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {}
    	}
    }
    
    try {
      parseXml(readFileAsString(filename));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void parseXml(String xml_string) throws KeyMapException{
    Document dom;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    
    //Create Dom Structure
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
      
      try {
        dom = db.parse(xml_string);
        
        Element root_element = dom.getDocumentElement();
        
        if(root_element.getNodeName().equalsIgnoreCase("keymap")){
          
        	ArrayList<Node> key_settings = findChildrenByName(root_element, "key");
        	
        	for(Node n : key_settings){
        		String id = ((Element) n).getAttribute("ID");
        		
        		try {
					if(KeyMap.class.getMethod(n.getTextContent()) != null){
						enums_to_function.put(
							lwjgl_key_enums.get(id),
							n.getTextContent()
						);
					}
				//TODO:  Throw a warning or something
				} catch (SecurityException e) {
				} catch (DOMException e) {
				} catch (NoSuchMethodException e) {
				}
        	}
        }else{
          throwException("KeyMap tag should be root element.");
        }
      } catch (SAXException e) {
        throwException("Sax Exception");
      } catch (IOException e) {
        throwException("IO Exception");
      }
    }catch (ParserConfigurationException e) {
      throwException("ParserConfigurationException");
    }
  }
  
  private static String readFileAsString(String filePath) throws java.io.IOException{
      byte[] buffer = new byte[(int) new File(filePath).length()];
      BufferedInputStream f = null;
      try {
          f = new BufferedInputStream(new FileInputStream(filePath));
          f.read(buffer);
      } finally {
          if (f != null) try { f.close(); } catch (IOException ignored) { }
      }
      return new String(buffer);
  }
  
  private void throwException(String message) throws KeyMapException{
    KeyMapException e = new KeyMapException();
    e.initCause(new Throwable(message));
    throw e;
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
  	
  private void moveCameraLeft(){
	  ((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementRotation(-1.0d);
  }
  private void moveCameraRight(){
	  ((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementRotation(1.0d);
  }
  private void moveCameraUp(){
	  ((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(1.0d);
  }
  private void moveCameraDown(){
	  ((Camera)entity_list.getItem(Camera.CAMERA_NAME)).incrementDeclination(-1.0d);
  }
}
