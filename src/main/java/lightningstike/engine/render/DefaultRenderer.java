package lightningstike.engine.render;

import de.javagl.obj.Obj;
import lightningstike.engine.data.GLData;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.util.MatrixUtils;
import lightningstike.engine.util.ObjectsManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class DefaultRenderer {

    private static void setUniform(String name, Matrix4f matrix, int PID) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(PID, name), false, matrixB);
    }

    private static void renderObjects(int vao, int ibo, int PID, int indsCount, Vector3f pos, Vector3f rot, Vector3f scale) {
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL20.glUseProgram(PID);

        setUniform("transform", MatrixUtils.transformationMatrix(pos, rot, scale), PID);
        setUniform("project", MatrixUtils.projectionMatrix(70, (float) WindowManager.getWindows().get(0).w/(float)WindowManager.getWindows().get(0).h, 0.1f, 100), PID);
        setUniform("view", MatrixUtils.viewMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0)), PID);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indsCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glUseProgram(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public static void render() {
        for(int i = 0; i < ObjectsManager.getAmount(); i ++) {
            ObjectsManager.get(i).selectForRendering();
            renderObjects(GLData.vao, GLData.ibo, GLData.PID, GLData.indsCount, GLData.obj.pos, GLData.obj.rot, GLData.obj.scale);
        }
    }
}
