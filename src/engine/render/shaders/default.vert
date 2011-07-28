uniform mat4 transform;
varying vec4 color;
varying vec3 normal;
varying vec3 vertex;

void main() {
	/*
	mat4 identity = mat4(
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(p.x,p.y,p.z,1.0)
	);
	*/
	
	//Calculate vertex position
	gl_Position = gl_ModelViewProjectionMatrix * transform * gl_Vertex;
	vertex = vec3(gl_ModelViewMatrix * transform * gl_Vertex);
	
	// Calculate the normal value for this vertex, in world coordinates
    normal = normalize(gl_NormalMatrix * gl_Normal);

    // Set the front color to the color passed through with glColor
    //gl_FrontColor = gl_Color;
	
	color = gl_Color;
}