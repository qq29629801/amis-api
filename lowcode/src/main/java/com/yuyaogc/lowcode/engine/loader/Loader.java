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
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
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





    public List<String> installedList(Context context){
        // 已安装模组
        List<Model> modules =   context.get("base.base_module").search(Criteria.equal("state", 0), 0, 0, null);
        List<Long> moduleIds = modules.stream().map(model -> model.getLong("id")).collect(Collectors.toList());

        // 已经安装模组依赖
        List<Model> depends =  context.get("base.base_depends").search(Criteria.in("baseApp", (Object) moduleIds), 0, 0, null);


        // 构建图
        Graph graph = new Graph();

        // 添加边
        for(Model module: modules){
            List<Model> dependList =    depends.stream().filter(d->module.getLong("id").equals(d.getLong("baseApp"))).collect(Collectors.toList());
            for(Model depend: dependList){
                Optional<Model> optionalModel = modules.stream().filter(c -> c.getStr("appName").equals(depend.getStr("name"))).findFirst();
                if(optionalModel.isPresent()){
                    Model deptModule = optionalModel.get();
                    // 增加边
                    graph.addEdge(module.getLong("id"), deptModule.getLong("id"));
                }
            }
        }

        List<String> installedList = new ArrayList<>();
        // 拓扑排序
        for(Long id: graph.topologicalSort()){
            Optional<Model> optionalModel = modules.stream().filter(c -> c.getLong("id").equals(id)).findFirst();
            if(optionalModel.isPresent()){
                installedList.add(optionalModel.get().getStr("jarUrl"));
            }
        }

        // 拓扑排序
        System.out.println(graph.topologicalSort());

        return installedList;
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





    public void startUp() throws Exception {
        try (Context context = new Context(null, Db.getConfig())) {
            // base-1.0-SNAPSHOT.jar
            this.doInstall("base-1.0-SNAPSHOT.jar", "com.yatop.lambda", Container.me(), new Application() ,context);

            //
            List<String> installedList =   this.installedList(context);

            for(String installed: installedList){
                // TODO 包名
                this.doInstall(installed, "com.yatop.lambda", Container.me(), new Application() ,context);
            }

        } catch (Exception e){
            throw e;
        }

    }


    public abstract void install(String fileName, String basePackage, Container container, Application application, Context context);
}
