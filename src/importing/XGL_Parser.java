/*
 * 	Parser for XGL files.  Turns out to be a fairly shitty format, but Autocad
 * 		exports it, and its fairly simple, so it was included.
 */

package importing;

import importing.pieces.Material;
import importing.pieces.Model;
import importing.pieces.Object_3D;
import importing.pieces.World;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.vecmath.Vector3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XGL_Parser extends Parser{
	HashMap<String, String> nameConversion;
	
	XGL_Parser()
	{
		String[] tagNames = { "ALPHA", "SHINE", "AMB", "DIFF", "SPEC", "EMISS" };
		String[] convertedNames = {"Alpha", "Shine", "Ambient", "Diffuse", "Specular", "Emission"};
		
		nameConversion = new HashMap<String, String>();
		for(int i = 0; i < tagNames.length; i++)
		{
			nameConversion.put(tagNames[i], convertedNames[i]);
		}
	}
	
	private static Document parseXglFile(String filePath)
	{
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			//get instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation
			dom = db.parse(filePath);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return dom;
	}
	
	private void parseDocument(Document dom) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		//.
		//Now that we've got the file in dom format, loop through elements
		Element rootElement = dom.getDocumentElement(); //Should be a WORLD tag
		
		String[] defineTags = { "MAT", "OBJECT", "MESH", "LINESTYLE", "POINTSTYLE", "TEXTURE", "TEXTURERGB", "TEXTURERGBA", "TC"};
		String[] requiredTags = {"LIGHTING", "BACKGROUND" };
		String[] optionalTags = { "DATA", "NAME" };
		
		NodeList tagList;
		
		World newWorld = new World();
		
		/*
		for(Method m: this.getClass().getDeclaredMethods())
		{
			System.out.println(m.getName());
		}
		*/
		/*tagList = rootElement.getChildNodes();
		for(int i = 0; i < tagList.getLength(); i++ )
		{
			System.out.println( "Node " + i + ": " + tagList.item(i).getNodeName());
		}*/
		
		//Handle define tags
		for(String tagName : defineTags)
		{
			tagList = rootElement.getElementsByTagName(tagName);
			if( tagList != null && tagList.getLength() > 0)
			{
				
				for( int i =0; i < tagList.getLength(); i++)
				{
					Method method;

					method = this.getClass().getDeclaredMethod("handle" + tagName, Element.class, Object.class);
					method.invoke(this, tagList.item(i), newWorld);
				}
			}else
			{
				System.out.println("No define tag " + tagName + " in WORLD");
				//throw exception?
			}
		}
		
		//Handle required tags
		for(String tag : requiredTags)
		{
			tagList = rootElement.getElementsByTagName(tag);
			if( tagList != null && tagList.getLength() == 1)
			{
				
			}else
			{
				System.out.println("Missing " + tag + " in WORLD");
				//TODO: throw exception?
			}
		}
		
		//Handle Optional Tags
		for(String tag : optionalTags)
		{
			tagList = rootElement.getElementsByTagName(tag);
			if( tagList != null && tagList.getLength() > 0)
			{
			
			}else
			{
				//Everything's fine and dandy, but let us know for Debugging purposes
				System.out.println("Missing optional " + tag + " in WORLD");
			}
		}
	}

	
	
	private void handleMAT(Element rootElement, Object_3D parent) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{	
		Material newMat = new Material();
		String[] possibleSubTags = { "ALPHA", "SHINE", "AMB", "DIFF", "SPEC", "EMISS"};
		String[] requiredTags = {"AMB", "DIFF"}; 
	
		NodeList tagList;
		
		//for each subtag
		for( String tagName : possibleSubTags)
		{
			//all children for this tag(name)
			tagList = rootElement.getElementsByTagName(tagName);
			//if tags exist and do not occur more than once
			if( tagList != null && tagList.getLength() == 1)
			{
				Method method;

				//create an instance of the method
				
				if( !(tagList.item(0).getTextContent().contains(",")))
				{
					method = newMat.getClass().getDeclaredMethod("set" + nameConversion.get(tagName), float.class);
					Object[] args = { (float) Float.parseFloat(tagList.item(0).getTextContent()) };
					method.invoke(newMat, args );
				}else
				{
					String[] temp = tagList.item(0).getTextContent().split(",");
					if( temp.length == 3)
					{
						method = newMat.getClass().getDeclaredMethod("set" + nameConversion.get(tagName), Vector3f.class);
						Object[] args = {new Vector3f(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]),Float.parseFloat(temp[2])) };
						method.invoke(newMat, args );
					}else
					{
						System.out.println("Diffuse tag has wrong number of numbers");
					}
				}
			}else
			{
				for( String reqTag : requiredTags)
				{
					if(reqTag.equals(tagName))
					{
						System.out.println("Problem with MAT Tag, " + tagName);
					}
				}
			}
		}
				
		//Set the material reference
		newMat.setReference(Integer.parseInt(rootElement.getAttribute("ID")));
		
		//parent.addMaterial( newMat );
	}
	
	@SuppressWarnings("unused")
	private void handleOBJECT(Element rootElement, Object parent) throws FileNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Object_3D newObj = new Object_3D();
		//String[] possibleSubTags = { "MESH", "MESHREF", "TRANSFORM", "OBJECT", "DATA", "NAME"};
	
		NodeList tagList;
		
		//Pull in the Transform tag
		tagList = rootElement.getElementsByTagName("TRANSFORM");
		if( tagList != null && tagList.getLength() == 1)
		{
			handleTRANSFORM((Element)tagList.item(0), newObj);
		}
		
		//Pull in the Mesh tag
		tagList = rootElement.getElementsByTagName("MESH");
		if( tagList != null && tagList.getLength() == 1)
		{
			handleMESH((Element)tagList.item(0), newObj);
		}
		else
		{
			tagList = rootElement.getElementsByTagName("MESHREF");
			if( tagList != null && tagList.getLength() == 1)
			{
				newObj.setMeshRef(Integer.parseInt(tagList.item(0).getTextContent()));
			}else
			{
				System.out.println("Funky Meshes inside an Object");
			}
		}
		
		//Pull in all data tags
		tagList = rootElement.getElementsByTagName("DATA");
		if( tagList != null && tagList.getLength() > 0)
		{
			for( int i = 0; i < tagList.getLength(); i++ )
			{
				handleDATA((Element)tagList.item(i), newObj);
			}
		}
		
		//Pull in name tag
		tagList = rootElement.getElementsByTagName("NAME");
		if( tagList != null && tagList.getLength() == 1)
		{
			newObj.setName(tagList.item(0).getTextContent());
		}
		
		//Pull in all mat tags
		tagList = rootElement.getElementsByTagName("MAT");
		if( tagList != null && tagList.getLength() > 0)
		{
			for( int i = 0; i < tagList.getLength(); i++ )
			{
				handleMAT((Element)tagList.item(i), newObj);
			}
		}
	}
	
	private void handleDATA(Element item, Object_3D newObj) {
		
	}

	private void handleTRANSFORM(Element item, Object_3D newObj) {
		
	}

	private void handleMESH( Element rootElement, Object parent)
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handleLINESTYLE()
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handlePOINTSTYLE()
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handleTEXTURE()
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handleTEXTURERGB()
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handleTEXTURERGBA()
	{
		
	}
	
	@SuppressWarnings("unused")
	private void handleTC()
	{
		
	}
	
	@Override
	public void readFile(String fileName) {
		Document dom;
		dom = parseXglFile("./lib/legoman.xgl");
		
		try {
			parseDocument(dom);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Model createModel() {
		return new Model(world);
	}
}
