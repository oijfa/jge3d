uniform mat4 transform;
varying vec4 color;
varying float diffuse;

void main() {
	/*
	mat4 identity = mat4(
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(p.x,p.y,p.z,1.0)
	);
	*/
	 // Calculate the normal value for this vertex, in world coordinates
    vec3 vertex_normal = normalize(gl_NormalMatrix * gl_Normal);
    
    // Calculate the light position for this vertex
    vec3 vertex_light_position = gl_LightSource[0].position.xyz;
    
    // Set the diffuse value (darkness). 
    //This is done with a dot product between the normal and the light
    diffuse = max(dot(vertex_normal, vertex_light_position), 0.0);

    // Set the front color to the color passed through with glColor
    gl_FrontColor = gl_Color;
    
	gl_Position = gl_ModelViewProjectionMatrix * transform * gl_Vertex;
	color = gl_Color;
}