package lightningstike.engine.util;

import lightningstike.engine.data.GMaterial;
import lightningstike.engine.data.GObject;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;

public class ObjectsManager {
    private static ArrayList<GObject> objs = new ArrayList<>();

    public static void add(String model, Vector3f pos, Vector3f rot, Vector3f scale, GMaterial material) {
        objs.add(initObject(model, pos, rot, scale, material));
    }

    private static GObject initObject(String objModelName, Vector3f pos, Vector3f rot, Vector3f scale, GMaterial material) {

        GObject object = new GObject(objModelName, pos, rot, scale, material);

        return object;
    }

    public static int getAmount() {
        return objs.size();
    }

    public static GObject get(int index) {
        return objs.get(index);
    }
}
