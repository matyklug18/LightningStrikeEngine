#version 460 core
in vec2 outTexCoord;
in vec3 outNormal;
in vec3 fragPos;
uniform sampler2D texture_sampler;
uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 viewPos;
out vec4 outColor;

void main()
{
    vec3 norm = normalize(outNormal);
    vec3 lightDir = normalize(lightPos - fragPos);
    float specularStrength = 0.5;
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, outNormal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;
    outColor = texture(texture_sampler, outTexCoord);
    outColor = vec4(outColor.x*1.0,outColor.y*1.0,outColor.z*1.0,outColor.w*1.0);
    vec3 result = (vec3(0.1) + diffuse + specular) * outColor.rgb;   outColor = vec4(result, 1.);
}