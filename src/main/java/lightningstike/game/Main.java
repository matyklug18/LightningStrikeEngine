package lightningstike.game;

import lightningstike.engine.Engine;
import lightningstike.engine.data.*;
import lightningstike.engine.data.lights.DirLight;
import lightningstike.engine.data.lights.PointLight;
import lightningstike.engine.data.lights.SpotLight;
import lightningstike.engine.io.Window;
import lightningstike.engine.managers.*;
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
        WindowManager.add(new Window(new Vector4f(0,0,0,0), new Vector2i(1280, 720)).init(), DefaultRenderer::render);

        //add some lights
        LightManager.MAX_POINT_LIGHTS = 3;
        LightManager.MAX_AREA_LIGHTS = 0;
        LightManager.MAX_DIR_LIGHTS = 0;
        LightManager.MAX_SPOT_LIGHTS = 0;

        LightManager.add(new PointLight(new Vector3f(-5,0,0), new Vector3f(1f,0.5f,0), new Vector3f(1f,0,0)));
        LightManager.add(new PointLight(new Vector3f(0,0,0), new Vector3f(0.25f,1f,1), new Vector3f(1f,0,0)));
        LightManager.add(new PointLight(new Vector3f(5,0,0), new Vector3f(1,0.25f,0.25f), new Vector3f(1f,0,0)));

        LightManager.add(new SpotLight(new Vector3f(0, -8f, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0), 0.1f, 0.05f, new Vector3f(0, -1, 0)));

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
            ObjectsManager.add("test.obj", new Vector3f(0, -20, 0), new Vector3f(0,0,0), new Vector3f(10,10,10), "default");

        //start the engine
        Engine.start((delta) -> { }, () -> {
            //ObjectsManager.get(0).getRot().add(0, 0.1f, 0);
        });
    }
}
