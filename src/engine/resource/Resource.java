package engine.resource;

import java.net.URL;

public interface Resource {
	public void loadFromFile(
		ResourceManager resource_manager, 
		URL url, 
		String extension
	) throws Exception;
	
	public String toXML();	
}
