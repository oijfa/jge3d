package engine.resource;

import java.io.InputStream;

public interface Resource {
	public void loadFromFile(
		ResourceManager resource_manager, 
		InputStream is, 
		String extension
	) throws Exception;
	
	public String toXML();	
}
