uniform vec4 color;

void main(){
  // Setting Each Pixel To Red
  //gl_FragColor.rbga = vec4(color.x, color.y, color.z, 1.0);
  gl_FrontColor = gl_Color;
}