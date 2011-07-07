//uniform mat4x4 transform;

void main() {
	mat4 transform = mat4(1.0,0.0,0.0,0.0,  0.0,1.0,0.0,0.0,  0.0,0.0,1.0,0.0,  0.0,0.0,0.0,1.0);
	gl_Position = (transform * gl_ModelViewProjectionMatrix) * gl_Vertex;
	//gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}