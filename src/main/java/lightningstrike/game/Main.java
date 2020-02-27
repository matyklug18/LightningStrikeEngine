package lightningstrike.game;

import lightningstrike.engine.Engine;
import lightningstrike.engine.data.*;
import lightningstrike.engine.io.Window;
import lightningstrike.engine.managers.*;
import lightningstrike.engine.render.DefaultRenderer;
import lightningstrike.engine.util.ObjectsManager;
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
        ObjectsManager.add("test.obj", new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(10,1,10), "default");

        LightManager.add(new PointLight(new Vector3f(-5, 2, 0), new Color(new Vector4f(1,0,0,1))));
        LightManager.add(new SpotLight(new Vector3f(0, 2, 0), new Color(new Vector4f(1, 1, 1, 1)), new Vector3f(1, 0, 0), 0.1f, 0.05f, new Vector3f(0, -1, 0)));

        //start the engine
        Engine.start((delta) -> {
            ObjectsManager.get(0).getRot().add(new Vector3f(0, new Double(delta).floatValue(),0));
        }, () -> {

        });
    }
}
