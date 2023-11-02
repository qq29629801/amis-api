package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.loader.AppClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;

public class ConfigUtils {

    public Properties getApplicationProperties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();

        if (classLoader instanceof AppClassLoader) {
            AppClassLoader appClassLoader = (AppClassLoader) classLoader;
            Enumeration<JarEntry> entries = appClassLoader.jarFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".properties")) {
                    InputStream inputStream = appClassLoader.getResourceAsStream(name);
                    try {
                        properties.load(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return properties;
        } else {
            InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream("application.properties");
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
