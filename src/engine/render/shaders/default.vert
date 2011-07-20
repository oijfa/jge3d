uniform mat4 transform;
varying vec4 color;

void main() {
	/*
	mat4 identity = mat4(
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(0.0,4.0,0.0,1.0)
	);
	*/
	gl_Position = gl_ModelViewProjectionMatrix * transform * gl_Vertex;
	//gl_Position = gl_ModelViewProjectionMatrix * identity * gl_Vertex;
	color = gl_Color;
}