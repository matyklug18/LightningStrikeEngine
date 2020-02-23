package lightningstike.engine.render;

import lightningstike.engine.data.SelectedData;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.util.MatrixUtils;
import lightningstike.engine.util.ObjectsManager;
import lightningstike.engine.util.TextureLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class DefaultRenderer {

    public static Vector3f cam = new Vector3f(0, 5, 10);
    public static Vector3f camRot = new Vector3f(35, 0, 0);

    private static void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(SelectedData.obj.mat.PID, name), false, matrixB);
    }

    private static void setUniform(String name, Vector3f vector) {
        glUniform3fv(glGetUniformLocation(SelectedData.obj.mat.PID, name), new float[] {vector.x, vector.y, vector.z});
    }

    private static void renderObject() {
        GL30.glBindVertexArray(SelectedData.obj.vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, SelectedData.obj.ibo);

        GL20.glUseProgram(SelectedData.obj.mat.PID);

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        try {
            int tex = TextureLoader.loadTexture(SelectedData.obj.mat.mat.getTexture().getPath());
            glBindTexture(GL_TEXTURE_2D, tex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cam.add(new Vector3f(0.001f, 0, 0));

        setUniform("transform", MatrixUtils.transformationMatrix(SelectedData.obj.pos, SelectedData.obj.rot, SelectedData.obj.scale));
        setUniform("project", MatrixUtils.projectionMatrix(70, (float) WindowManager.getWindows().get(0).w/(float)WindowManager.getWindows().get(0).h, 0.1f, 100));
        setUniform("view", MatrixUtils.viewMatrix(cam, camRot));

        setUniform("lightPos", new Vector3f(0,0,-3));
        setUniform("lightColor", new Vector3f(1,1,1));
        setUniform("viewPos", cam);

        GL11.glDrawElements(GL11.GL_TRIANGLES, SelectedData.obj.indsCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glUseProgram(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public static void render() {
        for(int i = 0; i < ObjectsManager.getAmount(); i ++) {
            ObjectsManager.selectForRendering(i);
            renderObject();
        }
    }
}
