import io.Window;
import io.WindowManager;
import org.joml.Random;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.glfwInit;

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

    private static void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < 2; i++)
            WindowManager.add(new Window(new Vector4f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat())).init(), Main::renderObjects);
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
