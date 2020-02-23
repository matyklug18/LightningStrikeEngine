package lightningstike.engine.data;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GMaterial {
    public GMaterial(IMaterial mat) {
        this.mat = mat;
        if(mat.getColor() instanceof IVoidColor)
            initShader(((IVoidColor)mat.getColor()).getColor());
    }
    public IMaterial mat;

    public int PID;

    private void initShader(Vector4f color) {
        String VF = "#version 460 core\n" +
                "in vec3 aPos;\n" +
                "in vec2 texCoord;\n" +
                "in vec3 normal;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 project;\n" +
                "uniform mat4 transform;\n" +
                "out vec2 outTexCoord;" +
                "out vec3 outNormal;\n" +
                "out vec3 fragPos;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   gl_Position = project * view * transform * vec4(aPos, 1.0);\n" +
                "   outTexCoord = texCoord;\n" +
                "   outNormal = mat3(transpose(inverse(transform))) * normal;\n" +
                "   fragPos = vec3(transform * vec4(aPos, 1.0));\n" +
                "}";
        String FF = "#version 460 core\n" +
                "in vec2 outTexCoord;\n" +
                "in vec3 outNormal;\n" +
                "in vec3 fragPos;\n" +
                "uniform sampler2D texture_sampler;\n" +
                "uniform vec3 lightPos;\n" +
                "uniform vec3 lightColor;\n" +
                "uniform vec3 viewPos;\n" +
                "out vec4 outColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   vec3 norm = normalize(outNormal);\n" +
                "   vec3 lightDir = normalize(lightPos - fragPos);\n" +
                "   float specularStrength = 0.5;\n" +
                "   float diff = max(dot(norm, lightDir), 0.0);\n" +
                "   vec3 diffuse = diff * lightColor;\n" +
                "   vec3 viewDir = normalize(viewPos - fragPos);\n" +
                "   vec3 reflectDir = reflect(-lightDir, outNormal);\n" +
                "   float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);\n" +
                "   vec3 specular = specularStrength * spec * lightColor;\n" +
                "   outColor = texture(texture_sampler, outTexCoord);\n" +
                "   outColor = vec4(outColor.x*"+color.x+",outColor.y*"+color.y+",outColor.z*"+color.z+",outColor.w*"+color.w+");\n" +
                "   vec3 result = (vec3(0.1) + diffuse + specular) * outColor.rgb;" +
                "   outColor = vec4(result, 1.);\n" +
                "} ";


        PID = GL20.glCreateProgram();

        int VID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(VID, VF);
        GL20.glCompileShader(VID);

        if (GL20.glGetShaderi(VID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(VID));
            return;
        }

        int FID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(FID, FF);
        GL20.glCompileShader(FID);

        if (GL20.glGetShaderi(FID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(FID));
            return;
        }

        GL20.glAttachShader(PID, VID);
        GL20.glAttachShader(PID, FID);

        GL20.glLinkProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(PID));
            return;
        }

        GL20.glValidateProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(PID));
            return;
        }
    }
}