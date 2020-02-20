package util;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class OBJLoader {
    public static Obj load(String path) throws IOException {
        InputStream objInputStream = new FileInputStream(Paths.get("src/main/resources/" + path).toFile());
        return ObjReader.read(objInputStream);
    }
}
