package lightningstike.engine.data;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GMaterial {
    public GMaterial(Vector4f color) {
        this.albedo = color;
        initShader(color);
    }
    public Vector4f albedo;

    public int PID;

    private void initShader(Vector4f color) {
        String VF = "#version 460 core\n" +
                "in vec3 aPos;\n" +
                "in vec2 texCoord;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 project;\n" +
                "uniform mat4 transform;\n" +
                "out vec2 outTexCoord;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   gl_Position = project * view * transform * vec4(aPos, 1.0);\n" +
                "   outTexCoord = texCoord;" +
                "}";
        String FF = "#version 460 core\n" +
                "in  vec2 outTexCoord;\n" +
                "uniform sampler2D texture_sampler;\n" +
                "out vec4 outColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
//                "   outColor = vec4(outTexCoord, 0, 1);\n" +
                "   outColor = texture(texture_sampler, outTexCoord);\n" +
//                "   outColor = vec4("+color.x+","+color.y+","+color.z+","+color.w+");\n" +
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
