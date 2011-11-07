package test;

import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;
import java.net.URISyntaxException;

public class JarContents{
	private ArrayList<String> found_file_paths;

	public JarContents() throws IOException{
		found_file_paths = new ArrayList<String>();
		readJar();
		
		String filename = "/home/adam/Desktop/jge3d.jar"; 
		
		File file = new File(filename);
		if(!filename.endsWith(".jar")){
			System.out.println("File extension does not end in JAR!");
			System.exit(0);
		}
		else if(!file.exists()){
			System.out.println("JAR does not exist!");
		}
		
		System.out.println("Loading from: " + filename);
		
		try{
			JarFile jarfile = new JarFile(filename);
			//Enumeration<JarEntry> em = jarfile.entries();
			for (Enumeration<JarEntry> em1 = jarfile.entries(); em1.hasMoreElements();) {
				//RI
				String filtered_files = em1.nextElement().toString();
				if(filtered_files.startsWith("resources")){
					filtered_files = filtered_files.replace("resources/", "");
					found_file_paths.add(filtered_files);
					System.out.println(filtered_files);
				}
			}
		}
		catch(ZipException ze){
			System.out.println(ze.getMessage());
			System.exit(0);
		}
	}
	
	public File readJar() {
		try {
			File moduleFile = new File
				(JarContents.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI()
			);
			
			System.out.println("Current module " + moduleFile.toURI().toString());
			System.out.println("Current path: " + (new File(".")).getAbsolutePath());
			
			return moduleFile;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	public ArrayList<String> getFiles() {
		return found_file_paths;
	}
}
