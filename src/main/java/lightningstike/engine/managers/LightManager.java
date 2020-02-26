package lightningstike.engine.managers;

import lightningstike.engine.data.ILight;

import java.util.ArrayList;

//TODO
public class LightManager {
    private static ArrayList<ILight> lights;

    public static void add(ILight light) {
        lights.add(light);
    }
}
