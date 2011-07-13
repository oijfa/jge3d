uniform mat4 transform;

void main() {
	mat4 identity = mat4( 
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(0.0,0.0,0.0,1.0)
	);
	//gl_Position = gl_ModelViewProjectionMatrix * identity * transform *  gl_Vertex;
	gl_Position = gl_ModelViewProjectionMatrix * identity * gl_Vertex;
}