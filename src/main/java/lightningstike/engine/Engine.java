package lightningstike.engine;

import de.javagl.obj.Obj;
import lightningstike.engine.data.GLData;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.util.Function;
import lightningstike.engine.util.FunctionDouble;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import lightningstike.engine.util.OBJLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Engine {

    private static FunctionDouble deltaFunc;
    private static Function fixedFunc;
    public static void start(FunctionDouble deltaF, Function fixedF) {
        init();
        deltaFunc = deltaF;
        fixedFunc = fixedF;
        Thread delta = new Thread(() -> startDeltaUpdate(), "deltaUpdate");
        delta.start();
        Thread fixed = new Thread(() -> startFixedUpdate(), "fixedUpdate");
        fixed.start();
        startRenderUpdate();
        end();
    }

    private static void end() {
    }

    private static void init() {

        initMesh("test.obj");
        initShader();
    }

    private static void initMesh(String objName) {
        try {
            Obj obj =  OBJLoader.load(objName);

            GLData.vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(GLData.vao);

            FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(obj.getNumVertices() * 3);
            float[] positionData = new float[obj.getNumVertices()* 3];
            for (int i = 0; i < obj.getNumVertices(); i++) {
                positionData[i * 3] = obj.getVertex(i).getX();
                positionData[i * 3 + 1] = obj.getVertex(i).getY();
                positionData[i * 3 + 2] = obj.getVertex(i).getZ();
            }
            positionBuffer.put(positionData).flip();

            GLData.pbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLData.pbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            ArrayList<Integer> inds = new ArrayList<>();

            for (int i = 0; i < obj.getNumFaces(); i++) {
                for (int j = 0; j < obj.getFace(i).getNumVertices(); j++) {
                    inds.add(obj.getFace(i).getVertexIndex(j));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initShader() {
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
        deltaFunc.run(delta);
    }

    private static void startRenderUpdate() {
        while(!WindowManager.shouldClose()) {
            render();
        }
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
        fixedFunc.run();
    }
}
