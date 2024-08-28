package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.container.Graph;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.Plugins;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import redis.clients.jedis.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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




    public List<String> getALLJarList(List<Model> modules,List<Model> dependApps){
        Graph graph = new Graph();
        for(Model module: modules){
            List<Model> dependList =    dependApps.stream().filter(d->module.getLong("id").equals(d.getLong("baseApp"))).collect(Collectors.toList());
            graph.addEdge(module.getLong("id"), 0L);
            addDepends(dependList, modules, graph, module);
        }
        List<String> jarList = new ArrayList<>();
        if(graph.hasCycle()){
            throw new EngineException(String.format("Has Cycle: %s",graph ));
        }

        for(Long id: graph.topologicalSort()){
            Optional<Model> optionalModel = modules.stream().filter(c -> c.getLong("id").equals(id)).findFirst();
            if(optionalModel.isPresent()){
                jarList.add(optionalModel.get().getStr("jarUrl"));
            }
        }
        return jarList;
    }


    public List<String> getModuleAlls(Context context){
        List<Model> installs =   context.get("base.base_module").search(Criteria.equal("state", 0), 0, 0, null);
        List<Long> installIds = installs.stream().map(model -> model.getLong("id")).collect(Collectors.toList());
        List<Model> depends =  context.get("base.base_depends").search(Criteria.in("baseApp", (Object) installIds), 0, 0, null);
        return getALLJarList(installs, depends);
    }



    public void addDepends(List<Model> dependList, List<Model> modules, Graph graph, Model module){
        for(Model depend: dependList){
            Optional<Model> optionalModel = modules.stream().filter(c -> c.getStr("appName").equals(depend.getStr("name"))).findFirst();
            if(optionalModel.isPresent()){
                Model deptModule = optionalModel.get();
                graph.addEdge(module.getLong("id"), deptModule.getLong("id"));
            }
        }
    }

    public APP getAppInfo(String fileName, String basePackage) throws IOException {
        JarFile jarFile = new JarFile(  fileName);
        AppClassLoader jarLauncher = new AppClassLoader(jarFile);
        List<Class<?>> classList = ClassUtils.scanPackage(  basePackage, jarLauncher);
        for (Class<?> clazz : classList) {
            if (clazz.isAnnotationPresent(APP.class)) {
              return   clazz.getAnnotation(APP.class);
            }
        }
        return null;
    }


    public void doInstall(String fileName, String basePackage, Container container, Application application, Context context) throws IOException {
        JarFile jarFile = new JarFile(  fileName);


        // 构建类加载器
        AppClassLoader jarLauncher = new AppClassLoader(jarFile);

        List<Class<?>> classList = ClassUtils.scanPackage(  basePackage, jarLauncher);

        application.setClassLoader(jarLauncher);


        // 构建app
        ClassUtils.buildApp(container, application, classList);


        // 构建模型

        for (EntityClass entityClass1 : application.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, container);
        }


        // 初始化表结构
        application.autoTableInit(context.getConfig());


        // 加载种子数据
        ClassUtils.loadSeedData(context, jarLauncher, application);



        // 启动事件
        application.onEvent();
    }

    public abstract void startUp() throws Exception ;


    public abstract void install(String fileName, String basePackage, Container container, Application application, Context context);
}
