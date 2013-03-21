###FRAG###
	#version 330

	uniform sampler2D texture_data;

	in vec2 tex_coord;
	out vec4 frag_color;
	
	void main(){
		frag_color = texture2D(texture_data,tex_coord);
	}
###ENDFRAG###
###VERT###
	#version 330
	
	uniform mat4 transform;
	
	uniform TransformationMatrices { 
		mat4 MVP;
	};
	
	in vec3 vertex;
	in vec3 normal;
	in vec2 texture;
	//in vec4 color;
	uniform vec4 scale;

	out vec2 tex_coord;
	
	void main() {
		vec4 vertex_cast;
		vertex_cast.x = vertex.x * scale.x;
		vertex_cast.y = vertex.y * scale.y;
		vertex_cast.z = vertex.z * scale.z;
		vertex_cast.w = 1.0 * scale.w;
		
		vertex_cast = transform * vertex_cast;
	
		//Calculate vertex position
		gl_Position = MVP * vertex_cast;

		tex_coord = texture;
	
	    // Set the front color to the color passed through with glColor
		//color_mod = color;
	}
###ENDVERT###