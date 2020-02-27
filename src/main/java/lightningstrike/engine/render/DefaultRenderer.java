package lightningstrike.engine.render;

import lightningstrike.engine.data.*;
import lightningstrike.engine.managers.WindowManager;
import lightningstrike.engine.util.MatrixUtils;
import lightningstrike.engine.util.ObjectsManager;
import lightningstrike.engine.util.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;

public class DefaultRenderer {

    private static ISelectable<GObject> obj = new ISelectable<GObject>() {
        GObject obj;

        @Override
        public GObject getSelectedObject() {
            return obj;
        }

        @Override
        public void setSelectedObject(GObject obj) {
            this.obj = obj;
        }
    };

    public static int MAX_POINT_LIGHTS = 5;
    public static int MAX_SPOT_LIGHTS = 5;
    public static int MAX_DIR_LIGHTS = 5;
    public static ArrayList<DirLight> dirLights = new ArrayList<>();
    public static ArrayList<SpotLight> spotLights = new ArrayList<>();
    public static ArrayList<PointLight> pointLights = new ArrayList<>();

    public static void loadUniforms() {
        if(MAX_POINT_LIGHTS != 0)
            loadPointLights();
        if(MAX_SPOT_LIGHTS != 0)
            loadSpotLights();
        if(MAX_DIR_LIGHTS != 0)
            loadDirLights();
    }

    private static void loadDirLights() {
        int count = Math.min(dirLights.size(), MAX_DIR_LIGHTS);
        setUniform("dirLightCount", count);
        int i = 0;
        for(DirLight light : dirLights) {
            setUniform("dirLights[" + i + "].dir", light.dir);
            setUniform("dirLights[" + i + "].color", ColorInstanceofManager.checkInstance(light.color).getColor());
            if(++i >= count)
                break;
        }
    }

    private static void loadSpotLights() {
        int count = Math.min(spotLights.size(), MAX_SPOT_LIGHTS);
        setUniform("spotLightCount", count);
        int i = 0;
        for(SpotLight light : spotLights) {
            setUniform("spotLights[" + i + "].pos", light.pos);
            setUniform("spotLights[" + i + "].dir", light.dir);
            setUniform("spotLights[" + i + "].color", ColorInstanceofManager.checkInstance(light.color).getColor());
            setUniform("spotLights[" + i + "].cutoff", light.cutoff);
            setUniform("spotLights[" + i + "].outerCutoff", light.outerCutoff);
            setUniform("spotLights[" + i + "].attenuation", light.attenuation);
            if(++i >= count)
                break;
        }
    }

    private static void loadPointLights() {
        int count = Math.min(pointLights.size(), MAX_POINT_LIGHTS);
        setUniform("pointLightCount", count);
        int i = 0;
        for(PointLight light : pointLights) {
            setUniform("pointLights[" + i + "].pos", light.pos);
            setUniform("pointLights[" + i + "].color", ColorInstanceofManager.checkInstance(light.color).getColor());
            if(++i >= count)
                break;
        }
    }

    private static void setUniform(String name, int intValue) {
        GL20.glUniform1i(checkUniform(obj.getSelectedObject().mat.PID, name), intValue);
    }

    private static void setUniform(String name, float floatValue) {
        GL20.glUniform1f(checkUniform(obj.getSelectedObject().mat.PID, name), floatValue);
    }

    private static int checkUniform(int PID, String name) {
        int loc = glGetUniformLocation(PID, name);
        if(loc == -1)   System.err.println(name);
        return loc;
    }

    public static Vector3f cam = new Vector3f(0, 5, 10);
    public static Vector3f camRot = new Vector3f(35, 0, 0);

    private static void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(obj.getSelectedObject().mat.PID, name), false, matrixB);
    }

    private static void setUniform(String name, Vector3f vector) {
        glUniform3fv(glGetUniformLocation(obj.getSelectedObject().mat.PID, name), new float[] {vector.x, vector.y, vector.z});
    }

    private static void setUniform(String name, Vector4f vector) {
        glUniform4fv(glGetUniformLocation(obj.getSelectedObject().mat.PID, name), new float[] {vector.x, vector.y, vector.z, vector.w});
    }

    static int tex;
    private static void renderObject() {
        GObject obj0 = obj.getSelectedObject();
        GL30.glBindVertexArray(obj0.vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, obj0.ibo);

        GL20.glUseProgram(obj0.mat.PID);

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        try {
            if(tex == GL_ZERO)
                tex = TextureLoader.loadTexture(obj0.mat.mat.getTexture().getPath());
            glBindTexture(GL_TEXTURE_2D, tex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUniform("transform", MatrixUtils.transformationMatrix(obj0.pos, obj0.rot, obj0.scale));
        setUniform("project", MatrixUtils.projectionMatrix(70, (float) WindowManager.getWindows().get(0).w/(float)WindowManager.getWindows().get(0).h, 0.1f, 100));
        setUniform("view", MatrixUtils.viewMatrix(cam, camRot));

        loadUniforms();

        glEnable(GL_FRAMEBUFFER_SRGB);
        GL11.glDrawElements(GL11.GL_TRIANGLES, obj0.indsCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glUseProgram(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public static void render() {
        for(int i = 0; i < ObjectsManager.getAmount(); i ++) {
            ObjectsManager.selectForRendering(i, () -> obj);
            renderObject();
        }
    }
}
