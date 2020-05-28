#version 400

const int MAX_LIGHTS = 16;

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 textureCoords;
layout(location = 2) in vec3 normal;

out vec3 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toCameraVector;
out float visibility;

out vec3 toLightVector[MAX_LIGHTS];

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float useFakeLighting;

uniform float ignoreViewMatrix;

uniform vec3 lightPosition[MAX_LIGHTS];

uniform float numberOfRows;
uniform vec2 offset;

const float density = 0.001;
const float gradient = 0.7;

void main() {

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    
    if (ignoreViewMatrix > 0.8) {
        gl_Position = projectionMatrix * worldPosition;
    } else {
        gl_Position = projectionMatrix * positionRelativeToCam;
    }
    
    passTextureCoords = (textureCoords / numberOfRows) + vec3(offset,0.0);

    for(int i = 0; i < MAX_LIGHTS; i++){
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }

    vec3 actualNormal = normal;
    if(useFakeLighting > 0.5){
        actualNormal = vec3(0,1,0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0,0,0,1.0)).xyz 
                - worldPosition.xyz;

    float distanceFromCam = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distanceFromCam*density),gradient));
    visibility = clamp(visibility, 0.0, 1.0);

    if ( ignoreViewMatrix > 0.8 ) {
        visibility = 1.0;
    }

}
