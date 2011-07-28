varying vec4 color;
varying float diffuse;

void main(){
	//if the object is transparent then don't bother rendering
	if(color.a == 0.0) {
  		discard;
  	}
  	
  	//multiply through by diffuse to set the dark portions of the object
  	gl_FragColor = gl_Color * color * diffuse;
  	//set alpha back to its original value or you will have the alpha channel set to the diffuse value
  	gl_FragColor.a = color.a;
}