package lightningstrike.engine.data;

public class LightInstanceofManager {
    public static PointLight getPointLight(ILight light) {
        if(light instanceof PointLight)
            return (PointLight)light;
        return null;
    }

    public static SpotLight getSpotLight(ILight light) {
        if(light instanceof SpotLight)
            return (SpotLight)light;
        return null;
    }

    public static DirLight getDirLight(ILight light) {
        if(light instanceof DirLight)
            return (DirLight)light;
        return null;
    }
}
