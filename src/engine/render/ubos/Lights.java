package engine.render.ubos;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import engine.render.UBO.Type;

public class Lights implements UBOInterface {
    private ArrayList<Light> lights;
    //private static final int size = 24;
    private static final String name = "alights";
    
	public Lights()	{
		lights = new ArrayList<Light>();
	}
		
	public void add(Light light) {
		lights.add(light);
	}
	
	public FloatBuffer createBuffer(int block_size, IntBuffer offsets) {
		int sub_block_size=0;
		if(lights.size() > 0) {
			FloatBuffer combined_buffers = BufferUtils.createFloatBuffer(
				block_size/4
			);
			IntBuffer sub_offset = BufferUtils.createIntBuffer(
				offsets.capacity()/lights.size()
			);
			
			for(int j=0; j<lights.size();j++) {
				for(int i=0;i<offsets.capacity()/lights.size();i++) {
					sub_offset.put(
						offsets.get(j*(offsets.capacity()/lights.size())+i)
						-
						offsets.get((j*(offsets.capacity()/lights.size())))
					);
				}
				sub_offset.flip();
				
				if(j < lights.size()-1) {
					sub_block_size = 
						offsets.get((j+1)*(offsets.capacity()/lights.size()))
						-
						offsets.get(j*(offsets.capacity()/lights.size()));
				} else {
					sub_block_size = 
						block_size/4
						-
						offsets.get(j*(offsets.capacity()/lights.size()));
				}
				
				combined_buffers.put(
					lights.get(j).createBuffer(
						sub_block_size,
						sub_offset
					)
				);
				
				sub_offset.clear();
			}
			
			combined_buffers.flip();

			return combined_buffers;
		}
		return null;
	}
	
	public int getSize() {
		return lights.size()*lights.get(0).getSize();
	}
	
	public String[] getNames() {
		int size=0;
		for(int i=0; i<lights.size();i++) {
			size += lights.get(i).getSize();
		}
		String[] names = new String[size];
		for(int i=0; i<lights.size();i++) {
			String temp_names[] = lights.get(i).getNames();
			for(int j=0; j<temp_names.length; j++) {		
				names[i*temp_names.length+j] = name + "." + temp_names[j];
			}
		}
	
		return names;
	}

	public Type getType() {
		return Type.LIGHT;
	}
	
	public String getName() {
		return name;
	}
}
