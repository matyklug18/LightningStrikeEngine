package lightningstike.engine.data;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class WMaterial {
    IMaterial mat;

    public WMaterial(GMaterial mat) {
        this.mat = mat.mat;
    }

    public Vector4f getColor() {
        if(mat.getColor() instanceof IVoidColor)
            return ((IVoidColor)mat.getColor()).getColor();
        else
            return new Vector4f(0f,0f,0f,0f);
    }
}
