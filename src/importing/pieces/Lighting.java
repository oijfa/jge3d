package importing.pieces;

import javax.vecmath.Vector3f;

public class Lighting {
	private Vector3f ambient;
	private Vector3f dlight;
	
	public Lighting(Vector3f amb, Vector3f dl) {
		ambient = amb;
		dlight = dl;
	}
	
	public Vector3f getAmbientLight() {
		return ambient;
	}
	
	public Vector3f getDirectionLight() {
		return dlight;
	}
}
