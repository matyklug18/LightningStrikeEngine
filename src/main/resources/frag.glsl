#version 460 core

#define MAX_POINT_LIGHTS %maxpointlights%
#define MAX_DIR_LIGHTS %maxdirlights%
#define MAX_SPOT_LIGHTS %maxspotlights%

in vec2 outTexCoord;
in vec3 outNormal;
in vec3 fragPos;

uniform sampler2D texture_sampler;

uniform vec3 viewPos;

out vec4 outColor;

const float specularStrength = 10;
const float ambientStrength = 0.1;
const float materialShininess = 1024;

//Light Structs
#if MAX_POINT_LIGHTS != 0
#define __POINT_LIGHTS__

struct PointLight {
    vec3 pos;
    vec4 color;
    vec3 attenuation;
};

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform int pointLightCount;

vec4 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.pos - fragPos);

    vec4 diffuse = max(dot(normal, lightDir), 0.0) * light.color;

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    vec4 specular = specularStrength * spec * light.color;

    vec4 texColor = texture(texture_sampler, outTexCoord);
    return texColor * (vec4(ambientStrength) + diffuse + specular);
}

    #endif

    #if MAX_DIR_LIGHTS != 0
    #define __DIR_LIGHTS__

struct DirLight {
    vec3 dir;
    vec4 color;
};

uniform DirLight dirLights[MAX_DIR_LIGHTS];
uniform int dirLightCount;

vec4 calcDirLight(DirLight light, vec3 normal, vec3 viewDir) {
    vec3 lightDir = normalize(-light.dir);

    vec4 diffuse = max(dot(normal, lightDir), 0.0) * light.color;

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), materialShininess);
    vec4 specular = specularStrength * spec * light.color;

    vec4 texColor = texture(texture_sampler, outTexCoord);
    return texColor * (vec4(ambientStrength) + diffuse + specular);
}

    #endif

    #if MAX_SPOT_LIGHTS != 0

    #define __SPOT_LIGHTS__

struct SpotLight {
    vec3 pos;
    vec3 dir;
    vec4 color;
    vec3 attenuation;
    float cutoff;
    float outerCutoff;
};

uniform int spotLightCount;
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

vec4 calcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.pos - fragPos);

    float theta = dot(lightDir, normalize(-light.dir));
    float epsilon = light.cutoff - light.outerCutoff;
    float intensity = clamp((theta - light.outerCutoff) / epsilon, 0.0, 1.0);

    vec4 diffuse = max(dot(normal, lightDir), 0.0) * light.color;

    vec3 halfwayDir = normalize(lightDir + viewDir);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), materialShininess);
    vec4 specular = light.color * spec;

    vec4 texColor = texture(texture_sampler, outTexCoord);
    return texColor * (vec4(ambientStrength) + (diffuse + specular) * intensity);
}

    #endif

void main()
{
    vec4 outRGBA = vec4(0.0);

    vec3 norm = normalize(outNormal);
    vec3 viewDir = normalize(viewPos - fragPos);

    #ifdef __POINT_LIGHTS__
    for(int i = 0; i < pointLightCount; i++) {
        outRGBA += calcPointLight(pointLights[i], norm, fragPos, viewDir);
    }
     #endif

     #ifdef __DIR_LIGHTS__
    for(int i = 0; i < dirLightCount; i++) {
        outRGBA += calcDirLight(dirLights[i], norm, viewDir);
    }
    #endif

    #ifdef __SPOT_LIGHTS__
    for(int i = 0; i < spotLightCount; i++) {
        outRGBA += calcSpotLight(spotLights[i], norm, fragPos, viewDir);
    }
    #endif

    outRGBA *= 0.4;

    outColor = vec4(outRGBA);
}