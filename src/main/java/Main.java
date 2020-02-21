import de.javagl.obj.Obj;
import io.Window;
import io.WindowManager;
import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import util.MatrixUtils;
import util.OBJLoader;

import java.io.IOException;
import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Main {

    public static void main(String[] args) {
        init();
        Thread delta = new Thread(() -> startDeltaUpdate(), "deltaUpdate");
        delta.start();
        Thread fixed = new Thread(() -> startFixedUpdate(), "fixedUpdate");
        fixed.start();
        startRenderUpdate();
        end();
    }

    private static void end() {
    }

    private static int vao, pbo, ibo;
    private static int indsCount;

    private static int PID;

    private static void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < 1; i++)
            WindowManager.add(new Window(new Vector4f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat())).init(), Main::renderObjects);

        glEnable(GL_DEPTH_TEST);

        try {

            Obj obj =  OBJLoader.load("test.obj");

            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);

            FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(obj.getNumVertices() * 3);
            float[] positionData = new float[obj.getNumVertices()* 3];
            for (int i = 0; i < obj.getNumVertices(); i++) {
                positionData[i * 3] = obj.getVertex(i).getX();
                positionData[i * 3 + 1] = obj.getVertex(i).getY();
                positionData[i * 3 + 2] = obj.getVertex(i).getZ();
            }
            positionBuffer.put(positionData).flip();

            pbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            ArrayList<Integer> inds = new ArrayList<>();

            for (int i = 0; i < obj.getNumFaces(); i++) {
                for (int j = 0; j < obj.getFace(i).getNumVertices(); j++) {
                    inds.add(obj.getFace(i).getVertexIndex(j));
                    indsCount++;
                }
            }

            IntBuffer indsBuffer = MemoryUtil.memAllocInt(indsCount);
            indsBuffer.put(inds.stream().mapToInt(i -> i).toArray());
            indsBuffer.flip();

            ibo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indsBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                "    outColor = vec4(0, 1, 0, 1);\n" +
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

    private static void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(PID, name), false, matrixB);
    }

    public static long fixedTime = 2;

    private static void startDeltaUpdate() {
        double delta = 0.0;
        double lastLoopTime = GLFW.glfwGetTime();
        double lastFPSTime = 0.0;

        while(!WindowManager.shouldClose()) {
            double now = GLFW.glfwGetTime();
            double updateLength = now - lastLoopTime;

            lastLoopTime = now;

            delta = updateLength;
            lastFPSTime += updateLength;

            if(lastFPSTime >= 1) {
                lastFPSTime = 0;
            }

            updateDelta(delta);
        }
    }

    private static void updateDelta(double delta) {

    }

    private static void startRenderUpdate() {
        while(!WindowManager.shouldClose()) {
            render();
        }
    }

    private static void renderObjects() {
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL20.glUseProgram(PID);

        setUniform("transform", MatrixUtils.transformationMatrix(new Vector3f(0,0,-2.5f), new Vector3f(0,0,0), new Vector3f(1,1,1)));
        setUniform("project", MatrixUtils.projectionMatrix(70, (float)WindowManager.getWindows().get(0).w/(float)WindowManager.getWindows().get(0).h, 0.1f, 10));
        setUniform("view", MatrixUtils.viewMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0)));

        GL11.glDrawElements(GL11.GL_TRIANGLES, indsCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glUseProgram(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private static void render() {
        WindowManager.update();
    }

    private static void startFixedUpdate() {
        while(!WindowManager.shouldClose()) {
            double frameStart = GLFW.glfwGetTime();
            updateFixed();
            double timeElapsed = GLFW.glfwGetTime() - frameStart;

            try {
                long wait = (long) ((fixedTime - timeElapsed));
                if(wait > 0) {
                    Thread.sleep(wait);
                }
            } catch (InterruptedException ex) {}

        }
    }

    private static void updateFixed() {

    }
}
