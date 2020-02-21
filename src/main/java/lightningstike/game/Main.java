package lightningstike.game;

import lightningstike.engine.Engine;
import lightningstike.engine.data.GMaterial;
import lightningstike.engine.io.Window;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.render.DefaultRenderer;
import lightningstike.engine.util.ObjectsManager;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        WindowManager.add(new Window(new Vector4f(0,0,0,0)).init(), DefaultRenderer::render);
        ObjectsManager.add("test.obj", new Vector3f(0,0,-50f), new Vector3f(0,0,0), new Vector3f(1,1,1), new GMaterial(new Vector4f(1,1,1,1)));
        Engine.start((delta) -> { }, () -> { });
    }
}
