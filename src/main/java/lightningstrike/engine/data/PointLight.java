package lightningstrike.engine.data;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PointLight implements ILight {

    public PointLight(Vector3f pos, Color color) {
        this.pos = pos;
        this.color = color;
    }
    public Vector3f pos;
    public IColor color;
}
