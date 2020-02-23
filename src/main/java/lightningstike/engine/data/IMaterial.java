package lightningstike.engine.data;

public interface IMaterial {
    IColor getColor();
    ITexture getTexture();
    float getRoughness();
    float getEmission();
    float getMetallic();
    String getName();
}