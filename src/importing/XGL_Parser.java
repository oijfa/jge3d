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
	XGL_Parser(){}
	
	@Override
	public void readFile(String fileName) throws Exception {
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		HashMap<Integer, Material> mats = new HashMap<Integer, Material>();
		HashMap<Integer, Mesh> meshes = new HashMap<Integer, Mesh>();
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
					Mesh[] ms = readObjects((Element) tagList.item(i),mats,meshes,points);
					for(Mesh m: ms){
						meshes.put(meshes.size()+ 1, m);
					}
				}
			}else{
				throwException("World tag should be root element.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Add to the hashmaps containing references
	private void readDefines(
			Element root, 
			HashMap<Integer,Material> mats, 
			HashMap<Integer,Mesh> meshes,
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
				Mesh[] ms = readMeshes((Element)tagList.item(i), mats, points);
				
				//Shove them in hashmap
				for(Mesh m: ms){
					meshes.put(ID, m);	
				}
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
	
	private Mesh[] readObjects(Element rootElement,
			HashMap<Integer, Material> _mats, 
			HashMap<Integer, Mesh> _meshes,
			HashMap<Integer, float[]> _points
	) throws Exception {
		//Create clones so we don't overwrite the parent's references
		
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats.clone();
		@SuppressWarnings("unchecked")
		HashMap<Integer, float[]> points = (HashMap<Integer, float[]>) _points.clone();
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Mesh> meshes = (HashMap<Integer, Mesh>) _meshes.clone();
		
		//Add defines created in this tag
		readDefines(rootElement,mats,meshes,points);
		
		//Holds all the meshes we create
		ArrayList<Mesh> createdMeshes = new ArrayList<Mesh>();
		
		//Read any sub objects
		 NodeList tagList = rootElement.getElementsByTagName("OBJ");
		for(int i = 0; i < tagList.getLength(); i++){
			//Get all defined Meshes
			Mesh[] mtemp = readMeshes(rootElement, mats, points);
			for(Mesh m: mtemp ){
				createdMeshes.add(m);
			}
			//Get all Meshes from sub Objects
			mtemp = readObjects((Element) tagList.item(i),mats,meshes,points);
			for(Mesh m: mtemp ){
				createdMeshes.add(m);
			}
		}
		return (Mesh[]) createdMeshes.toArray();
	}
	
	//Create meshes from a mesh tag.  Multiples because it groups based on assigned material.
	private Mesh[] readMeshes(Element root, HashMap<Integer, Material> _mats, HashMap<Integer, float[]> _points) throws Exception {
		@SuppressWarnings("unchecked") //Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats.clone();
		@SuppressWarnings("unchecked")
		HashMap<Integer, float[]> points = (HashMap<Integer, float[]>) _points.clone();
		
		readDefines(root, mats, null, points);		
		ArrayList<Mesh> created_meshes = new ArrayList<Mesh>();
		
		HashMap<Integer, ArrayList<Face>> faces = readFaces(root,mats,points);
		
		
		for(Integer key: faces.keySet()){
			Mesh m = new Mesh((Face[]) faces.get(key).toArray());
			created_meshes.add(m);
			m.setMaterial(mats.get(key));
		}
		
		return (Mesh[])created_meshes.toArray();
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
