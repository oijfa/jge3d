package engine.resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import engine.input.InputMap;
import engine.render.Model;
import engine.render.Shader;

public class ResourceManager {
	String program_path;
	HashMap<String,ArrayList<ResourceItem>> resources;
	
	HashMap<String, Class<? extends Resource>> cat_to_class;
	
	public ResourceManager(){
		loadCatToClass();
		
		resources = new HashMap<String, ArrayList<ResourceItem>>();
		
		program_path = (new File(".")).getAbsolutePath();
		ContentExtractor c;
		if(program_path.endsWith(".jar")){
			c = new JarContents(program_path);
		}else{
			if(program_path.endsWith(".")){
				program_path = program_path.substring(0, program_path.length()-1);
			}
			c = new FileSystemContents(program_path);
		}
		
		ArrayList<String> file_list = filter_files(c.getFiles());
		
		for(String file_path : file_list){
			ResourceItem tmp = new ResourceItem();
			tmp.category = getCategory(file_path);
			tmp.name = getName(file_path);
			tmp.path = file_path;
			
			tmp.item_class = cat_to_class.get(tmp.category);
			
			if(tmp.item_class != null){
				System.out.println("Loaded Resource: (" + tmp.name + "," + tmp.category + ")");
				addResource(tmp);
			}else{
				System.out.println("Didn't load resource: " + tmp.path);
				System.out.println("  No class specified.");
			}
		}
	}
	
	private ArrayList<String> filter_files(ArrayList<String> files){
		ArrayList<String> filtered = new ArrayList<String>();
		
		for(String name : files){
			if(name.matches(".*resources.*") && !name.matches(".*ignore.*")){
				filtered.add(name);
			}
		}
		
		return filtered;
	}
	
	private void loadCatToClass() {
		cat_to_class = new HashMap<String, Class<? extends Resource>>();
		
		cat_to_class.put("inputmaps", InputMap.class);
		cat_to_class.put("models", Model.class);
		cat_to_class.put("shaders", Shader.class);
	}

	public void saveResource(String name, String category){
		ResourceItem res = findResource(name, category);
		
		FileWriter fstream;
		try {
			fstream = new FileWriter(res.path);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(res.data.toXML());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Object getResource(String name, String category){
		ResourceItem res = findResource(name, category);
		
		if( res != null){
			if( res.data == null ){
				try {
					File f = new File(res.path);
				
					System.out.println(f.getAbsolutePath());
					
					InputStream in = new FileInputStream(f);
					res.data = res.item_class.newInstance();
					res.data.loadFromFile(in);
					
					if( res.item_class == Model.class ){
						Model model = (Model)res.data;
						Shader shader = (Shader)getResource("default", "shaders");
						model.setShader(shader);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (Exception e){
					e.printStackTrace();
					System.exit(0);
				}	
			}
			
			return res.data;
		}
		
		try{
			throw new Exception();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("No such resource found: (" + name + "," + category + ")");
		System.exit(0);
		return null;
	}
	
	public void createResource(String name, String category, String path, Resource resource){
		ResourceItem ri = new ResourceItem();
		ri.name = name;
		ri.category = category;
		ri.item_class = resource.getClass();
		ri.path = path;
		
		addResource(ri);
	}
	
	private String getName(String file_path) {
		return file_path.substring(file_path.lastIndexOf("/") + 1, file_path.lastIndexOf(".") );
	}

	private String getCategory(String file_path) {
		try{
			String tmp = file_path.substring(file_path.indexOf("resources") + 10);
			tmp = tmp.substring(0, tmp.indexOf("/"));
			return tmp;
		}catch(java.lang.StringIndexOutOfBoundsException e){
			System.out.println(file_path);
			System.exit(0);
		}
		return "";
	}

	private void addResource(ResourceItem r){
		if(!resources.containsKey(r.category)){
			resources.put(r.category,(new ArrayList<ResourceItem>()));
		}
		
		resources.get(r.category).add(r);
	}
	
	private ResourceItem findResource(String name, String category){
		ArrayList<ResourceItem> cat = resources.get(category);
		if( cat != null){
			for(ResourceItem res : cat ){
				if( res.name.equals(name) ){
					return res;
				}
			}
		}
		return null;
	}
	
	class ResourceItem{
		public String category;
		public String name;
		public String path;
		public Class<? extends Resource> item_class;
		public Resource data;
	}
}
