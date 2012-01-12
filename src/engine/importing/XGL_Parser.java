/*
 * 	Parser for XGL files.  Turns out to be a fairly shitty format, but Autocad
 * 		exports it, and its fairly simple, so it was included.
 */

package engine.importing;

import engine.render.Model;
import engine.render.model_pieces.Face;
import engine.render.model_pieces.Mesh;
import engine.render.ubos.Material;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XGL_Parser extends Parser {
	Model model;

	int backup = 999999;

	public XGL_Parser() {
	}

	@Override
	public void readUrl(String url) throws Exception {
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Create Dom Structure
		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(
			this.getClass().getClassLoader()
			.getResourceAsStream(url)
		);
		parseXGL(dom);
		model.verify();
	}

	@Override
	public void readFile(String fileName) throws Exception {
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Create Dom Structure
		DocumentBuilder db = dbf.newDocumentBuilder();

		dom = db.parse(this.getClass().getClassLoader().getResourceAsStream(fileName));
		parseXGL(dom);
		model.verify();
	}

	private void parseXGL(Document dom) {
		ArrayList<Mesh> drawableMeshes = new ArrayList<Mesh>();
		HashMap<Integer, Material> mats = new HashMap<Integer, Material>();
		HashMap<Integer, ArrayList<Mesh>> meshes = new HashMap<Integer, ArrayList<Mesh>>();
		HashMap<Integer, Vector3f> points = new HashMap<Integer, Vector3f>();
		HashMap<Integer, Vector3f> normals = new HashMap<Integer, Vector3f>();
		ArrayList<Node> tagList;
		try {
			// Now that we've got the file in DOM format, loop through elements
			Element rootElement = dom.getDocumentElement();

			if (rootElement.getNodeName().equals("WORLD")) {
				// Get World Defines
				readDefines(rootElement, mats, meshes, points, normals);

				// Get any meshes from objects

				tagList = findChildrenByName(rootElement, "OBJECT");
				for (int i = 0; i < tagList.size(); i++) {
					ArrayList<Mesh> ms = readObjects((Element) tagList.get(i),
						mats, meshes, points, normals);
					for (Mesh m : ms) {
						drawableMeshes.add(m);
					}
				}
				if (tagList.size() == 0) {
					for (Integer i : meshes.keySet()) {
						for (Mesh m : meshes.get(i)) {
							drawableMeshes.add(m);
						}
					}
				}
			} else {
				throwException("World tag should be root element.");
			}

			// Everything read in, create the model
			model = new Model(drawableMeshes);

			// : Remove this debug
			// System.out.println(model.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Add to the hashmaps containing references
	private void readDefines(Element root, HashMap<Integer, Material> mats,
		HashMap<Integer, ArrayList<Mesh>> meshes,
		HashMap<Integer, Vector3f> points, HashMap<Integer, Vector3f> normals)
		throws Exception {
		ArrayList<Node> tagList;

		tagList = findChildrenByName(root, "P");
		if (tagList.size() != 0) {
			for (int i = 0; i < tagList.size(); i++) {
				Element ele = (Element) tagList.get(i);
				// Get ID;
				int ID = Integer.parseInt(ele.getAttribute("ID"));

				// Get point values
				Vector3f pos = readVector(ele.getTextContent());

				// Add to hashmap
				points.put(ID, pos);
			}
		}

		tagList = findChildrenByName(root, "N");
		if (tagList.size() != 0) {
			for (int i = 0; i < tagList.size(); i++) {
				Element ele = (Element) tagList.get(i);
				// Get ID;
				int ID = Integer.parseInt(ele.getAttribute("ID"));

				// Get point values
				Vector3f pos = readVector(ele.getTextContent());

				// Add to hashmap
				normals.put(ID, pos);
			}
		}

		tagList = findChildrenByName(root, "MAT");
		if (tagList.size() != 0) {
			for (int i = 0; i < tagList.size(); i++) {
				// Get ID;
				int ID = Integer.parseInt(
					(((Element) tagList.get(i)).getAttribute("ID"))
				);

				// Create material
				Material m = readMaterial((Element) tagList.get(i));

				// Add to hashmap
				mats.put(ID, m);
			}
		}

		root.getNodeName();
		tagList = findChildrenByName(root, "MESH");
		if (tagList.size() != 0) {
			for (int i = 0; i < tagList.size(); i++) {
				// Only load meshes that could be referenced later
				int ID;
				if (tagList.get(i).hasAttributes()) {
					// Get ID;
					ID = Integer.parseInt(
						((Element) tagList.get(i)).getAttribute("ID")
					);

					// System.out.println("Reading Mesh ID: " +
					// String.valueOf(ID));
				} else {
					ID = backup;
					backup++;
				}

				// Create Meshes
				ArrayList<Mesh> ms = readMeshes((Element) tagList.get(i), mats,
					meshes, points, normals);

				// Shove them in hashmap
				meshes.put(ID, ms);
			}
		}
	}

	private ArrayList<Mesh> readObjects(Element rootElement,
		HashMap<Integer, Material> _mats,
		HashMap<Integer, ArrayList<Mesh>> _meshes,
		HashMap<Integer, Vector3f> _points, HashMap<Integer, Vector3f> _normals)
		throws Exception {
		// Create clones so we don't overwrite the parent's references
		@SuppressWarnings("unchecked")
		// Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats
			.clone();
		@SuppressWarnings("unchecked")
		// Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, ArrayList<Mesh>> meshes = (HashMap<Integer, ArrayList<Mesh>>) _meshes.clone();

		HashMap<Integer, Vector3f> normals = makeCopy(_normals);
		HashMap<Integer, Vector3f> points = makeCopy(_points);

		// Add defines created in this tag
		readDefines(rootElement, mats, meshes, points, normals);

		// Holds all the meshes we create
		// Since these are in objects, these are what will actually be added to
		// the model in the end.
		// Definitions outside of OBJ tags with no references from them will not
		// be drawn
		ArrayList<Mesh> createdMeshes = new ArrayList<Mesh>();

		ArrayList<Node> tagList;

		// Transform information
		Vector3f location = new Vector3f(0.0f, 0.0f, 0.0f);
		Vector3f forward = new Vector3f(0.0f, 0.0f, 1.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		readTransform(rootElement, location, forward, up);

		// Get all defined Meshes
		tagList = findChildrenByName(rootElement, "MESH");
		for (int i = 0; i < tagList.size(); i++) {
			int ID;
			if (tagList.get(i).hasAttributes()) {
				ID = Integer.parseInt(
					(((Element) tagList.get(i)).getAttribute("ID"))
				);
			} else {
				ID = backup;
				backup++;
			}

			ArrayList<Mesh> mtemp = readMeshes((Element) tagList.get(i), mats,
				meshes, points, normals);

			// Add to references
			meshes.put(ID, mtemp);
			for (Mesh m : mtemp) {
				// Add to list being returned
				m.transform(location, forward, up);
				createdMeshes.add(m);
			}
		}

		// Read any sub objects
		tagList = findChildrenByName(rootElement, "OBJECT");
		for (int i = 0; i < tagList.size(); i++) {
			// Get all Meshes from sub Objects
			ArrayList<Mesh> mtemp = readObjects((Element) tagList.get(i), mats,
				meshes, points, normals);
			for (Mesh m : mtemp) {
				createdMeshes.add(m);
			}
		}

		// Handle Mesh reference tags
		tagList = findChildrenByName(rootElement, "MESHREF");
		for (int i = 0; i < tagList.size(); i++) {
			int mref = Integer.parseInt(tagList.get(i).getTextContent());
			ArrayList<Mesh> refrenced_meshes = meshes.get(mref);
			for (Mesh m : refrenced_meshes) {
				// Transform the Mesh
				Mesh m_clone = new Mesh(m);
				m_clone.transform(location, forward, up);
				createdMeshes.add(m_clone);
			}
		}
		return createdMeshes;
	}

	// Create meshes from a mesh tag. Multiples because it groups based on
	// assigned material.
	private ArrayList<Mesh> readMeshes(Element root,
		HashMap<Integer, Material> _mats,
		HashMap<Integer, ArrayList<Mesh>> _meshes,
		HashMap<Integer, Vector3f> _points, HashMap<Integer, Vector3f> _normals)
		throws Exception {

		// Create clones so we don't overwrite the parent's references
		@SuppressWarnings("unchecked")
		// Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, Material> mats = (HashMap<Integer, Material>) _mats.clone();
		@SuppressWarnings("unchecked")
		// Not sure what check its wanting, but I ain't doin' it
		HashMap<Integer, ArrayList<Mesh>> meshes = (HashMap<Integer, ArrayList<Mesh>>) _meshes.clone();

		HashMap<Integer, Vector3f> points = makeCopy(_points);
		HashMap<Integer, Vector3f> normals = makeCopy(_normals);

		readDefines(root, mats, null, points, normals);

		// Meshes created this call
		ArrayList<Mesh> created_meshes = new ArrayList<Mesh>();

		// Faces created this call
		HashMap<Integer, ArrayList<Face>> faces = readFaces(root, mats, points,
			normals);

		// Get all defined Patches
		ArrayList<Node> tagList = findChildrenByName(root, "PATCH");
		for (int i = 0; i < tagList.size(); i++) {
			/*
			 * int ID; if(tagList.get(i).hasAttributes()){ ID =
			 * Integer.parseInt(
			 * (((Element)tagList.get(i)).getAttribute("PATCHID"))); }else{ ID =
			 * backup; backup++; }
			 * 
			 * System.out.println("Reading PATCH ID: " + String.valueOf(ID));
			 */

			ArrayList<Mesh> mtemp = readMeshes(
				(Element) tagList.get(i), mats,	meshes, points, normals
			);

			for (Mesh m : mtemp) {
				// Add to list being returned
				created_meshes.add(m);
			}
		}

		for (Integer key : faces.keySet()) {
			// For every Material, grab that set of faces and shove it into a
			// Mesh
			Mesh m = new Mesh(faces.get(key));

			// Add to list of meshes created this call
			created_meshes.add(m);

			// Set the material
			m.setMaterial(mats.get(key));
		}

		return created_meshes;
	}

	private HashMap<Integer, ArrayList<Face>> readFaces(Element root,
		HashMap<Integer, Material> _mats, HashMap<Integer, Vector3f> _points,
		HashMap<Integer, Vector3f> _normals) throws Exception {
		// Faces will be grouped by what material they are tied too
		HashMap<Integer, ArrayList<Face>> faces = new HashMap<Integer, ArrayList<Face>>();
		ArrayList<Node> tagList = findChildrenByName(root, "F");
		if (tagList.size() != 0) {
			for (int i = 0; i < tagList.size(); i++) {
				Element ele = (Element) tagList.get(i);
				int MatID = (int) readScalarTag(ele, "MATREF", true);
				Face f = new Face();

				String[] temp = { "FV1", "FV2", "FV3" };
				ArrayList<Node> vertexList = findChildrenByName(ele, temp);
				for (int j = 0; j < vertexList.size(); j++) {
					int temp2 = (int) readScalarTag(
						(Element) vertexList.get(j), "PREF", true
					);
					Vector3f temp1 = _points.get(temp2);
					f.addVertex(temp1);

					temp1 = _normals.get(
						(int) readScalarTag((Element) vertexList.get(j), "NREF", false)
					);
					if (temp1 != null) {
						f.addVertexNorm(temp1);
					}
				}
				if (faces.get(MatID) == null) {
					faces.put(MatID, new ArrayList<Face>());
				}
				faces.get(MatID).add(f);
			}
		} else {
			// throwException("Faces sought, but none found.");
		}
		return faces;
	}

	// Creates a new material from a MAT tag
	private Material readMaterial(Element root) throws Exception {
		Material m = new Material();
		m.setAmbient(readVector4fTag(root, "AMB", true));
		m.setDiffuse(readVector4fTag(root, "DIFF", true));
		m.setSpecular(readVector4fTag(root, "SPEC", false));
		//m.setEmission(readVectorTag(root, "EMISS", false));
		m.setAlpha(readScalarTag(root, "ALPHA", false));
		m.setShininess(readScalarTag(root, "SHINE", false));
		return m;
	}

	private void readTransform(Element ele, Vector3f location,
		Vector3f forward, Vector3f up) throws Exception {
		ArrayList<Node> tagList;
		tagList = findChildrenByName(ele, "TRANSFORM");
		if (tagList.size() == 1) {
			Vector3f local_location = readVectorTag(
				(Element) tagList.get(0),
				"POSITION", true
			);
			Vector3f local_forward = readVectorTag(
				(Element) tagList.get(0),
				"FORWARD", true
			);
			Vector3f local_up = readVectorTag(
				(Element) tagList.get(0), "UP",
				true
			);

			location.add(local_location);
			forward.add(local_forward);
			up.add(local_up);
		} else if (tagList.size() == 0) {
			// Do nothing, its fine.
		} else if (tagList.size() > 1) {
			throwException("Multiple Transform in one tag");
		}
	}

	private Vector3f readVectorTag(Element root, String childName,
		boolean required) throws Exception {
		ArrayList<Node> tagList;
		tagList = findChildrenByName(root, childName);
		if (tagList.size() == 1) {
			Element e = (Element) tagList.get(0);
			return readVector(e.getTextContent());
		} else if (tagList.size() == 0) {
			if (required) {
				throwException("Material Tag with no " + childName + " Tag");
			}
		} else if (tagList.size() > 1) {
			throwException("Material Tag with redundant " + childName + " Tag");
		}

		return null;
	}
	
	private Vector4f readVector4fTag(Element root, String childName,
			boolean required) throws Exception {
			ArrayList<Node> tagList;
			tagList = findChildrenByName(root, childName);
			if (tagList.size() == 1) {
				Element e = (Element) tagList.get(0);
				return readVector4f(e.getTextContent());
			} else if (tagList.size() == 0) {
				if (required) {
					throwException("Material Tag with no " + childName + " Tag");
				}
			} else if (tagList.size() > 1) {
				throwException("Material Tag with redundant " + childName + " Tag");
			}

			return null;
		}

	private Vector3f readVector(String data) {
		String[] temp = data.split(",");
		Vector3f ret = new Vector3f();

		ret.x = Float.parseFloat(temp[0]);
		ret.y = Float.parseFloat(temp[1]);
		ret.z = Float.parseFloat(temp[2]);

		return ret;
	}
	
	private Vector4f readVector4f(String data) {
		String[] temp = data.split(",");
		Vector4f ret = new Vector4f();

		ret.x = Float.parseFloat(temp[0]);
		ret.y = Float.parseFloat(temp[1]);
		ret.z = Float.parseFloat(temp[2]);
		if(ret.length() == 4)
			ret.w = Float.parseFloat(temp[3]);
		else
			ret.w = 1.0f;

		return ret;
	}

	private ArrayList<Node> findChildrenByName(Node root, String name) {
		String[] names = new String[1];
		names[0] = name;
		return findChildrenByName(root, names);
	}

	private ArrayList<Node> findChildrenByName(Node root, String[] names) {
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

	private float readScalarTag(Element root, String childName, boolean required)
		throws Exception {
		ArrayList<Node> tagList;
		tagList = findChildrenByName(root, childName);
		if (tagList.size() == 1) {
			Element e = (Element) tagList.get(0);
			return Float.parseFloat(e.getTextContent());
		} else if (tagList.size() == 0) {
			if (required) {
				throwException(
					root.getNodeName() + " Tag with no " + childName + " Tag"
				);
			}
		} else if (tagList.size() > 1) {
			throwException(
				root.getNodeName() + " Tag with redundant "	+ childName + " Tag"
			);
		}

		return -500;// ? TODO
	}

	private HashMap<Integer, Vector3f> makeCopy(
		HashMap<Integer, Vector3f> copyFrom) {

		HashMap<Integer, Vector3f> copyTo = new HashMap<Integer, Vector3f>();
		if (copyFrom.size() != 0) {
			for (Integer key : copyFrom.keySet()) {
				try {
					Vector3f temp = copyFrom.get(key);
					copyTo.put(key, new Vector3f(temp.x, temp.y, temp.z));
				} catch (Exception e) {
					System.out.println("Failed on key: " + String.valueOf(key));
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		return copyTo;
	}

	private void throwException(String message) throws Exception {
		Exception e = new Exception();
		e.initCause(new Throwable(message));
		throw e;
	}

	@Override
	public Model createModel() {
		return new Model(model);
	}
}
