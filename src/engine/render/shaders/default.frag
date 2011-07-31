varying vec4 color;
varying vec3 vertex;
varying vec3 normal;

void main(){
	//if the object is transparent then don't bother rendering
	if(color.a == 0.0) {
  		discard;
  	}
  	
	vec3 light_pos = normalize(gl_LightSource[0].position.xyz - vertex);   
	vec3 eye_pos = normalize(-vertex); // we are in Eye Coordinates, so EyePos is (0,0,0)  
	vec3 light_reflection = normalize(-reflect(light_pos,normal));  
 
	//calculate Ambient Term:  
	vec4 ambient = gl_FrontLightProduct[0].ambient;    
	
	//calculate Diffuse Term:  
	vec4 diffuse = gl_FrontLightProduct[0].diffuse * max(dot(normal,light_pos), 0.0);
	diffuse = clamp(diffuse, 0.0, 1.0);     
	   
	// calculate Specular Term:
	vec4 specular = gl_FrontLightProduct[0].specular * pow(max(dot(light_reflection,eye_pos),0.0),0.3*gl_FrontMaterial.shininess);
	specular = clamp(specular, 0.0, 1.0); 
	
	// write Total Color:  
	//gl_FragColor = gl_FrontLightModelProduct.sceneColor  + ambient + diffuse + specular;
	gl_FragColor = color + ambient + diffuse + specular;  
  	
  	//multiply through by diffuse to set the dark portions of the object
  	//gl_FragColor = gl_Color * color * diffuse;
  	//set alpha back to its original value or you will have the alpha channel set to the diffuse value
  	gl_FragColor.a = color.a;
}