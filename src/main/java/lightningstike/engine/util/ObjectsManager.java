package lightningstike.engine.util;

import lightningstike.engine.data.GLData;
import lightningstike.engine.data.GMaterial;
import lightningstike.engine.data.GObject;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.ArrayList;

public class ObjectsManager {
    private static ArrayList<GObject> objs = new ArrayList<>();
    public static void add(String model, Vector3f pos, Vector3f rot, Vector3f scale, GMaterial material) {
        objs.add(initObject(model, pos, rot, scale, material));
    }

    private static GObject initObject(String objModelName, Vector3f pos, Vector3f rot, Vector3f scale, GMaterial material) {

        GObject object = new GObject();
        try {
            object.model = OBJLoader.load(objModelName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        object.pos = pos;
        object.rot = rot;
        object.scale = scale;
        GMaterial mat = material;
        object.mat = mat;

        return object;
    }

    public static int getAmount() {
        return objs.size();
    }

    public static GObject get(int index) {
        return objs.get(index);
    }
}
