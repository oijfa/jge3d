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
		    int light_index;
		    int num_lights;
		};
		
		uniform alights {
			light_type light[8];
		} lights;
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
		
		vec4 phongPass(int light_id) {		
		    float nDotVP;       // normal * light direction
		    float nDotR;        // normal * light reflection vector
		    float pf;           // power factor
		    float attenuation;  // computed attenuation factor
		    float d;            // distance from surface to light position
		    vec3 VP;            // direction from surface to light position
		    vec3 reflection;    // direction of maximum highlights
		    vec4 frag;			// sum of all frag operations
		
		    // Compute vector from surface to light position
		    VP = vec3 (lights.light[light_id].position) - vertex_mod;
		
		    // Compute distance between surface and light position
		    d = length(VP);
		
		    // Normalize the vector from surface to light position
		    VP = normalize(VP);
		
		    // Compute attenuation
		    attenuation = 1.0f / (lights.light[light_id].constant_attenuation +
		                          lights.light[light_id].linear_attenuation * d +
		                          lights.light[light_id].quadratic_attenuation * d * d);
		
		    reflection = normalize(reflect(-normalize(VP), normalize(normal_mod)));
		
		    nDotVP = max (0.0f, dot (normal_mod, VP));
		    nDotR = max (0.0f, dot(normalize(normal_mod), reflection));
		
		    if (nDotVP == 0.0f) {
		        pf = 0.0f;
		    }
		    else {
		        //pf = pow(nDotR, material.shininess);
		        pf = pow(nDotR, 1.0f);
		    }
			
			/*
		    vec4 ambient = material.ambient * lights.light[light_id].ambient * attenuation;
		    vec4 diffuse = material.diffuse * lights.light[light_id].diffuse * nDotVP * attenuation;
		    vec4 specular = material.specular * lights.light[light_id].specular * pf * attenuation;
		    */
		    
		    vec4 ambient = color_mod * lights.light[light_id].ambient * attenuation;
		    vec4 diffuse = color_mod * lights.light[light_id].diffuse * nDotVP * attenuation;
		    vec4 specular = color_mod * lights.light[light_id].specular * pf * attenuation;

		  	frag = ambient + diffuse + specular;
		  	frag.a = color_mod.a;
		  	
		  	return frag;
		}
		
		void main(){
			if(color_mod.a == 0.0) {
		  		discard;
		  	}
		  	frag_color = vec4(0,0,0,0);
			for(int i=0;i != 2;i++) {
				frag_color += phongPass(i);
			}
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
		uniform vec4 scale;
		smooth out vec3 vertex_mod;
		smooth out vec3 normal_mod;
		out vec4 color_mod;
		
		void main() {
			vec4 vertex_cast;
			vertex_cast.x = vertex.x * scale.x;
			vertex_cast.y = vertex.y * scale.y;
			vertex_cast.z = vertex.z * scale.z;
			vertex_cast.w = 1.0 * scale.w;
			
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
