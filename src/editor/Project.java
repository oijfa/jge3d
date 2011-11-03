package editor;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Project {
	private String path;
	private String name;
	
	public Project(String name, String path) {
		
	}
	/*
	public void getContents() {
		URL url = this.getClass().getResource("Project.class");
		String scheme = url.getProtocol();
		if (!"jar".equals(scheme))
		  throw new IllegalArgumentException("Unsupported scheme: " + scheme);
		JarURLConnection con = (JarURLConnection) url.openConnection();
		JarFile archive = con.getJarFile();
		// Search for the entries you care about.
		Enumeration<JarEntry> entries = archive.entries();
		while (entries.hasMoreElements()) {
		  JarEntry entry = entries.nextElement();
		  if (entry.getName().startsWith(path)) {
		    //...
		  }
		}
	}
	*/
}
