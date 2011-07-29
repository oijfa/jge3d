uniform mat4 transform;
uniform vec4 scale;

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
	vec4 vertex;
	vertex.x = gl_Vertex.x * scale.x;
	vertex.y = gl_Vertex.y * scale.y;
	vertex.z = gl_Vertex.z * scale.z;
	vertex.w = 1.0;
	
	//Calculate vertex position
	gl_Position = gl_ModelViewProjectionMatrix * transform * vertex;
	vertex = vec4(gl_ModelViewMatrix * transform * vertex);
	gl_Position = gl_Position;
	
	// Calculate the normal value for this vertex, in world coordinates
    normal = normalize(gl_NormalMatrix * gl_Normal);

    // Set the front color to the color passed through with glColor
    //gl_FrontColor = gl_Color;
	
	color = gl_Color;
}