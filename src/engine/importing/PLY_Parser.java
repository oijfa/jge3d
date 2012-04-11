package engine.importing;

import engine.render.Model;
import engine.render.model_pieces.*;
import engine.render.ubos.Material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class PLY_Parser extends Parser {
	protected class Element {
		public ArrayList<Property> properties;
		public int num_elements = 0;
		public String name = "";
		
		public Element(String name, int num_elements) {
			this.name = name;
			this.num_elements = num_elements;
			properties = new ArrayList<Property>();
		}
	}
	protected class Property {
		public boolean list = false;
		public Type list_indices;
		public Type value;
		public String name;
		
		public Property(String name, Type list_indices, Type value) {
			this.list = true;
			this.name = name;
			this.list_indices = list_indices;
			this.value = value;
		}
		
		public Property(String name, Type value) {
			this.list = false;
			this.name = name;
			this.value = value;
		}
	}
	
	public ArrayList<Element> elements;
	private ArrayList<String> comments = new ArrayList<String>();
	private String format;
	private float version;
	private BufferedReader file;
	private Model model;
	private ArrayList<Vector3f> vertices;

	public PLY_Parser() {
		super();
		model = new Model();
		elements = new ArrayList<Element>();
		vertices = new ArrayList<Vector3f>();
	}

	@Override
	public void readFile(InputStream in) throws Exception {
		file = new BufferedReader(
			new InputStreamReader(in)
		);
		
		parsePly();
	}

	@Override
	public void readUrl(String url) throws Exception {
		file = new BufferedReader(
			new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream(url)
			)
		);
		parsePly();
	}

	public void parsePly() throws Exception {
		parseHeader();
		parseBody();
	}

	private void parseHeader() throws IOException {
		String currentLine = file.readLine();
		if(!currentLine.equals("ply")) {
			System.out.println("Error: valid ply file's first line must be 'ply'");
			System.exit(1);
		}
		
		currentLine = file.readLine();
		while(currentLine != null) {
			String split_line[] = currentLine.split("\\s+");
			switch(split_line[0]) {
				case "comment":
					comments.add(split_line[1]);
					currentLine = file.readLine();
					break;
				case "format":
					format = split_line[1];
					version = Float.parseFloat(split_line[2]);
					currentLine = file.readLine();
					break;
				case "element":
					//String split_line[] = currentLine.split("\\s+");
					Element element = new Element(split_line[1], Integer.parseInt(split_line[2]));
					currentLine = file.readLine();
					while(currentLine != null &&
						 !currentLine.contains("element") &&
						 !currentLine.contains("end_header"))
					{
						readProperty(element, currentLine);			
						currentLine = file.readLine();
					}
					elements.add(element);
					break;
				case "end_header":
					return;
				default:
					System.out.println("Unknown def encountered while parsing ply");
					System.out.println("\t"+currentLine);
					break;
			}
		}
	}
	
	private void parseBody() throws Exception {
		String currentLine = file.readLine();
		Mesh mesh = new Mesh();
		for(Element element: elements) {
			switch(element.name) {
				case "vertex":
					for(int i=0; i < element.num_elements; i++) {
						parseVector(currentLine);
						currentLine = file.readLine();
					}
					break;
				case "face":
					for(int i=0; i < element.num_elements; i++) {
						parseIndex(currentLine, mesh);
						currentLine = file.readLine();
					}
					break;
				default:
					System.out.println("unknown element type: " + element.name);
					break;
			}
		}
		mesh.setMaterial(new Material());
		model.addMesh(mesh);
	}
	
	private void readProperty(Element element, String currentLine) throws IOException {
		String split_line[] = currentLine.split("\\s+");

		if(currentLine.contains("list")) {
			element.properties.add(
				new Property(
					split_line[4],
					getDataType(split_line[2]),
					getDataType(split_line[3])
				)
			);
		} else {
			element.properties.add(
				new Property(
					split_line[2],
					getDataType(split_line[1])
				)
			);
		}
	}

	private Type getDataType(String type) {
		switch(type) {
			case "char": return Character.TYPE;
			case "uchar": return Character.TYPE;
			case "short": return Short.TYPE;
			case "ushort": return Short.TYPE;
			case "int":	return Integer.TYPE;
			case "uint": return Integer.TYPE;
			case "float": return Float.TYPE;
			case "double": return Double.TYPE;
			default:
				//System.out.println("Unknown datatype: " + type);
				return null;
		}
	}
	
	public void parseVector(String string) throws Exception {
		String[] line = string.trim().split("\\s+");

		vertices.add(
			new Vector3f(
				Float.parseFloat(line[1]),
				Float.parseFloat(line[2]),
				Float.parseFloat(line[3])
			)
		);
	}
	
	private void parseIndex(String currentLine, Mesh mesh) {
		String split_line[] = currentLine.split("\\s+");
		
		Face face = new Face();
		for(int i=0; i < Integer.parseInt(split_line[0]); i++) {
			face.addVertex(vertices.get(Integer.parseInt(split_line[i+1]))); 
		}
		
		mesh.addFace(new Face(face));
	}
	
	public String getFormat() {
		return format;
	}
	public float getVersion() {
		return version;
	}

	@Override
	public Model createModel() {
		return new Model(model);
	}

}