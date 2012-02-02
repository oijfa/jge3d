package engine.resource;

import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;

public class JarContents implements ContentExtractor{
	private ArrayList<String> found_file_paths;

	public JarContents(String path){
		found_file_paths = new ArrayList<String>();
		File file = new File(path);
		if(!path.endsWith(".jar")){
			System.out.println("File extension does not end in JAR!");
			System.exit(0);
		}
		else if(!file.exists()){
			System.out.println("JAR does not exist!");
			System.exit(0);
		}
		
		try{
			JarFile jarfile;
			jarfile = new JarFile(path);
		
			for (Enumeration<JarEntry> em1 = jarfile.entries(); em1.hasMoreElements();) {
				String file_names = em1.nextElement().toString();
				found_file_paths.add(file_names);
			}
		}catch(ZipException ze){
			System.out.println(ze.getMessage());
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public ArrayList<String> getFiles() {
		return found_file_paths;
	}
}

