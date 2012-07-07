package engine.render;

import engine.resource.Resource;
import engine.resource.ResourceManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

import com.bulletphysics.linearmath.Transform;

import engine.entity.Entity;

public class TextureBuffer implements Resource{
	/*
    * if the shaders are setup ok we can use shaders, otherwise we just
    * use default settings
    */
    private boolean useShader=false;
    
    /*
    * program shader, to which is attached a vertex and fragment shaders.
    * They are set to 0 as a check because GL will assign unique int
    * values to each
    */
    private int shader=0;
    private int vertShader=0;
    private int fragShader=0;
    private int geomShader=0;
    private HashMap<String, UBO> ubo_interfaces;
    
    public TextureBuffer() {
    	
    }
    
    public TextureBuffer(TextureBuffer shader) {
    	this.vertShader = shader.vertShader;
    	this.fragShader = shader.fragShader;
    	this.geomShader = shader.geomShader;
    	this.shader = shader.shader;
    	this.useShader = shader.useShader;
    }
    
    /*
     * With the exception of syntax, setting up vertex and fragment shaders
     * is the same.
     * @param the name and path to the vertex shader
     */
     private int createVertShader(String vertexCode){
         //vertShader will be non zero if successfully created
         vertShader=ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
         
         //if created, convert the vertex shader code to a String
         if(vertShader==0){return 0;}

         /*
         * associate the vertex code String with the created vertex shader
         * and compile
         */
         ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
         ARBShaderObjects.glCompileShaderARB(vertShader);

         //if there was a problem compiling, reset vertShader to zero
         if(!printLogInfo(vertShader)){
        	 System.out.println("ERROR [vertshader id:" + vertShader + "]:\n" + vertexCode);
             vertShader=0;
         }
         //if zero we won't be using the shader
         return vertShader;
     }

     //same as per the vertex shader except for method syntax
     private int createFragShader(String fragCode){
     	//fragShader will be non zero if successfully created
         fragShader=ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
         
         //if created, convert the vertex shader code to a String
         if(fragShader==0){return 0;}

         /*
         * associate the vertex code String with the created vertex shader
         * and compile
         */
         ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
         ARBShaderObjects.glCompileShaderARB(fragShader);
         //if there was a problem compiling, reset vertShader to zero
         if(!printLogInfo(fragShader)){
        	 System.out.println("ERROR [fragshader id:" + fragShader + "]:\n" + fragCode);
             fragShader=0;
         }
         //if zero we won't be using the shader
         return fragShader;
     }
     
   //same as per the vertex shader except for method syntax
     private int createGeomShader(String geomCode){
     	//fragShader will be non zero if successfully created
    	 geomShader=ARBShaderObjects.glCreateShaderObjectARB(ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB);
         
         //if created, convert the vertex shader code to a String
         if(geomShader==0){return 0;}

         /*
         * associate the vertex code String with the created vertex shader
         * and compile
         */
         ARBShaderObjects.glShaderSourceARB(geomShader, geomCode);
         ARBShaderObjects.glCompileShaderARB(geomShader);
         //if there was a problem compiling, reset vertShader to zero
         if(!printLogInfo(geomShader)){
        	 System.out.println("ERROR [fragshader id:" + geomShader + "]:\n" + geomCode);
        	 geomShader=0;
         }
         //if zero we won't be using the shader
         return geomShader;
     }
     
    /*
    * If the shader was setup successfully, we use the shader. Otherwise
    * we run normal drawing code.
    */
    public void startShader(int vbo_id, Entity ent){
    	if(useShader) {            
     		//Adjust the position and rotation of the object from physics
    		Transform transform_matrix = new Transform();
    		transform_matrix = ent.getCollisionObject().getWorldTransform(new Transform());
    		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
    		
    		float[] body_matrix = new float[16];
    		transform_matrix.getOpenGLMatrix(body_matrix);
    		buf.put(body_matrix);
    		buf.flip();

        	//*****UBO setup*****//
    		//world space transform
    		ARBShaderObjects.glUseProgramObjectARB(shader);
    		int transform = ARBShaderObjects.glGetUniformLocationARB(shader, "transform");
    		ARBShaderObjects.glUniformMatrix4ARB(transform, false, buf);
    		
    		//world space scale op
    		buf.clear();
    		buf = BufferUtils.createFloatBuffer(4);
    		
    		Vector3f scalevec = ent.getCollisionObject().getCollisionShape().getLocalScaling(new Vector3f());
    		buf.put(scalevec.x);
    		buf.put(scalevec.y);
    		buf.put(scalevec.z);
    		buf.put(1.0f);
    		buf.flip();
    		int scale = ARBShaderObjects.glGetUniformLocationARB(shader, "scale");
    		ARBShaderObjects.glUniform4ARB(scale, buf);
    		buf.clear();
    		
    		ubo_interfaces.get("Material").setInterface(ent.getModel().getMesh(0).getMaterial());
    		
    		//parse material and light uniforms
    		for(UBO ubo: ubo_interfaces.values()) {
    			ubo.bufferData();
    		}
        }
    }
    
