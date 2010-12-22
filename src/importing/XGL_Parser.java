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
import org.w3c.dom.NodeList;

public class XGL_Parser extends Parser{
	Model model;
	int i;
	int iiiiii;
	
	XGL_Parser(){}
	
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
		
		NodeList tagList;
		try {
			//Now that we've got the file in DOM format, loop through elements
			Element rootElement = dom.getDocumentElement();
			
			if( rootElement.getNodeName().equals("WORLD")){
				//Get World Defines
				readDefines(rootElement,mats,meshes,points);
				
				//Get any meshes from objects
				
				tagList = rootElement.getElementsByTagName("OBJ");
				for(int i = 0; i < tagList.getLength(); i++){
					ArrayList<Mesh> ms = readObjects((Element) tagList.item(i),mats,meshes,points);
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
		NodeList tagList;
		tagList = root.getElementsByTagName("MAT");
		if( tagList.getLength() != 0 ){
			for(int i = 0; i < tagList.getLength(); i++){
				//Get ID;
				int ID = Integer.parseInt((((Element)tagList.item(i)).getAttribute("ID")));
				
				//Create material
				Material m = readMaterial((Element)tagList.item(i));
				
				//Add to hashmap
				mats.put(ID, m);
			}
		}
		
		tagList = root.getElementsByTagName("MESH");
		if( tagList.getLength() != 0 ){
			for(int i = 0; i < tagList.getLength(); i++){
				//Get ID;
				int ID = Integer.parseInt((((Element)tagList.item(i)).getAttribute("ID")));
				
				//Create Meshes
				ArrayList<Mesh> ms = readMeshes((Element)tagList.item(i), mats, points);
				
				//Shove them in hashmap
				meshes.put(ID, ms);	
			}
		}
		
		tagList = root.getElementsByTagName("P");
		if( tagList.getLength() != 0 ){
			for(int i = 0; i < tagList.getLength(); i++){
				Element ele = (Element)tagList.item(i);
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
		
		//Get all defined Meshes
		NodeList tagList = rootElement.getElementsByTagName("MESH");
		
		//Transform information
		float[] location = null;
		float[] forward = null;
		float[] up = null;
		readTransform(rootElement,location,forward,up);
		
		for( int i = 0; i < tagList.getLength(); i++){
			int ID = Integer.parseInt((((Element)tagList.item(i)).getAttribute("ID")));
			
			ArrayList<Mesh> mtemp = readMeshes(rootElement, mats, points);
			
			//Add to references
			meshes.put(ID, mtemp);
			for(Mesh m: mtemp ){
				//Add to list being returned
				m.transform(location, forward, up);
				createdMeshes.add(m);
			}
		}
		
		//Read any sub objects
		tagList = rootElement.getElementsByTagName("OBJ");
		for(int i = 0; i < tagList.getLength(); i++){
			//Get all Meshes from sub Objects
			ArrayList<Mesh> mtemp = readObjects((Element) tagList.item(i),mats,meshes,points);
			for(Mesh m: mtemp ){
				createdMeshes.add(m);
			}
		}
		
		//Handle Mesh reference tags
		tagList = rootElement.getElementsByTagName("MREF");
		for(int i = 0; i < tagList.getLength(); i++){
			int mref = Integer.parseInt(tagList.item(i).getTextContent());
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
			Mesh m = new Mesh((Face[]) faces.get(key).toArray());
			
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
		NodeList tagList = root.getElementsByTagName("F");
		if(tagList.getLength() != 0){
			for( int i = 0; i < tagList.getLength(); i++){
				Element ele = (Element)tagList.item(i);
				int MatID = (int)readScalarTag(root,"MATREF",true);
				Face f = new Face();
				
				NodeList vertexList = ele.getElementsByTagName("FV*");
				for(int j = 0; j < vertexList.getLength(); j++){
					f.addVertex(readVectorTag((Element)vertexList.item(j),"PREF",true));
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
		m.setAlpha(readVectorTag(root,"ALPHA",false));
		m.setShine(readVectorTag(root,"SHINE",false));
		return m;
	}

	private void readTransform(Element ele, float[] location, float[] forward, float[] up) throws Exception{
		NodeList tagList;
		tagList = ele.getElementsByTagName("TRANSFORM");
		if( tagList.getLength() == 1 ){
			float[] local_location = readVectorTag((Element) tagList.item(0),"POSITION",true);
			float[] local_forward = readVectorTag((Element) tagList.item(0),"FORWARD",true);
			float[] local_up = readVectorTag((Element) tagList.item(0),"UP",true);
			
			for(int i = 0; i < 3; i++){
				location[i] += local_location[i];
				forward[i] += local_forward[i];
				up[i] += local_up[i];
			}
		}else if(tagList.getLength() == 0){
			throwException("Transform sought, but none found.");
		}else if(tagList.getLength() > 1){
			throwException("Multiple Transform in one tag");
		}
	}
	
	private float[] readVectorTag(Element root, String childName, boolean required) throws Exception {
		NodeList tagList;
		tagList = root.getElementsByTagName(childName);
		if( tagList.getLength() == 1 ){
			Element e = (Element)tagList.item(0);
			return readVector(e.getTextContent());
		}else if(tagList.getLength() == 0){
			if(required){
				throwException("Material Tag with no " + childName + " Tag");
			}
		}else if(tagList.getLength() > 1){
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
	
	private float readScalarTag(Element root, String childName, boolean required) throws Exception {
		float[] f = readVectorTag(root, childName, required);
		return f[0];
	}
	
	private void throwException(String message) throws Exception{
		Exception e = new Exception();
		e.initCause(new Throwable(message));
		throw e;
	}

	@Override
	public Model createModel() {
		// TODO
		return null;
	}
}
