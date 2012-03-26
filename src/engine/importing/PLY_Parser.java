/*
 * 	Reads in obj files, a fairly simplistic model format.
 * 
 * 
 */
package engine.importing;

import engine.render.Model;
import engine.render.model_pieces.*;
import engine.render.ubos.Material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3f;

public class PLY_Parser extends Parser {
	private HashMap<String, HashMap<String,Object>> elements = new HashMap<String, HashMap<String,Object>>();
	private HashMap<String, HashMap<String,Object>> properties = new HashMap<String, HashMap<String,Object>>();
	private ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> vertexNormals = new ArrayList<Vector3f>();
	private ArrayList<String> comments = new ArrayList<String>();
	private String format;
	private float version;
	private BufferedReader file;
	private Model model;

	public PLY_Parser() {
		super();
		model = new Model();
	}

	@Override
	public void readFile(InputStream in) throws Exception {
		parseObj(
			new BufferedReader(
				new InputStreamReader(in)
			)
		);
	}

	@Override
	public void readUrl(String url) throws Exception {
		file = new BufferedReader(
			new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream(url)
			)
		);
		parseObj(file);
	}

	public void parseObj(BufferedReader file) throws Exception {
		Mesh mesh = new Mesh();
		
		parseHeader();
		
		mesh.setMaterial(new Material());
		model.addMesh(mesh);
	}

	private void parseHeader() throws IOException {
		String currentLine = file.readLine();
		if(!currentLine.equals("ply")) {
			System.out.println("Error: valid ply file's first line must be 'ply'");
			System.exit(1);
		}
		
		while(currentLine != null) {
			String split_line[] = currentLine.split("\\s+");
			switch(split_line[0]) {
				case "comment":
					comments.add(split_line[1]);
					break;
				case "format":	
					format = split_line[1];
					version = Float.parseFloat(split_line[2]);
				case "element":
					readElement(currentLine);
					file.readLine();
					readProperty(currentLine);
					break;
				case "end_header":
					return;
				default:
					System.out.println("Unknown def encountered while parsing obj");
					System.out.println("\t"+currentLine);
					break;
			}
			currentLine = file.readLine();
		}
	}
	
	private void readProperty(String currentLine) throws IOException {
		String split_line[] = currentLine.split("\\s+");
		if(currentLine.contains("list")) {
			for(int i=2; i<split_line.length; i++) {
				//currentLine[i];
			}
		} else {
			
		}
		currentLine = file.readLine();
	}
	
	private void readElement(String currentLine) {
		
	}

	
	private Vector3f parseVector(String string) throws Exception {
		String[] line = string.trim().split("\\s+");

		return new Vector3f(
			Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])
		);
	}

	@Override
	public Model createModel() {
		return new Model(model);
	}

}