package engine.importing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.net.URL;

import javax.vecmath.Vector3f;

import engine.render.Model;
import engine.render.model_pieces.Face;

public class InternalModelParser {
	private static final int SIZE_TRANSLATION = 12;
	private static final int SIZE_ROTATION = 8;
	private static final int SIZE_NUM_PRIMS = 2;
	private static final int SIZE_BLOCK = 28;
	private static final int SIZE_FACE = 60;
	private static final int SIZE_NUM_MATS = 2;
	private static final int SIZE_MAT = 52;
	private static final int SIZE_NUM_MESHES = 2;
	private static final int SIZE_NUM_REFS = 2;
	
	DataInputStream input;
	
	boolean use_blocks;
	int number_of_primatives;
	int number_of_materials;
	int number_of_meshes;
	int number_of_objects;
	
	
	Model readFile(URL url) throws Exception{
		try{
			byte[] bytes;
			
			input = new DataInputStream(url.openStream());
			
			use_blocks = (input.readByte()==1);
	
			bytes = new byte[SIZE_NUM_PRIMS];
			number_of_primatives = byteArrayToInt(bytes);
			
			for(int i = 0; i < number_of_primatives; i++){
				if(use_blocks){
					bytes = new byte[SIZE_BLOCK];
					readBlock(bytes);
				}else{
					bytes = new byte[SIZE_FACE];
					readFace(bytes);
				}
			}
			
			bytes = new byte[SIZE_NUM_MATS];
			number_of_materials = byteArrayToInt(bytes);
			for(int i = 0; i < number_of_materials; i++){
				bytes = new byte[SIZE_MAT];
				readMaterial(bytes);
			}
			
			bytes = new byte[SIZE_NUM_MESHES];
			number_of_meshes = byteArrayToInt(bytes);
			for(int i = 0; i < number_of_meshes; i++){
				boolean mesh_refs = (input.readByte()==1);
				bytes = new byte[SIZE_NUM_REFS];
				int num_refs = byteArrayToInt(bytes);
				
				bytes = new byte[(num_refs*SIZE_NUM_PRIMS) + SIZE_TRANSLATION + SIZE_ROTATION];
				input.read(bytes);
				
				readMesh(bytes,mesh_refs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void readMesh(byte[] bytes, boolean mesh_refs) {
		// TODO Auto-generated method stub
		
	}

	private void readMaterial(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	private Face readFace(byte[] bytes) {
		try{
			DataInputStream face_data = new DataInputStream(
				new ByteArrayInputStream(bytes)
			);
			
			Vector3f[] verts = new Vector3f[3];
			Vector3f[] vertnorms = new Vector3f[3];
			Vector3f norm = null;
			
			for(int i = 0; i < 3; i++){
				verts[i].x = face_data.readFloat();
				verts[i].y = face_data.readFloat();
				verts[i].z = face_data.readFloat();
			}
			
			return new Face(verts, vertnorms, norm);
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	private void readBlock(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	byte[] readBytes(int num) throws IOException{
		byte[] bytes = new byte[num];
		input.read(bytes);
		return bytes;
	}
	
	public static int byteArrayToInt(byte[] b) {
	    int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (b[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
}
