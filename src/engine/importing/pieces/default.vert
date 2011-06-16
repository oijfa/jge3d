uniform vec3 transform;

void main()
{
	gl_Position = transform * gl_Vertex;
}