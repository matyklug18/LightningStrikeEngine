import org.lwjgl.glfw.GLFW;

public class Main {
    public static void main(String[] args) {
        init();
        Runnable delta = () -> startDeltaUpdate();
        delta.run();
        Runnable fixed = () -> startFixedUpdate();
        fixed.run();
    }

    private static void init() {
    }

    public static long fixedTime = 2;

    private static void startDeltaUpdate() {
        double delta = 0.0;
        double lastLoopTime = GLFW.glfwGetTime();
        double lastFPSTime = 0.0;

        while(true) {
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

    private static void startFixedUpdate() {
        while(true) {
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
