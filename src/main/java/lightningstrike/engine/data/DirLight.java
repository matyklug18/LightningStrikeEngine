package lightningstrike.engine.data;

import org.joml.Vector3f;

public class DirLight implements ILight {
    public Vector3f dir;
    public IColor color;

    public DirLight(Vector3f direction, IColor color) {
        this.dir = direction;
        this.color = color;
    }
}
