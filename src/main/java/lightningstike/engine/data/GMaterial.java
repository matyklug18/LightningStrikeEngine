package lightningstike.engine.data;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class GMaterial {
    public GMaterial(Vector4f color) {
        this.albedo = color;
    }
    public Vector4f albedo;
}
