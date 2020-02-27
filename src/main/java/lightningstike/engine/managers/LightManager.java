package lightningstike.engine.managers;

import lightningstike.engine.data.lights.AreaLight;
import lightningstike.engine.data.lights.DirLight;
import lightningstike.engine.data.lights.PointLight;
import lightningstike.engine.data.lights.SpotLight;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3fv;

//TODO
public class LightManager {

    public static int MAX_POINT_LIGHTS = 3;
    public static int MAX_SPOT_LIGHTS = 0;
    public static int MAX_DIR_LIGHTS = 1;
    public static int MAX_AREA_LIGHTS = 0;

    private static LinkedList<PointLight> pointLights = new LinkedList<>();
    private static LinkedList<SpotLight> spotLights = new LinkedList<>();
    private static LinkedList<DirLight> dirLights = new LinkedList<>();
    private static LinkedList<AreaLight> areaLights = new LinkedList<>();

    public static void add(PointLight light) {
        if(light instanceof SpotLight)  spotLights.add((SpotLight) light);
        else pointLights.add(light);
    }
    public static void add(SpotLight light) {
        spotLights.add(light);
    }
    public static void add(DirLight light) {
        dirLights.add(light);
    }
    public static void add(AreaLight light) {
        areaLights.add(light);
    }

    public static void loadUniforms(int PID) {
        if(MAX_POINT_LIGHTS != 0)   loadPointLights(PID);
        if(MAX_SPOT_LIGHTS != 0)   loadSpotLights(PID);
        if(MAX_DIR_LIGHTS != 0)   loadDirLights(PID);
        if(MAX_AREA_LIGHTS != 0)   loadAreaLights(PID);
    }

    private static void loadAreaLights(int PID) {

    }

    private static void loadDirLights(int PID) {
        int count = Math.min(dirLights.size(), MAX_DIR_LIGHTS);
        setUniform(PID, "dirLightCount", count);
        int i = 0;
        for(DirLight light : dirLights) {
            setUniform(PID, "dirLights[" + i + "].dir", light.direction);
            setUniform(PID, "dirLights[" + i + "].color", light.color);
            if(++i >= count)    break;
        }
    }

    private static void loadSpotLights(int PID) {
        int count = Math.min(spotLights.size(), MAX_SPOT_LIGHTS);
        setUniform(PID, "spotLightCount", count);
        int i = 0;
        for(SpotLight light : spotLights) {
            setUniform(PID, "spotLights[" + i + "].pos", light.position);
            setUniform(PID, "spotLights[" + i + "].dir", light.direction);
            setUniform(PID, "spotLights[" + i + "].color", light.color);
            setUniform(PID, "spotLights[" + i + "].cutoff", light.cutoff);
            setUniform(PID, "spotLights[" + i + "].outerCutoff", light.outerCutoff);
            setUniform(PID, "spotLights[" + i + "].attenuation", light.attenuation);
            if(++i >= count)    break;
        }
    }

    private static void loadPointLights(int PID) {
        int count = Math.min(pointLights.size(), MAX_POINT_LIGHTS);
        setUniform(PID, "pointLightCount", count);
        int i = 0;
        for(PointLight light : pointLights) {
            setUniform(PID, "pointLights[" + i + "].pos", light.position);
            setUniform(PID, "pointLights[" + i + "].color", light.color);
            if(++i >= count)    break;
        }
    }

    private static void setUniform(int PID, String name, Vector3f vector) {
        GL20.glUniform3f(checkUniform(PID, name), vector.x, vector.y, vector.z);
    }

    private static void setUniform(int PID, String name, int intValue) {
        GL20.glUniform1i(checkUniform(PID, name), intValue);
    }

    private static void setUniform(int PID, String name, float floatValue) {
        GL20.glUniform1f(checkUniform(PID, name), floatValue);
    }

    private static int checkUniform(int PID, String name) {
        int loc = glGetUniformLocation(PID, name);
        if(loc == -1)   System.err.println(name);
        return loc;
    }

}
