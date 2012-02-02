package engine.resource;

import java.io.InputStream;

public interface Resource {
	public void loadFromFile(InputStream is) throws Exception;
	public String toXML();	
}