    public void stopShader() {
        if(useShader) {
	        //release the shader
	        ARBShaderObjects.glUseProgramObjectARB(0);
        }
    }
    
    /*
    * oddly enough, checking the success when setting up the shaders is
    * verbose upon success. If the reference iVal becomes greater
    * than 1, the setup being examined (obj) has been successful, the
    * information gets printed to System.out, and true is returned.
    */
    private static boolean printLogInfo(int obj){
    	IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(
        	obj,
        	ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB,
        	iVal
        );

        int length = iVal.get();
        // We have some info we need to output.
        if(length == 1) {
        	return true;
        } else {
        	ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log:\n"+out);   
            return false;
        }        
    }
    
	protected String getShaderText(InputStream in, String tag_name) {
		String vertexCode="";
        String line;
        try{
        	InputStreamReader is = new InputStreamReader(in);
        	BufferedReader reader = new BufferedReader(is);
           	while((line=reader.readLine())!=null){
           		vertexCode+=line + "\n";
           	}
        }catch(Exception e){
            System.out.println("Failed to read " + tag_name + " shading code: " + in.toString());
            return "";
        }
        
        return vertexCode;
	}

	public int getShaderID() {
		return shader;
	}

	@Override
	public void loadFromFile(ResourceManager resource_manager, InputStream is, String extension) throws Exception {
		ubo_interfaces = new HashMap<String, UBO>();
		
        //create the shader program. If OK, create vertex
        //and fragment shaders
    	shader=ARBShaderObjects.glCreateProgramObjectARB();
    	
    	InputStreamReader isr = new InputStreamReader(is);
    	BufferedReader br = new BufferedReader(isr);
    	String line = br.readLine();
    	
    	String vert = new String();
    	String frag = new String();
    	String geom = new String();
    	
    	while(line != null) {
    		switch(line) {
    			case "###VERT###": 
    				line = br.readLine();
    				while(line != null && !line.contains("###ENDVERT###")) {
    					vert += line + "\n";
    					line = br.readLine();
    				}
    				line = br.readLine();
    				break;
    			case "###FRAG###":
    				line = br.readLine();
    				while(line != null && !line.contains("###ENDFRAG###")) {
    					frag += line + "\n";
    					line = br.readLine();
    				}
    				line = br.readLine();
    				break;
    			case "###GEOM###":
    				line = br.readLine();
    				while(line != null && !line.contains("###ENDGEOM###")) {
    					geom += line + "\n";
    					line = br.readLine();
    				}
    				line = br.readLine();
    				break;
    			default:
    				line = br.readLine();
    				break;
    		}
    	}

        if(shader!=0){
            vertShader=createVertShader(vert);
            fragShader=createFragShader(frag);
            if(!geom.equals("")) {
            	geomShader=createGeomShader(geom);
            }
            
        } else {
        	useShader=false;
        }

        //if the vertex and fragment shaders setup sucessfully,
        //attach them to the shader program, link the shader program
        //(into the GL context I suppose), and validate
        if(vertShader != 0 && fragShader != 0){
            ARBShaderObjects.glAttachObjectARB(shader, vertShader);
            ARBShaderObjects.glAttachObjectARB(shader, fragShader);
            if(geomShader != 0)
            	ARBShaderObjects.glAttachObjectARB(shader, geomShader);
            ARBShaderObjects.glLinkProgramARB(shader);
            ARBShaderObjects.glValidateProgramARB(shader);
            useShader=printLogInfo(shader);
        } else {
        	useShader=false;
        	System.out.println("Failed to create shader");
        	System.out.println("\tvertShader: " + vertShader + " && fragShader: " + fragShader + "geomShader: " + geomShader);
        }
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
