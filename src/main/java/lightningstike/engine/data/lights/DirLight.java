package lightningstike.engine.data.lights;

import org.joml.Vector3f;

public class DirLight implements ILight {
    public Vector3f direction;
    public Vector3f color;

    public DirLight(Vector3f direction, Vector3f color) {
        this.direction = direction;
        this.color = color;
    }
}
