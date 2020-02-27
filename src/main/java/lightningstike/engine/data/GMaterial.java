package lightningstike.engine.data;

import lightningstike.engine.managers.LightManager;
import lightningstike.engine.util.StringLoader;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GMaterial {

    public GMaterial(IMaterial mat) {
        this.mat = mat;
        initShader(ColorInstanceofManager.checkInstance(mat.getColor()).getColor());
    }
    public IMaterial mat;

    public int PID;

    private void initShader(Vector4f color) {
        String VF = StringLoader.loadResourceAsString("vert.glsl");
        String FF = StringLoader.loadResourceAsString("frag.glsl");

        FF = FF.replace("%maxpointlights%", Integer.toString(LightManager.MAX_POINT_LIGHTS))
                .replace("%maxdirlights%", Integer.toString(LightManager.MAX_DIR_LIGHTS))
                .replace("%maxarealights%", Integer.toString(LightManager.MAX_AREA_LIGHTS))
                .replace("%maxspotlights%", Integer.toString(LightManager.MAX_SPOT_LIGHTS));

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