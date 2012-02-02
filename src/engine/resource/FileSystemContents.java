package engine.resource;

import java.io.File;
import java.util.ArrayList;

public class FileSystemContents implements ContentExtractor {
	ArrayList<String> contents;
	
	public FileSystemContents(String program_path) {
		contents = getContents(program_path);
	}

	public ArrayList<String> getFiles(){
		return contents;
	}
	
	private ArrayList<String> getContents(String path){
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		
		if( file.isDirectory() ){
			for(File child : file.listFiles()){
				ArrayList<String> child_contents = getContents(child.getAbsolutePath());
				
				for(String child_content : child_contents){
					files.add(child_content);
				}
			}
		}else{
			files.add(path);
		}
		return files;
	}
}
