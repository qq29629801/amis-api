package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.container.Graph;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.Plugins;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

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





    public List<String> jarUrlList(Context context){
        List<Model> modules =   context.get("base.base_module").search(Criteria.equal("state", 0), 0, 0, null);
        List<Long> moduleIds = modules.stream().map(model -> model.getLong("id")).collect(Collectors.toList());
        List<Model> depends =  context.get("base.base_depends").search(Criteria.in("baseApp", (Object) moduleIds), 0, 0, null);

        Graph graph = new Graph();

        for(Model module: modules){
            List<Model> dependList =    depends.stream().filter(d->module.getLong("id").equals(d.getLong("baseApp"))).collect(Collectors.toList());
            for(Model depend: dependList){
                Optional<Model> optionalModel = modules.stream().filter(c -> c.getStr("appName").equals(depend.getStr("name"))).findFirst();
                if(optionalModel.isPresent()){
                    Model deptModule = optionalModel.get();
                    graph.addEdge(module.getLong("id"), deptModule.getLong("id"));
                }
            }
        }

        List<String> jarUrlList = new ArrayList<>();
        for(Long id: graph.topologicalSort()){
            Optional<Model> optionalModel = modules.stream().filter(c -> c.getLong("id").equals(id)).findFirst();
            if(optionalModel.isPresent()){
                jarUrlList.add(optionalModel.get().getStr("jarUrl"));
            }
        }

        System.out.println(graph.topologicalSort());

        return jarUrlList;
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
