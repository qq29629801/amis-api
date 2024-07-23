package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.Plugins;
import com.yuyaogc.lowcode.engine.util.ClassUtils;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

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

    public void doInstall(String fileName, String basePackage, Container container, Application application, Context context) throws IOException {
        JarFile jarFile = new JarFile(  fileName);

        AppClassLoader jarLauncher = new AppClassLoader(jarFile);

        List<Class<?>> classList = ClassUtils.scanPackage(  basePackage, jarLauncher);

        application.setClassLoader(jarLauncher);

        ClassUtils.buildApp(container, application, classList);


        for (EntityClass entityClass1 : application.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, container);
        }



        application.autoTableInit(context.getConfig());


        ClassUtils.loadSeedData(context, jarLauncher, application);


        application.onEvent();
    }


    public abstract void build(String fileName, String basePackage, Container container, Application application);


    public abstract void install(String fileName, String basePackage, Container container, Application application, Context context);
}
