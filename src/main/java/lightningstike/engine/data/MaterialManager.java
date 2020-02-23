package lightningstike.engine.data;

import java.util.ArrayList;

public class MaterialManager {
    private static ArrayList<GMaterial> mats = new ArrayList<>();

    public static void add(IMaterial mat) {
        mats.add(new GMaterial(mat));
    }

    public static GMaterial getMaterial(String id) {
        for(GMaterial mat:mats)
            if(mat.mat.getName().equals(id))
                return mat;
        return mats.get(0);
    }
}
