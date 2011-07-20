varying vec4 color;

void main(){
  // Setting Each Pixel To Red
  //gl_FragColor.rbga = vec4(1, 1, 0, 1.0);
  gl_FragColor = color;
}