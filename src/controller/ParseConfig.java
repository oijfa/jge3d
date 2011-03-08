package controller;

import importing.Parser;
import importing.XGL_Parser;
import importing.pieces.Model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import window.tree.ColoredTextString;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.shapes.BoxShape;

import de.matthiasmann.twl.model.TreeTableNode;
import entity.Entity;
import entity.EntityList;
import entity.QueueItem;

public class ParseConfig {
	EntityList objectList;
	
	public ParseConfig(EntityList objectList) {
		this.objectList = objectList;
	}
	
	public void readConfigFile(String path) throws Exception{
		Document dom;
		//URL url = new URL("resources/models/config.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		//Create Dom Structure
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(this.getClass().getClassLoader().getResourceAsStream(path));
		
		String configName;
		window.tree.Model treeModel = new window.tree.Model();
		HashMap<String, Vector3f> defaultPositions = new HashMap<String, Vector3f>();
		String fullassemblyFocus = null;
		String lineupFocus = null;
		
		ArrayList<Node> tagList;
		
		try {
			//Now that we've got the file in DOM format, loop through elements
			Element rootElement = dom.getDocumentElement();
			
			if( rootElement.getNodeName().equals("config")){
				tagList = findChildrenByName(rootElement, "fullassembly");
				if(tagList.size() == 1){
					fullassemblyFocus = tagList.get(0).getTextContent();
				}else{
					throw new Exception("No default focus");
				}
				
				tagList = findChildrenByName(rootElement, "lineup");
				if(tagList.size() == 1){
					lineupFocus = tagList.get(0).getTextContent();
				}else{
					throw new Exception("No default focus");
				}
				
				tagList = findChildrenByName(rootElement, "name");
				configName = tagList.get(0).getTextContent();
				
				tagList = findChildrenByName(rootElement, "item");
				for(int i = 0; i < tagList.size(); i++){
					//Create nodes for all of them
					createItem((Element)tagList.get(i), treeModel, configName, defaultPositions);
				}
				objectList.parsePhysicsQueue();
				Config.addConfig(configName, new Vector3f(0,0,0), treeModel, defaultPositions, objectList.getItem(configName + "-" + fullassemblyFocus), objectList.getItem(configName + "-" + lineupFocus));
			}else{
				Exception e = new Exception();
				e.initCause(new Throwable("Invalid config file"));
				throw e;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void createItem(Element ele, TreeTableNode parent, String configName, HashMap<String, Vector3f> defaultPositions) throws Exception {
		String name;
		String value;
		ArrayList<Byte> color = null; 
		String path;
		boolean show;
		Vector3f position;
		boolean collidable;
		String[] xyz;
		ArrayList<Node> tagList = findChildrenByName(ele, "name");
		name = tagList.get(0).getTextContent();
		
		tagList = findChildrenByName(ele, "value");
		value = tagList.get(0).getTextContent();
		
		tagList = findChildrenByName(ele, "show");
		if(tagList.size() == 0){
			show = true;
		}else if(tagList.get(0).getTextContent() == "true"){
			show = true;
		}else{
			show = false;
		}
		
		tagList = findChildrenByName(ele, "position");
		if(tagList.size() > 0){
			xyz = tagList.get(0).getTextContent().split(",");
			position = new Vector3f(Float.parseFloat(xyz[0]),Float.parseFloat(xyz[1]),Float.parseFloat(xyz[2]));
		}else{
			position = new Vector3f(0,0,0);
		}
		
		tagList = findChildrenByName(ele, "collidable");
		if(tagList.size() > 0){
			if(tagList.get(0).getTextContent().equals("true"))
				collidable = true;
			else if(tagList.get(0).getTextContent().equals("false"))
				collidable = false;
			else {
				System.out.println("XML parse error: unsupported value" + tagList.get(0).getTextContent());
				collidable=false;
			}
		}else{
			collidable = true;
		}
		
		tagList = findChildrenByName(ele, "path");
		if( tagList.size() > 0){
			path = tagList.get(0).getTextContent();
			BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
			
			Entity ent = new Entity(1.0f, boxShape, collidable);
			ent.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);
			if( !path.equals("") ){
				//Make a cathode	
				Parser p = new XGL_Parser();
				p.readFile(path);
				Model model = new Model();
				model = p.createModel();
				ent.setModel(model);
				objectList.enqueueRenderer(ent, QueueItem.ADD);
				ent.setCollisionShape(model.createCollisionShape());
			}
			
			ent.setPosition(position);
			ent.setProperty("name", configName + "-" + name);
			objectList.enqueuePhysics(ent, QueueItem.ADD);
			
			defaultPositions.put(name, position);
			color = ent.getModel().getColor();
			
			ent.setProperty("resetydist", 4);
			
			//Stop the movement
			ent.setDamping(1.0f,1.0f);
			ent.activate();
			ent.setAngularFactor(0.0f, new Vector3f(0,0,0));
			
			//Move the object back to its original position
			ent.setAngularIdentity();
		}
		
		if(show == true){
			window.tree.Node item;
			if( parent.getClass() == window.tree.Model.class ){
				if(color != null){
					item = ((window.tree.Model)parent).insert(new ColoredTextString(name, color.get(0), color.get(1), color.get(2)), "");
				}else{
					item = ((window.tree.Model)parent).insert(name, "");
				}
			}else{
				if(color != null){
					item = ((window.tree.Node)parent).insert(new ColoredTextString(name, color.get(0), color.get(1), color.get(2)),value);
				}else{
					item = ((window.tree.Node)parent).insert(name,value);
				}
			}
			
			tagList = findChildrenByName(ele, "item");
			for(int i = 0; i < tagList.size(); i++){
				//Create nodes for all of them
				createItem((Element)tagList.get(i),item, configName, defaultPositions);
			}
		}
	}

	private ArrayList<Node> findChildrenByName(Node root, String name){
		String[] names = new String[1];
		names[0] = name;
		return findChildrenByName(root,names);
	}
	
	private ArrayList<Node> findChildrenByName(Node root, String[] names){
		ArrayList<Node> list = new ArrayList<Node>();
		for( int i = 0; i < names.length; i++){
			Node e = root.getFirstChild();
			while(e != null){
				if(e.getNodeName().equals(names[i])){
					list.add(e);
				}
				e = e.getNextSibling();
			}
		}
		return list;
	}
}
