#version 400

const int MAX_LIGHTS = 16;

in vec3 passTextureCoords;

in vec3 surfaceNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;

uniform vec3 lightColor[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform vec3 skyColor;

uniform float textureTileSize;
uniform float shineDamper;
uniform float reflectivity;

uniform float glow;

void main() {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    vec2 scaledTextureCoords = vec2(passTextureCoords) * textureTileSize;

    for(int i = 0; i < MAX_LIGHTS; i++){
        float distanceToLight = length(toLightVector[i]);
        float attenuationFactor = attenuation[i].x + 
            (attenuation[i].y * distanceToLight) + 
            (attenuation[i].z * distanceToLight * distanceToLight);

        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDot1 = dot(unitNormal, unitLightVector);
        float brightness = max(nDot1, 0.0);
        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) 
            / attenuationFactor;
        totalDiffuse += (brightness * lightColor[i]) / attenuationFactor;
    }
    totalDiffuse = max(totalDiffuse, 0.1);

    if(glow > 0.8){
        totalDiffuse = vec3(10,10,10);
    }

    vec4 textureColor = texture(textureSampler, scaledTextureCoords);

    if(textureColor.a < 0.5){
        discard;
    }

    out_Color = vec4(totalDiffuse,1.0) * textureColor + vec4(totalSpecular, 1.0);
    out_Color = mix(vec4(skyColor, 1.0),  out_Color, visibility);

}
