Notes

VAOs & VBOs
	A VBO is some number data.
	A VAO has multiple VBOS.
	They have IDs which is how you access & modify them.

Shaders
	The CPU handles logic, calculations, gameplay, etc.
	The GPU actually draws it
	A shader takes what it is given and draws it
	It might change the color, scale, texture, or position
	You make these shaders (pipelines) to turn math into visuals
	You have total control over the display
	Written in GLSL (c style)
	
	Vertex Shader
		Runs for each vertex
		Given VAO model data (might be pos, color, lighting, uv coord, etc.)
		Must determine where it goes on screen
		Determines other values for each vertex for the fragment shader
		For example, convert a position to a color, the frag gets the color
		
	Fragment Shader
		Takes the Vertex Shader values of each vertex to determine the values for each pixel
		It always outputs an RGB value