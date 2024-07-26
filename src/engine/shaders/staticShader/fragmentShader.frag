#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec4 positionColor;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(void) {
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.0);
	vec3 diffuse = max(0.5, brightness) * lightColor;
	
	float di = 0.01;
	// if(pass_textureCoords.x < di || pass_textureCoords.x > (1 - di) || pass_textureCoords.y < di || pass_textureCoords.y > (1 - di) || pass_textureCoords.x == pass_textureCoords.y) {
	// 	out_Color = vec4(0.0, 0.0, 1.0, 1.0);
	// } else {
		float positionColorRatio = 0.25;
		out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
		out_Color = out_Color * (1.0 - positionColorRatio) * positionColor * positionColorRatio;
	// }
}