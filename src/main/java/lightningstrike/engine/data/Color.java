package lightningstrike.engine.data;

import org.joml.Vector4f;

public class Color implements IVoidColor {
    Vector4f col;

    public Color(Vector4f col) {
        this.col = col;
    }

    @Override
    public Vector4f getColor() {
        return col;
    }
}
