package lightningstike.engine.managers;

import lightningstike.engine.data.Color;
import lightningstike.engine.data.IColor;
import lightningstike.engine.data.IMaterial;
import lightningstike.engine.data.ITexture;
import org.joml.Vector4f;

public class MaterialLibrary {
    public static IMaterial grape = new IMaterial() {
        @Override
        public IColor getColor() {
            return new Color(new Vector4f(1,1,1,1));
        }

        @Override
        public ITexture getTexture() {
            return () -> "grape.jpg";
        }

        @Override
        public float getRoughness() {
            return 0;
        }

        @Override
        public float getEmission() {
            return 0;
        }

        @Override
        public float getMetallic() {
            return 0;
        }

        @Override
        public String getName() {
            return "grape";
        }
    };
}
