/*uniform mat4 transform;*/

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}