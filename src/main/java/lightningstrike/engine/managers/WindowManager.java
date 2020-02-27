package lightningstrike.engine.managers;

import lightningstrike.engine.io.Window;
import lightningstrike.engine.util.Function;

import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;

public class WindowManager {
    private static ArrayList<Window> windows = new ArrayList<>();
    private static ArrayList<Function> renderers = new ArrayList<>();

    public static void add(Window win, Function render) {
        windows.add(win);
        InputManager.init(win.winID);
        renderers.add(render);
    }

    public static void update() {
        for(int i = 0; i < windows.size(); i++) {
            windows.get(i).update(renderers.get(i));
        }
    }

    public static boolean shouldClose() {
        boolean flag = true;
        for(Window win:windows) {
            if (!win.shouldClose()) {
                flag = false;
            }
        }
        return flag;
    }

    public static ArrayList<Window> getWindows() {
        return windows;
    }

    public static void end() {
        for(Window wnd:getWindows()) {
            glfwFreeCallbacks(wnd.winID);
            glfwDestroyWindow(wnd.winID);
        }
    }
}
