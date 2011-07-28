varying vec4 color;
varying float diffuse;

void main(){
  // Setting Each Pixel To Red
  //gl_FragColor.rbga = vec4(1, 1, 0, 1.0);
  if(color.a == 0.0) {
  	discard;
  }
  //gl_FragColor = color;   // Set the output color of our current pixel
  gl_FragColor = gl_Color * diffuse;
}