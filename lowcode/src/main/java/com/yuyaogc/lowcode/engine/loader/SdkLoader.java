package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.loader.lifecycle.Lifecycle;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 应用加载
 * Created by bzGhost on 20230322.
 */
public class SdkLoader extends Loader {

    public static Logger logger = LoggerFactory.getLogger(SdkLoader.class);


    @Override
    public void build(String fileName, String basePackage, Container container, Application application) {
        try {

            JarFile jarFile = new JarFile(  fileName);
            AppClassLoader jarLauncher = new AppClassLoader(jarFile);


            List<Class<?>> classList = ClassUtils.scanPackage(basePackage, jarLauncher);

            ClassUtils.processApp(container, application, classList, jarLauncher);


            for (Application app : container.getApps()) {
                /**
                 *  构建模型
                 */
                for (EntityClass entityClass1 : app.getModels()) {
                    ClassBuilder.buildEntityClass(entityClass1, container);
                }

                /**
                 *  初始化表
                 */
                try (Context context = new Context(null, Db.getConfig())) {

                    app.autoTableInit(context.getConfig());

                    app.onEvent(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            throw new EngineException(e);
        }
    }

    @Override
    public void install(List<Application> installedApps) {

    }

    @Override
    public void reboot(List<Application> applications) {
        Map<AppStateEnum, List<Application>> apps = applications.stream().collect(Collectors.groupingBy(Application::getStateEnum));
        for (Map.Entry<AppStateEnum, List<Application>> app : apps.entrySet()) {
            Lifecycle lifecycle = Lifecycle.getLifecycle(app.getKey());
            lifecycle.load(app.getValue());
        }
        applications.forEach(application -> build(application.getFileName(), "com.yatop.lambda", Container.me(), application));
    }


}
