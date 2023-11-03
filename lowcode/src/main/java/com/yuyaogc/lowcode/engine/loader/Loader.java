package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.Plugins;

import java.util.List;

public abstract class Loader {

    private static Loader loader;

    public static Loader getLoader() {
        return loader;
    }

    public static void setLoader(Loader loader1) {
        loader = loader1;
    }


    public void loadPlugin(Plugins plugins) {
        List<IPlugin> pluginList = plugins.getPluginList();
        if (pluginList == null) {
            return;
        }

        for (IPlugin plugin : plugins.getPluginList()) {
            try {
                if (plugin.start() == false) {
                    String message = "Plugin start error: " + plugin.getClass().getName();
                    throw new RuntimeException(message);
                }
            } catch (Exception e) {
                String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
                throw new RuntimeException(message, e);
            }

        }
    }


    public abstract void build(String fileName, String basePackage, Container container, Application application);
}
