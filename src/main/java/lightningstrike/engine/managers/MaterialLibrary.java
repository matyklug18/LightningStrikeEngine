package lightningstrike.engine.managers;

import lightningstrike.engine.data.Color;
import lightningstrike.engine.data.IColor;
import lightningstrike.engine.data.IMaterial;
import lightningstrike.engine.data.ITexture;
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
        public float getSpecularHighlight() {
            return 0;
        }

        @Override
        public float getSpecularStrength() {
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
