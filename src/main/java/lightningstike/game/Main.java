package lightningstike.game;

import lightningstike.engine.Engine;
import lightningstike.engine.io.Window;
import lightningstike.engine.io.WindowManager;
import lightningstike.engine.render.DefaultRenderer;
import org.joml.Vector4f;

public class Main {
    public static void main(String[] args) {
        WindowManager.add(new Window(new Vector4f(0,0,0,0)).init(), DefaultRenderer::render);
        Engine.start((delta) -> { }, () -> { });
    }
}
