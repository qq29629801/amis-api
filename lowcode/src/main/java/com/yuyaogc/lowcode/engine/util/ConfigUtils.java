package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;

public class ConfigUtils {

    public static Object getApplication(Class<?> clazz) {
        AppClassLoader appClassLoader = (AppClassLoader) clazz.getClassLoader();
        Enumeration<JarEntry> entries = appClassLoader.jarFile.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(".yml")) {
                InputStream is = appClassLoader.getResourceAsStream(name);
                Yaml yaml = new Yaml();
                return yaml.load(is);
            }
        }
        return null;
    }
}
