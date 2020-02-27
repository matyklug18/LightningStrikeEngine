package lightningstrike.engine.data;

public interface IMaterial {
    IColor getColor();
    ITexture getTexture();
    float getSpecularHighlight();
    float getSpecularStrength();
    float getEmission();
    float getMetallic();
    String getName();
}