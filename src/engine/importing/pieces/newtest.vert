
void main() {
  mat4 transform = mat4(1.0,0.0,0.0,3.0,  0.0,1.0,0.0,0.0,  0.0,0.0,1.0,0.0,  0.0,0.0,0.0,1.0);
  gl_Position = (transform *gl_ModelViewProjectionMatrix) * gl_Vertex;
}