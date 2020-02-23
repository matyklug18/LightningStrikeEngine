package lightningstike.engine;

import lightningstike.engine.data.*;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.util.Function;
import lightningstike.engine.util.FunctionDouble;
import org.joml.Vector4f;
import org.lwjgl.glfw.*;

public class Engine {

    private static FunctionDouble deltaFunc;
    private static Function fixedFunc;

    public static void init() {
        MaterialManager.add(new IMaterial() {
            @Override
            public IColor getColor() {
                return new Color(new Vector4f(1,1,1,1));
            }

            @Override
            public ITexture getTexture() {
                return () -> "test.png";
            }

            @Override
            public float getRoughness() {
                return 0;
            }

            @Override
            public float getEmission() {
                return 0;
            }

            @Override
            public float getMetallic() {
                return 0;
            }

            @Override
            public String getName() {
                return "default";
            }
        });
    }

    public static void start(FunctionDouble deltaF, Function fixedF) {
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
