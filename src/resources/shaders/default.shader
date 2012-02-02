<shader>
	<fragment>
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

layout(std140) uniform Lights {
	Light light;
};

uniform Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
} material;

//uniform Material material;
*/

varying in vec3 vertex_mod;
varying in vec3 normal_mod;
varying in vec4 color_mod;
vec4 color;

void main(){
	//if the object is transparent then don't bother rendering
	if(color_mod.a == 0.0) {
  		discard;
  	}

	//vec3 light_pos = normalize(gl_LightSource[0].position.xyz - vertex_mod);   
	//vec3 eye_pos = normalize(-vertex_mod); // we are in Eye Coordinates, so EyePos is (0,0,0)  
	//vec3 light_reflection = normalize(-reflect(light_pos,normal_mod));  
/*
	//calculate Ambient Term:  
	vec4 ambient = material.ambient;    
	
	//calculate Diffuse Term:  
	vec4 diffuse = material.diffuse * max(dot(normal,light_pos), 0.0);
	diffuse = clamp(diffuse, 0.0, 1.0);     
	   
	// calculate Specular Term:
	vec4 specular = material.specular * pow(max(dot(light_reflection,eye_pos),0.0),0.3);
	specular = clamp(specular, 0.0, 1.0);
 */
 /*
	//calculate Ambient Term:  
	vec4 ambient = gl_FrontLightProduct[0].ambient;    
	
	//calculate Diffuse Term:  
	vec4 diffuse = gl_FrontLightProduct[0].diffuse * max(dot(normal,light_pos), 0.0);
	diffuse = clamp(diffuse, 0.0, 1.0);     
	   
	// calculate Specular Term:
	vec4 specular = gl_FrontLightProduct[0].specular * pow(max(dot(light_reflection,eye_pos),0.0),0.3);
	specular = clamp(specular, 0.0, 1.0); 
*/

//	TESTING SOME SHIT //
	//calculate Ambient Term:  
	//vec4 ambient = color;    
	
	//calculate Diffuse Term:  
	//vec4 diffuse = color * gl_LightSource[0].diffuse * max(dot(normal_mod,light_pos), 0.0);
	//diffuse = clamp(diffuse, 0.0, 1.0);     
	   
	// calculate Specular Term:
	//vec4 specular = color * gl_LightSource[0].specular * pow(max(dot(light_reflection,eye_pos),0.0),1.0);
	//specular = clamp(specular, 0.0, 1.0); 
//  END TEST //
	// write Total Color:  

	//gl_FragColor = gl_FrontLightModelProduct.sceneColor + color + ambient + diffuse + specular;
	//gl_FragColor = ambient + diffuse + specular;
	  
  	color = color_mod;
  	
  	//multiply through by diffuse to set the dark portions of the object
  	//gl_FragColor = gl_Color * color * diffuse;
  	//set alpha back to its original value or you will have the alpha channel set to the diffuse value
  	gl_FragColor.a = color.a;
}
	</fragment>
	<vertex>

	uniform mat4 transform;
	uniform vec4 scale;
	
	varying out vec4 color;
	varying out vec3 normal;
	varying out vec3 vertex;
	
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
		vertex_cast.x = gl_Vertex.x * scale.x;
		vertex_cast.y = gl_Vertex.y * scale.y;
		vertex_cast.z = gl_Vertex.z * scale.z;
		vertex_cast.w = 1.0;
		
		//Calculate vertex position
		gl_Position = gl_ModelViewProjectionMatrix * transform * vertex_cast;
		vertex = vec3(gl_Position);
		
		// Calculate the normal value for this vertex, in world coordinates
	    normal = normalize(gl_NormalMatrix * gl_Normal);
	
	    // Set the front color to the color passed through with glColor
	    //gl_FrontColor = gl_Color;
		
		color = gl_Color;
	}
	</vertex>
</shader>
