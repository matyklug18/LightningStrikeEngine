package lightningstike.engine.data;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class WMaterial {
    IMaterial mat;

    public WMaterial(GMaterial mat) {
        this.mat = mat.mat;
    }

    public Vector4f getColor() {
        return ColorInstanceofManager.checkInstance(mat.getColor()).getColor();
    }
}
