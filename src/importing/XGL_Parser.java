/*
 * 	Parser for XGL files.  Turns out to be a fairly shitty format, but Autocad
 * 		exports it, and its fairly simple, so it was included.
 */

package importing;

import importing.pieces.Face;
import importing.pieces.Material;
import importing.pieces.Mesh;
import importing.pieces.Model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XGL_Parser extends Parser{
	Model model;
	
	int backup = 999999;
	
	public XGL_Parser(){}
	
	@Override
	public void readFile(String fileName) throws Exception {
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		ArrayList<Mesh> drawableMeshes = new ArrayList<Mesh>();
		
		HashMap<Integer, Material> mats = new HashMap<Integer, Material>();
		HashMap<Integer, ArrayList<Mesh>> meshes = new HashMap<Integer, ArrayList<Mesh>>();
		HashMap<Integer, float[]> points = new HashMap<Integer, float[]>();
		
		//Create Dom Structure
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse("./lib/legoman.xgl");
		
		ArrayList<Node> tagList;
		try {
			//Now that we've got the file in DOM format, loop through elements
			Element rootElement = dom.getDocumentElement();
			
			if( rootElement.getNodeName().equals("WORLD")){
				//Get World Defines
				readDefines(rootElement,mats,meshes,points);
				
				//Get any meshes from objects
				
				tagList = findChildrenByName(rootElement, "OBJECT");
				for(int i = 0; i < tagList.size(); i++){
					ArrayList<Mesh> ms = readObjects((Element) tagList.get(i),mats,meshes,points);
					for(Mesh m: ms){
						drawableMeshes.add(m);
					}
				}
			}else{
				throwException("World tag should be root element.");
			}
			
			//Everything read in, create the model
			model = new Model(drawableMeshes);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Add to the hashmaps containing references
	private void readDefines(
			Element root, 
			HashMap<Integer,Material> mats, 
			HashMap<Integer, ArrayList<Mesh>> meshes,
			HashMap<Integer,float[]> points
	) throws Exception{
		ArrayList<Node> tagList;
		tagList = findChildrenByName(root, "MAT");
		if( tagList.size() != 0 ){
			for(int i = 0; i < tagList.size(); i++){
				//Get ID;
				int ID = Integer.parseInt((((Element)tagList.get(i)).getAttribute("ID")));
				
				//Create material
				Material m = readMaterial((Element)tagList.get(i));
				
				//Add to hashmap
				mats.put(ID, m);
			}
		}
		
		root.getNodeName();
		tagList = findChildrenByName(root, "MESH");
		if( tagList.size() != 0 ){
			for(int i = 0; i < tagList.size(); i++){
				//Only load meshes that could be referenced later

				//Create Meshes
				ArrayList<Mesh> ms = readMeshes((Element)tagList.get(i), mats, points);
				int ID;
				if(tagList.get(i).hasAttributes()){
					//Get ID;
					ID = Integer.parseInt(((Element)tagList.get(i)).getAttribute("ID"));
				}else{
					ID = backup;
					backup++;
				}
				
				//Shove them in hashmap
				meshes.put(ID, ms);
			}
		}
		
		tagList = findChildrenByName(root,"P");
		if( tagList.size() != 0 ){
			for(int i = 0; i < tagList.size(); i++){
				Element ele = (Element)tagList.get(i);
				//Get ID;
				int ID = Integer.parseInt(ele.getAttribute("ID"));
				
				//Get point values
				float[] pos = readVector(ele.getTextContent());
				
				//Add to hashmap
				points.put(ID, pos);
			}
		}
	}
	
	private ArrayList<Mesh> readObjects(Element rootElement,
			HashMap<Integer, Material> _mats, 
			HashMap<Integer, ArrayList<Mesh>> _meshes,
			HashMap<Integer, float[]> _points
	) throws Exception {
		//Create clones so we don't overwrite the parent's references
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats.clone();
		@SuppressWarnings("unchecked")
		HashMap<Integer, float[]> points = (HashMap<Integer, float[]>) _points.clone();
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, ArrayList<Mesh>> meshes = (HashMap<Integer, ArrayList<Mesh>>) _meshes.clone();
		
		//Add defines created in this tag
		readDefines(rootElement,mats,meshes,points);
		
		//Holds all the meshes we create
			//Since these are in objects, these are what will actually be added to the model in the end.
			//Definitions outside of OBJ tags with no references from them will not be drawn
		ArrayList<Mesh> createdMeshes = new ArrayList<Mesh>();
		
		ArrayList<Node> tagList;
		
		//Transform information
		float[] location = {0.0f,0.0f,0.0f};
		float[] forward = {0.0f,0.0f,1.0f};
		float[] up = {0.0f,1.0f,0.0f};
		readTransform(rootElement,location,forward,up);
		
		//Get all defined Meshes
		tagList = findChildrenByName(rootElement,"MESH");
		for( int i = 0; i < tagList.size(); i++){
			int ID;
			if(tagList.get(i).hasAttributes()){
				ID = Integer.parseInt((((Element)tagList.get(i)).getAttribute("ID")));
			}else{
				ID = backup;
				backup++;
			}
			
			ArrayList<Mesh> mtemp = readMeshes((Element)tagList.get(i), mats, points);
			
			//Add to references
			meshes.put(ID, mtemp);
			for(Mesh m: mtemp ){
				//Add to list being returned
				m.transform(location, forward, up);
				createdMeshes.add(m);
			}
		}
		
		//Read any sub objects
		tagList = findChildrenByName(rootElement,"OBJ");
		for(int i = 0; i < tagList.size(); i++){
			//Get all Meshes from sub Objects
			ArrayList<Mesh> mtemp = readObjects((Element) tagList.get(i),mats,meshes,points);
			for(Mesh m: mtemp ){
				createdMeshes.add(m);
			}
		}
		
		//Handle Mesh reference tags
		tagList = findChildrenByName(rootElement,"MREF");
		for(int i = 0; i < tagList.size(); i++){
			int mref = Integer.parseInt(tagList.get(i).getTextContent());
			ArrayList<Mesh> refrenced_meshes = meshes.get(mref);
			for(Mesh m : refrenced_meshes){
				//Transform the Mesh
				Mesh m_clone = new Mesh(m); 
				m_clone.transform(location,forward,up);
				createdMeshes.add(m_clone);
			}
		}
		return createdMeshes;
	}
	
	//Create meshes from a mesh tag.  Multiples because it groups based on assigned material.
	private ArrayList<Mesh> readMeshes(
			Element root, 
			HashMap<Integer, Material> _mats, 
			HashMap<Integer, float[]> _points
	) throws Exception {
		//Create clones so we don't overwrite the parent's references
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats.clone();
		@SuppressWarnings("unchecked")
		HashMap<Integer, float[]> points = (HashMap<Integer, float[]>) _points.clone();
		
		readDefines(root, mats, null, points);
		
		//Meshes created this call
		ArrayList<Mesh> created_meshes = new ArrayList<Mesh>();
		
		//Faces created this call
		HashMap<Integer, ArrayList<Face>> faces = readFaces(root,mats,points);
		
		
		for(Integer key: faces.keySet()){
			//For every Material, grab that set of faces and shove it into a Mesh
			Mesh m = new Mesh(faces.get(key));
			
			//Add to list of meshes created this call
			created_meshes.add(m);
			
			//Set the material
			m.setMaterial(mats.get(key));
		}
		
		return created_meshes;
	}
	
	private HashMap<Integer, ArrayList<Face>> readFaces(Element root, HashMap<Integer, Material> _mats, HashMap<Integer, float[]> _points) throws Exception {
		//Faces will be grouped by what material they are tied too
		HashMap<Integer, ArrayList<Face>> faces = new HashMap<Integer, ArrayList<Face>>();
		ArrayList<Node> tagList = findChildrenByName(root,"F");
		if(tagList.size() != 0){
			for( int i = 0; i < tagList.size(); i++){
				Element ele = (Element)tagList.get(i);
				int MatID = (int)readScalarTag(ele,"MATREF",true);
				Face f = new Face();
				
				String[] temp = {"FV1", "FV2", "FV3"};
				ArrayList<Node> vertexList = findChildrenByName(ele,temp);
				for(int j = 0; j < vertexList.size(); j++){
					float[] temp1 = _points.get((int)readScalarTag((Element)vertexList.get(j),"PREF",true));
					f.addVertex(temp1);
				}
				if(faces.get(MatID) == null){
					faces.put(MatID, new ArrayList<Face>());
				}
				faces.get(MatID).add(f);
			}
		}else{
			throwException("Faces sought, but none found.");
		}
		return faces;
	}
	
	//Creates a new material from a MAT tag
	private Material readMaterial(Element root) throws Exception {
		Material m = new Material();
		m.setAmbient(readVectorTag(root,"AMB",true));
		m.setDiffuse(readVectorTag(root,"DIFF",true));
		m.setSpecular(readVectorTag(root,"SPEC",false));
		m.setEmission(readVectorTag(root,"EMISS",false));
		m.setAlpha(readScalarTag(root,"ALPHA",false));
		m.setShine(readScalarTag(root,"SHINE",false));
		return m;
	}

	private void readTransform(Element ele, float[] location, float[] forward, float[] up) throws Exception{
		ArrayList<Node> tagList;
		tagList = findChildrenByName(ele,"TRANSFORM");
		if( tagList.size() == 1 ){
			float[] local_location = readVectorTag((Element) tagList.get(0),"POSITION",true);
			float[] local_forward = readVectorTag((Element) tagList.get(0),"FORWARD",true);
			float[] local_up = readVectorTag((Element) tagList.get(0),"UP",true);
			
			for(int i = 0; i < 3; i++){
				location[i] += local_location[i];
				forward[i] += local_forward[i];
				up[i] += local_up[i];
			}
		}else if(tagList.size() == 0){
			//Do nothing, its fine.
		}else if(tagList.size() > 1){
			throwException("Multiple Transform in one tag");
		}
	}
	
	private float[] readVectorTag(Element root, String childName, boolean required) throws Exception {
		ArrayList<Node> tagList;
		tagList = findChildrenByName(root,childName);
		if( tagList.size() == 1 ){
			Element e = (Element)tagList.get(0);
			return readVector(e.getTextContent());
		}else if(tagList.size() == 0){
			if(required){
				throwException("Material Tag with no " + childName + " Tag");
			}
		}else if(tagList.size() > 1){
			throwException("Material Tag with redundant " + childName + " Tag");
		}
		
		return new float[3];
	}
	
	private float[] readVector(String data){
		String[] temp = data.split(",");
		float[] ret = new float[3];
		
		for(int i = 0; i < 3; i++){
			ret[i] = Float.parseFloat(temp[i]);
		}
		
		return ret;
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
	
	private float readScalarTag(Element root, String childName, boolean required) throws Exception {
		ArrayList<Node> tagList;
		tagList = findChildrenByName(root,childName);
		if( tagList.size() == 1 ){
			Element e = (Element)tagList.get(0);
			return Float.parseFloat(e.getTextContent());
		}else if(tagList.size() == 0){
			if(required){
				throwException(root.getNodeName() + " Tag with no " + childName + " Tag");
			}
		}else if(tagList.size() > 1){
			throwException(root.getNodeName() + " Tag with redundant " + childName + " Tag");
		}
		
		return -500;//? TODO
	}
	
	private void throwException(String message) throws Exception{
		Exception e = new Exception();
		e.initCause(new Throwable(message));
		throw e;
	}

	@Override
	public Model createModel() {
		return new Model(model);
	}
}
