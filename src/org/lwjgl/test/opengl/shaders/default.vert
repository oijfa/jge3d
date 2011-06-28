uniform mat4 transform;

void main() {
	gl_Position = transform * gl_Vertex;
}