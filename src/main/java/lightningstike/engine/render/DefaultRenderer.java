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

    private static void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(SelectedData.obj.mat.PID, name), false, matrixB);
    }

    private static void renderObject() {
        GL30.glBindVertexArray(SelectedData.obj.vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, SelectedData.obj.ibo);

        GL20.glUseProgram(SelectedData.obj.mat.PID);

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        try {
            glBindTexture(GL_TEXTURE_2D, TextureLoader.loadTexture("test.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUniform("transform", MatrixUtils.transformationMatrix(SelectedData.obj.pos, SelectedData.obj.rot, SelectedData.obj.scale));
        setUniform("project", MatrixUtils.projectionMatrix(70, (float) WindowManager.getWindows().get(0).w/(float)WindowManager.getWindows().get(0).h, 0.1f, 100));
        setUniform("view", MatrixUtils.viewMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0)));

        GL11.glDrawElements(GL11.GL_TRIANGLES, SelectedData.obj.indsCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glUseProgram(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public static void render() {
        for(int i = 0; i < ObjectsManager.getAmount(); i ++) {
            ObjectsManager.get(i).selectForRendering();
            renderObject();
        }
    }
}
