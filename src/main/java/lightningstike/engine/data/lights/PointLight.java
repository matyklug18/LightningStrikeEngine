package lightningstike.engine.data.lights;

import org.joml.Vector3f;

public class PointLight implements ILight {
    public Vector3f position;
    public Vector3f color;
    public Vector3f attenuation;

    public PointLight(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public static final PointLight NULL = new PointLight(
            new Vector3f(0, 0, 0),
            new Vector3f(1, 1, 1),
            new Vector3f(1, 0, 0)
    );

}
