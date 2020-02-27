package lightningstrike.engine.data;

import org.joml.Vector4f;

public class ColorInstanceofManager {
    public static IVoidColor checkInstance(IColor color) {
        if(color instanceof IVoidColor)
            return ((IVoidColor)color);
        return () -> new Vector4f(0,0,0,0);
    }
}
