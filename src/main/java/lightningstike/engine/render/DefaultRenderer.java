package lightningstike.engine.render;

import lightningstike.engine.data.GObject;
import lightningstike.engine.data.ISelectable;
import lightningstike.engine.data.ISelectedRenderer;
import lightningstike.engine.data.ReturnFunction;
import lightningstike.engine.managers.WindowManager;
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

        setUniform("lightPos", new Vector3f(0,0,-3));
        setUniform("lightColor", new Vector3f(1,1,1));
        setUniform("viewPos", cam);

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
