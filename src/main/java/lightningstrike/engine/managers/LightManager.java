package lightningstrike.engine.managers;

import lightningstrike.engine.data.*;
import lightningstrike.engine.render.DefaultRenderer;

import java.util.ArrayList;

//TODO
public class LightManager {
    private static ArrayList<ILight> lights = new ArrayList<>();

    public static void add(ILight light) {
        lights.add(light);
        PointLight plight = LightInstanceofManager.getPointLight(light);
        SpotLight slight = LightInstanceofManager.getSpotLight(light);
        DirLight dlight = LightInstanceofManager.getDirLight(light);
        if(plight != null) {
            DefaultRenderer.pointLights.add(plight);
        } else if(slight != null) {
            DefaultRenderer.spotLights.add(slight);
        } else if(dlight != null) {
            DefaultRenderer.dirLights.add(dlight);
        }
    }

    public static ILight getLight(int id) {
        return lights.get(id);
    }

    public static ArrayList<ILight> getLights() {
        return lights;
    }
}
