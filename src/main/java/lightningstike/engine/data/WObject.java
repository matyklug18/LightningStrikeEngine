package lightningstike.engine.data;

import org.joml.Vector3f;

public class WObject {
    private GObject obj;

    public WObject(GObject obj) {
        this.obj = obj;
    }

    public Vector3f getPos() {
        return obj.pos;
    }

    public Vector3f getRot() {
        return obj.rot;
    }

    public Vector3f getScale() {
        return obj.scale;
    }

    public WMaterial getMat() {
        return new WMaterial(obj.mat);
    }
}
