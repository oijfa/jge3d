uniform mat4 transform;

void main() {
	/*
	// uniform matrices from JBullet are stored in column major order //
	mat4 identity = mat4( 
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(  X,  Y,  Z,1.0)
	);
	*/
	//gl_Position = gl_ModelViewProjectionMatrix * identity * transform *  gl_Vertex;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}