package lightningstike.engine.data.lights;

import org.joml.Vector3f;

public class SpotLight extends PointLight {
    public float cutoff, outerCutoff;
    public Vector3f direction;

    public SpotLight(Vector3f position, Vector3f color, Vector3f attenuation, float cutoff, float outerCutoff, Vector3f direction) {
        super(position, color, attenuation);
        this.cutoff = cutoff;
        this.outerCutoff = outerCutoff;
        this.direction = direction;
    }
}
