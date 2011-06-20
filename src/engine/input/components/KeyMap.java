package engine.input.components;

import java.io.*;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class KeyMap {
  HashMap<String,String> key_map;
  
  public KeyMap(String filename) throws KeyMapException{
    key_map = new HashMap<String,String>();
    
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
          
          
          
        }else{
          throwException("KeyMap tag should be root element.");
        }
      } catch (SAXException e) {
        throwException("Sax Exception");
      } catch (IOException e) {
        throwException("IO Exception");
      }
    } catch (ParserConfigurationException e) {
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
}
