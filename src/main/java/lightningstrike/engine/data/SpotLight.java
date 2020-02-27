package lightningstrike.engine.data;

import org.joml.Vector3f;

public class SpotLight implements ILight {
    public float cutoff, outerCutoff;
    public Vector3f dir;
    public Vector3f pos;
    public Vector3f attenuation;
    public IColor color;

    public SpotLight(Vector3f pos, IColor color, Vector3f attenuation, float cutoff, float outerCutoff, Vector3f dir) {
        this.cutoff = cutoff;
        this.outerCutoff = outerCutoff;
        this.dir = dir;
        this.color = color;
        this.pos = pos;
        this.attenuation = attenuation;
    }
}
