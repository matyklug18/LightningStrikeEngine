package lightningstike.engine.data;

import de.javagl.obj.Obj;
import lightningstike.engine.util.ObjectsManager;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class GObject {
    public Vector3f pos, rot, scale;
    public GMaterial mat;
    public Obj model;

    public void selectForRendering() {
        initMesh(this);
        initShader(this.mat.albedo);
        GLData.obj = this;
    }

    private static void initMesh(GObject obj) {
        GLData.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(GLData.vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(obj.model.getNumVertices() * 3);
        float[] positionData = new float[obj.model.getNumVertices()* 3];
        for (int i = 0; i < obj.model.getNumVertices(); i++) {
            positionData[i * 3] = obj.model.getVertex(i).getX();
            positionData[i * 3 + 1] = obj.model.getVertex(i).getY();
            positionData[i * 3 + 2] = obj.model.getVertex(i).getZ();
        }
        positionBuffer.put(positionData).flip();

        GLData.pbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLData.pbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        ArrayList<Integer> inds = new ArrayList<>();

        for (int i = 0; i < obj.model.getNumFaces(); i++) {
            for (int j = 0; j < obj.model.getFace(i).getNumVertices(); j++) {
                inds.add(obj.model.getFace(i).getVertexIndex(j));
                GLData.indsCount++;
            }
        }

        IntBuffer indsBuffer = MemoryUtil.memAllocInt(GLData.indsCount);
        indsBuffer.put(inds.stream().mapToInt(i -> i).toArray());
        indsBuffer.flip();

        GLData.ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GLData.ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indsBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private static void initShader(Vector4f color) {
        String VF = "#version 460 core\n" +
                "in vec3 aPos;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 project;\n" +
                "uniform mat4 transform;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = project * view * transform * vec4(aPos, 1.0);\n" +
                "}";
        String FF = "#version 460 core\n" +
                "out vec4 outColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    outColor = vec4("+color.x+","+color.y+","+color.z+","+color.w+");\n" +
                "} ";


        GLData.PID = GL20.glCreateProgram();

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

        GL20.glAttachShader(GLData.PID, VID);
        GL20.glAttachShader(GLData.PID, FID);

        GL20.glLinkProgram(GLData.PID);
        if (GL20.glGetProgrami(GLData.PID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(GLData.PID));
            return;
        }

        GL20.glValidateProgram(GLData.PID);
        if (GL20.glGetProgrami(GLData.PID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(GLData.PID));
            return;
        }
    }
}
