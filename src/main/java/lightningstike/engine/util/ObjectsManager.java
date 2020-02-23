package lightningstike.engine.util;

import lightningstike.engine.data.GMaterial;
import lightningstike.engine.data.GObject;
import lightningstike.engine.data.MaterialManager;
import lightningstike.engine.data.WObject;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ObjectsManager {
    private static ArrayList<GObject> objs = new ArrayList<>();

    public static void add(String model, Vector3f pos, Vector3f rot, Vector3f scale, String materialID) {
        objs.add(initObject(model, pos, rot, scale, MaterialManager.getMaterial(materialID)));
    }

    private static GObject initObject(String objModelName, Vector3f pos, Vector3f rot, Vector3f scale, GMaterial material) {
        GObject object = new GObject(objModelName, pos, rot, scale, material);
        return object;
    }

    public static int getAmount() {
        return objs.size();
    }

    public static WObject get(int index) {
        return new WObject(objs.get(index));
    }

    public static void selectForRendering(int id) {
        objs.get(id).selectForRendering();
    }
}
