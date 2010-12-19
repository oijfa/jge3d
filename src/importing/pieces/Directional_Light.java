package importing.pieces;

import javax.vecmath.Vector3f;

public class Directional_Light {
	//for all of these: x = red, y = green, z = blue 
	Vector3f direction;
	Vector3f diffuse;
	Vector3f specular;
	
	Directional_Light( Vector3f dir, Vector3f dif, Vector3f spec)
	{
		direction = dir;
		diffuse =  dif;
		specular = spec;
	}
}
