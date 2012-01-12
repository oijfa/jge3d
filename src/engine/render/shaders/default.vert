#version 140

uniform mat4 transform;
uniform vec4 scale;
/*
struct Light {
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float constantAtt;
    float linearAtt;
    float quadraticAtt;
    vec3 spotDirection;
    float spotExponent;
    float spotCutoff;
};

uniform Lights {
	Light light;
};

struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
};

uniform Material material;
*/

in vec3 vertex;
in vec3 normal;
in vec2 texture;
in vec4 color;

in mat4 projection;

out vec3 vertex_mod;
out vec3 normal_mod;
out vec4 color_mod;

void main() {
	/*
	mat4 identity = mat4(
		vec4(1.0,0.0,0.0,0.0),
		vec4(0.0,1.0,0.0,0.0),
		vec4(0.0,0.0,1.0,0.0),
		vec4(p.x,p.y,p.z,1.0)
	);
	*/
	vec4 vertex_cast;
	vertex_cast.x = vertex.x * scale.x;
	vertex_cast.y = vertex.y * scale.y;
	vertex_cast.z = vertex.z * scale.z;
	vertex_cast.w = 1.0;
	
	vertex_mod = vec3(vertex_cast);

	//Calculate vertex position
	//gl_Position = gl_ModelViewProjectionMatrix * transform * vertex_cast;
	gl_Position = projection * transform * vertex_cast;
	
	// Calculate the normal value for this vertex, in world coordinates
    //normal_mod = normalize(gl_NormalMatrix * normal);
    normal_mod = normal;

    // Set the front color to the color passed through with glColor
    //gl_FrontColor = gl_Color;
	
	color_mod = color;
}