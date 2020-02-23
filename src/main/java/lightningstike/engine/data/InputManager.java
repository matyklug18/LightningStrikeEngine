package lightningstike.engine.data;

import lightningstike.engine.io.Window;
import lightningstike.engine.io.WindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;

public class InputManager {
    private static ArrayList<IKey> keys = new ArrayList<>();

    public static void init(long id) {
        GLFW.glfwSetKeyCallback(id, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                for (IKey ikey : keys) if (ikey.getKey() == key) ikey.call(action, mods);
            }
        });
    }

    public static void add(IKey key) {
        keys.add(key);
    }
}
