package lightningstike.game;

import lightningstike.engine.Engine;
import lightningstike.engine.data.*;
import lightningstike.engine.io.Window;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.render.DefaultRenderer;
import lightningstike.engine.util.ObjectsManager;
import org.joml.Random;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class Main {
    public static void main(String[] args) {
        //add a new window
        WindowManager.add(new Window(new Vector4f(0,0,0,0), new Vector2i(300, 300)).init(), DefaultRenderer::render);
        //init the engine
        Engine.init();
        //add a new material
        MaterialManager.add(MaterialLibrary.grape);
        //add a new keybind
        InputManager.add(new IKey() {
            @Override
            public int getKey() {
                return GLFW.GLFW_KEY_O;
            }

            @Override
            public void call(int action, int mods) {
            }
        });
        //add a new object
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < 20; i ++)
            ObjectsManager.add("test.obj", new Vector3f(rnd.nextInt(10)-5, rnd.nextInt(10)-5, rnd.nextInt(10)-5), new Vector3f(0,0,0), new Vector3f(1,1,1), "default");

        //start the engine
        Engine.start((delta) -> { }, () -> {
            //ObjectsManager.get(0).getRot().add(0, 0.1f, 0);
        });
    }
}
