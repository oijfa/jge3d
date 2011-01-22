/*
 * The thing that controls everything....
 */
package controller;

import java.applet.Applet;

import java.io.File;
import java.util.ArrayList;

import importing.Obj_Parser;
import importing.Parser;
import importing.XGL_Parser;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.opengl.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import physics.Physics;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;

import de.matthiasmann.twl.model.TreeTableNode;

import entity.Camera;
import entity.Entity;
import entity.EntityList;
import entity.QueueItem;
import render.Renderer;

public class Controller extends Applet{
	private static final long serialVersionUID = 4458487765324323938L;

	// the game always runs (except when it doesn't)
	private volatile static boolean isRunning = true;
	
	private Renderer renderer;
	private Physics physics;
	
	private long frames = 0;

	private EntityList objectList;
	
	window.tree.Model treeModel;
	
	public static void main(String[] args) throws Exception {
		Applet app = new Controller();
		app.init();
	}
	
	public void init(){
		try {
			startThreads();
			loadLevel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Controller(){	}

	private void startThreads() throws Exception {
		treeModel = new window.tree.Model();
		readConfigFile();
		
		//Instantiate Physics first, as it depends on nothing
		physics = new Physics();
		physics_thread.start();
		
		//Next is the entity list, since it only depends on the physics
		objectList = new EntityList(physics);
		
		//Renderer has to be after entity list
		renderer = new Renderer(objectList);
		render_thread.start();
	}

	// Create the Physics Listening thread
	Thread physics_thread = new Thread() {
		public void run() {
			while (isRunning) {
				if(objectList != null && objectList.queueSize() > 0)
					objectList.parseQueue();
				else
					physics.clientUpdate();
			}
		}
	};

	// Create the vidya thread
	Thread render_thread = new Thread() {
		public void run() {
			renderer.initGL(treeModel);
			this.interrupt();
			while (isRunning) {
				if(Display.isCloseRequested())
					isRunning=false;
				renderer.draw();
			}
		}
	};
	
	public long getFrames() { return frames; }
	public void resetFrames() {	frames = 0;	}
	
	public static void quit() { isRunning = false;	}
	
	public void loadLevel() throws Exception{
		//Make a camera	
		CollisionShape boxShape = new BoxShape(new Vector3f(1, 1, 1));

		Camera cam = new Camera(0.0f, boxShape, false);
		objectList.enqueue(cam, QueueItem.ADD);
		
		//Load some stuff (I would only pick one of the following
		//two methods if I were you)
		loadTestShapes(cam);
		//pullModelFiles("resources/Models");
		
	}
	
	private void loadTestShapes(Camera cam) {
		physics.setGravity(new Vector3f(0,-10,0));
		
		//Legoman thing
		XGL_Parser xgl_parser = new XGL_Parser();
		try{
			xgl_parser.readFile("./lib/legoman.xgl");
			//xgl_parser.readFile("./lib/10010260.xgl");
			//xgl_parser.readFile("./lib/box2.xgl");
			//p.readFile("./lib/cath.xgl");
			//p.readFile("resources/Models/0335-CATHODE_ASSEMBLY.obj");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Model loading failed");
		}
		
		BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
		Entity ent = new Entity(0.0f, boxShape, false);
		ent.setModel(xgl_parser.createModel());
		ent.setPosition(new Vector3f(0.0f,0.0f,0.0f));
		//physics.reduceHull(ent);

		objectList.enqueue(ent, QueueItem.ADD);
		cam.setDistance(20.0f);

		cam.focusOn(ent);
		
		for(int i=10;i<15;i+=10) {
			ent = new Entity(10.0f, boxShape, false);
			ent.setModel(xgl_parser.createModel());
			ent.setPosition(new Vector3f(0.0f,(float)i,0.0f));
			//physics.reduceHull(ent);
			objectList.enqueue(ent, QueueItem.ADD);
		}

		boxShape = new BoxShape(new Vector3f(2, 2, 2));
		ent = new Entity(1.0f, boxShape, false);
		ent.setModel(xgl_parser.createModel());
		ent.setPosition(new Vector3f(-5.0f,0.0f,-10.0f));
		//physics.reduceHull(ent);
		objectList.enqueue(ent, QueueItem.ADD);

	}
	
	@SuppressWarnings("unused")
	private void pullModelFiles(String filename) throws Exception{
		File dir = new File(filename);
		File[] subFiles;
		subFiles = dir.listFiles();
		
		for( File f: subFiles){
			if(!f.isDirectory()){
				//create model
				int dotPos = f.getPath().lastIndexOf(".");
		        String extension = f.getPath().substring(dotPos);
		        if( extension.equals(".xgl") || extension.equals(".obj")){
		        	Parser p;
		        	XGL_Parser xparse = new XGL_Parser();
		    		Obj_Parser oparse = new Obj_Parser();
					if( extension.equals(".xgl")){
						xparse.readFile(f.getPath());
						p = xparse;
					} else {
						oparse.readFile(f.getPath());
						p = oparse;
					}
					if( p != null){
						//Make a cathode	
						BoxShape boxShape = new BoxShape(new Vector3f(1, 1, 1));
						Entity ent = new Entity(0.0f, boxShape, false);	
						ent.setModel(p.createModel());	
						ent.setPosition(new Vector3f(0.0f,0.0f,(float) Math.random()));
						ent.setProperty("name", f.getPath().substring(0,dotPos-1));
						objectList.enqueue(ent, QueueItem.ADD);
						ent.setShouldDraw(false);
					}
		        }
			}else{
				pullModelFiles(f.getPath());
			}
		}
	}
	
	/* Config file reading */
	private void readConfigFile() throws Exception{
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		//Create Dom Structure
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse("./resources/Models/config.xml");
		
		ArrayList<Node> tagList;
		
		try {
			//Now that we've got the file in DOM format, loop through elements
			Element rootElement = dom.getDocumentElement();
			
			if( rootElement.getNodeName().equals("config")){
				tagList = findChildrenByName(rootElement, "folder");
				for(int i = 0; i < tagList.size(); i++){
					//Create nodes for all of them
					createFolderNode((Element)tagList.get(i));
				}
				
				tagList = findChildrenByName(rootElement, "property");
				for(int i = 0; i < tagList.size(); i++){
					createPropertyNode(treeModel, (Element)tagList.get(i));
				}
			}else{
				throwException("Invalid config file");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void createFolderNode(Element ele) {
		String name;
		ArrayList<Node> tagList = findChildrenByName(ele, "name");
		name = tagList.get(0).getTextContent();
		
		window.tree.Node folderNode = treeModel.insert(name, "");
		
		tagList = findChildrenByName(ele, "model");
		for(int i = 0; i < tagList.size(); i++){
			createModelNode(folderNode, (Element)tagList.get(i));
		}
		
		tagList = findChildrenByName(ele, "property");
		for(int i = 0; i < tagList.size(); i++){
			createPropertyNode(folderNode, (Element)tagList.get(i));
		}
	}

	private void createModelNode(window.tree.Node folderNode, Element ele) {
		String name;
		ArrayList<Node> tagList = findChildrenByName(ele, "name");
		name = tagList.get(0).getTextContent();
		window.tree.Node modelNode = folderNode.insert(name, "");
		
		tagList = findChildrenByName(ele, "path");
		//Do something with path
		
		tagList = findChildrenByName(ele, "position");
		//Do something with position
		
		tagList = findChildrenByName(ele, "property");
		for(int i = 0; i < tagList.size(); i++){
			createPropertyNode(modelNode, (Element)tagList.get(i));
		}
	}

	private void createPropertyNode(TreeTableNode parent, Element ele) {
		String name;
		String value;
		
		ArrayList<Node> tagList = findChildrenByName(ele, "name");
		name = tagList.get(0).getTextContent();
		
		tagList = findChildrenByName(ele, "value");
		value = tagList.get(0).getTextContent();
		
		if( parent != treeModel ){
			((window.tree.Node)parent).insert(name, value);
		}else{
			((window.tree.Model)parent).insert(name, value);
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

	private void throwException(String message) throws Exception{
		Exception e = new Exception();
		e.initCause(new Throwable(message));
		throw e;
	}
}
