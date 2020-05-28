#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform float transparency;

void main(void){
    
    vec4 textureColor = texture(guiTexture, textureCoords);

    textureColor.a = textureColor.a - transparency;

    out_Color = textureColor;

}