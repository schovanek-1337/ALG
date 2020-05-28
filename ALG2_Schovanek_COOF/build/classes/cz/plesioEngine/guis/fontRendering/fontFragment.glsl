#version 330

in vec2 pass_textureCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0;
const float borderEdge = 0.1;

const vec3 outlineColor = vec3(0.0, 0.0, 0.0);

void main(void){

    float characterDistance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
    float alpha = 1.0 - smoothstep(width, width + edge, characterDistance);

    float edgeDistance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
    
    float edgeAlpha = 
        1.0 - smoothstep(borderWidth, borderWidth + borderEdge, edgeDistance);

    float overallAlpha = alpha + (1.0 - alpha) * edgeAlpha;

    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

    out_Color = vec4(overallColor, overallAlpha);

}