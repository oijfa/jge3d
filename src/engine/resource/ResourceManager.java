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

import engine.Engine;
import engine.input.InputMap;
import engine.render.Model;
import engine.render.Shader;
import engine.render.model_pieces.Texture;

public class ResourceManager {
	/*
	 * Basically just a struct for keeping internal info
	 */
	public class ResourceItem{
		public String category;
		public String name;
		public String path;
		public Class<? extends Resource> item_class;
		public Resource data;
	}
	
	//Path we are currently running from
	String program_path;
	
	//Holds the list of resources in each category.
	HashMap<String,ArrayList<ResourceItem>> resources;
	
	//Used to convert categories into their class names
	HashMap<String, Class<? extends Resource>> cat_to_class;

	private Engine engine;
	
	public ResourceManager(Engine engine){
		this.engine = engine;
		//Setup cat_to_class hashmap
		loadCatToClass();
		
		resources = new HashMap<String, ArrayList<ResourceItem>>();
		
		/*
		 * Get current path so that we can determine whether being
		 * 	Run from a jar or class files.
		 * 
		 * Each of those situations has its own file loader.
		 */
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
		
		/*
		 * Regardless of which method was used, we want a list of
		 * 	files available to us.
		 */
		ArrayList<String> file_list = filter_files(c.getFiles());
		
		
		/*
		 * Loop through each file, creating a resource item for it.
		 * 	Don't load the file yet, we do that as each resource is needed.
		 */
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
	
	/*
	 * Filter out the files we want to ignore.  
	 * 	Anything that's not in the resources folder is thrown out.
	 * 
	 * 	Anything with "ignore" in its path is also thrown out.
	 * 		\ This lets us keep the theme images and such in the 
	 * 		\	Resource folder without having a seperate resource
	 * 		\	for each one. 
	 */
	private ArrayList<String> filter_files(ArrayList<String> files){
		ArrayList<String> filtered = new ArrayList<String>();
		
		for(String name : files){
			if(name.matches(".*resources.*") && !name.matches(".*ignore.*")){
				filtered.add(name);
			}
		}
		
		return filtered;
	}
	
	/*
	 * Setup the hash that lets us pull the class name from the category
	 */
	private void loadCatToClass() {
		cat_to_class = new HashMap<String, Class<? extends Resource>>();
		
		cat_to_class.put("inputmaps", InputMap.class);
		cat_to_class.put("models", Model.class);
		cat_to_class.put("shaders", Shader.class);
		cat_to_class.put("textures", Texture.class);
	}

	/*
	 * Write out any changes that have been made to the resource.
	 */
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
	
	
	/*
	 * Load the resource on the fly, or just return it if it has been
	 * 	loaded previously.
	 */
	public Object getResource(String name, String category){
		ResourceItem res = findResource(name, category);
		try {
			if( res != null){
				//Only load it if we haven't before.
				if( res.data == null ){
					String[] extension;
					
					
					//Create a stream
					File f = new File(res.path);
					InputStream in = new FileInputStream(f);
					
					
					extension = res.path.split("\\.");
					
					//Pass to interface
					res.data = res.item_class.newInstance();
					res.data.loadFromFile(this, in, extension[extension.length-1]);
				}
				
				return res.data;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		/*
		 * TODO:  Return a default for each category if nothing else
		 * 	was found, or there was an error.
		 */
		System.out.println("No such resource found: (" + name + "," + category + ")");
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
		try {
			return file_path.substring(file_path.lastIndexOf("/") + 1, file_path.lastIndexOf(".") );
		} catch ( java.lang.StringIndexOutOfBoundsException e) {
			System.out.println(file_path + " is missing a file extension");
		}
		return null;
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
	
	/*
	 * Search through the resource list
	 */
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
	
	public ArrayList<ResourceItem> getResourcesInCategory(String category) {
		return resources.get(category);
	}
	
	public HashMap<String, ArrayList<ResourceItem>> getResources() {
		return resources;
	}

	public Engine getEngine() {
		return engine;
	}
}
