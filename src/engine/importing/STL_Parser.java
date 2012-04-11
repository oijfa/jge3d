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
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.vecmath.Vector3f;

public class STL_Parser extends Parser {
	private Model model;
	private Mesh mesh;
	private BufferedReader file;
	private String name;	
	
	public STL_Parser() {
		super();
		model = new Model();
	}

	@Override
	public void readFile(InputStream in) throws Exception {
		file = new BufferedReader(
			new InputStreamReader(in)
		);
		
		parseSTL();
	}

	@Override
	public void readUrl(String url) throws Exception {
		file = new BufferedReader(
			new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream(url)
			)
		);
		parseSTL();
	}

	public void parseSTL() throws Exception {
		String currentLine = file.readLine();
		String[] split_line = currentLine.trim().split("\\s+");
		if(currentLine.contains("solid")) {
			split_line = currentLine.trim().split("\\s+");
			if (split_line.length == 2)
				name = split_line[1];
		} else {
			System.out.println("Error: valid STL file's first line must be 'solid [name]'");
			System.exit(1);
		}
		
		mesh = new Mesh();
		currentLine = file.readLine();
		while(!currentLine.contains("endsolid")) {
			parseFacet(currentLine);
			currentLine = file.readLine();
		}
		
		mesh.setMaterial(new Material());
		model.addMesh(mesh);
	}
	
	private void parseFacet(String currentLine) throws Exception {
		String[] split_line;
		Face face = new Face();
		while(!currentLine.contains("endfacet")) {
			split_line = currentLine.trim().split("\\s+");
			if(currentLine.contains("facet normal")) {
				face.addVertexNorm(
					new Vector3f(
						Float.parseFloat(split_line[2]),
						Float.parseFloat(split_line[3]),
						Float.parseFloat(split_line[4])
					)
				);
			} else if(currentLine.contains("facet") && !currentLine.contains("normal")) {
				currentLine = file.readLine();
			} else {
				System.out.println("STL parsing failed: expected facet [normal], but got" + currentLine);
				System.exit(1);
			}
		
			parseLoop(face, currentLine);
			currentLine = file.readLine();
		}
		mesh.addFace(face);
	}
	
	private void parseLoop(Face face, String currentLine) throws Exception {
		currentLine = file.readLine();
		if(currentLine.contains("outer loop")) {
			currentLine = file.readLine();
			while(!currentLine.contains("endloop")) {
				face.addVertex(parseVertex(currentLine));
				currentLine = file.readLine();
			}				
		} else {
			System.out.println("STL parsing failed: expected 'outer loop', but got" + currentLine);
			System.exit(1);
		}
	}

	public Vector3f parseVertex(String string) throws Exception {
		Vector3f vertex = new Vector3f();
		String[] line = string.trim().split("\\s+");
		if(line[0].equals("vertex")) {
			vertex = new Vector3f(
				Float.parseFloat(line[1]),
				Float.parseFloat(line[2]),
				Float.parseFloat(line[3])
			);
		} else {
			System.out.println("STL parsing failed: expected 'vertex <f1> <f2> <f3>', but got" + string);
			System.exit(1);
		}
		return vertex;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Model createModel() {
		return new Model(model);
	}

}