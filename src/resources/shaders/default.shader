<shader>
	<fragment>
		#version 330

		struct light_type {
			vec4 position;
			vec4 ambient;
			vec4 diffuse;
			vec4 specular;
			float constant_attenuation;
		    float linear_attenuation;
		    float quadratic_attenuation;
		    vec3 spot_direction;
		    float spot_cutoff;
		    float spot_exponent;
		};

		uniform lights {
			light_type light[1];
		};
/*
		uniform material {
			vec4 ambient;
			vec4 diffuse;
			vec4 specular;
			float shininess;
		} material;
*/
		smooth in vec3 vertex_mod;
		smooth in vec3 normal_mod;
		in vec4 color_mod;
		
		out vec4 frag_color;
		
		int i = 0;
		
		void main(){
			if(color_mod.a == 0.0) {
		  		discard;
		  	}

		    float nDotVP;       // normal * light direction
		    float nDotR;        // normal * light reflection vector
		    float pf;           // power factor
		    float attenuation;  // computed attenuation factor
		    float d;            // distance from surface to light position
		    vec3 VP;            // direction from surface to light position
		    vec3 reflection;    // direction of maximum highlights
		
		    // Compute vector from surface to light position
		    VP = vec3 (light[i].position) - vertex_mod;
		
		    // Compute distance between surface and light position
		    d = length(VP);
		
		    // Normalize the vector from surface to light position
		    VP = normalize(VP);
		
		    // Compute attenuation
		    attenuation = 1.0f / (light[i].constant_attenuation +
		                          light[i].linear_attenuation * d +
		                          light[i].quadratic_attenuation * d * d);
		
		    reflection = normalize(reflect(-normalize(VP), normalize(normal_mod)));
		
		    nDotVP = max (0.0f, dot (normal_mod, VP));
		    nDotR = max (0.0f, dot(normalize(normal_mod), reflection));
		
		    if (nDotVP == 0.0f) {
		        pf = 0.0f;
		    }
		    else {
		        //pf = pow(nDotR, material.shininess);
		        pf = pow(nDotR, 0.8);
		    }
			
			/*
		    vec4 ambient = material.ambient * light[i].ambient * attenuation;
		    vec4 diffuse = material.diffuse * light[i].diffuse * nDotVP * attenuation;
		    vec4 specular = material.specular * light[i].specular * pf * attenuation;
		    */
		    
		    vec4 ambient = color_mod * light[i].ambient * attenuation;
		    vec4 diffuse = color_mod * light[i].diffuse * nDotVP * attenuation;
		    vec4 specular = color_mod * light[i].specular * pf * attenuation;

		  	frag_color = ambient + diffuse + specular;
		  	frag_color.a = color_mod.a;
		}
	</fragment>
	<vertex>
		#version 330
		
		uniform mat4 transform;
		
		uniform TransformationMatrices { 
			mat4 MVP;
		};
		
		in vec3 vertex;
		in vec3 normal;
		in vec2 texture;
		in vec4 color;
		in vec3 scale;
		smooth out vec3 vertex_mod;
		smooth out vec3 normal_mod;
		out vec4 color_mod;
		
		void main() {
			vec4 vertex_cast;
			vertex_cast.x = vertex.x * scale.x;
			vertex_cast.y = vertex.y * scale.y;
			vertex_cast.z = vertex.z * scale.z;
			vertex_cast.w = 1.0;
			
			vertex_cast = transform * vertex_cast;
		
			vertex_mod = vec3(vertex_cast);
		
			//Calculate vertex position
			gl_Position = MVP * vertex_cast;
		
			// Calculate the normal value for this vertex, in world coordinates
		    normal_mod = normal;
		
		    // Set the front color to the color passed through with glColor
			color_mod = color;
		}
	</vertex>
</shader>
