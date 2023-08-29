package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.mybatisplus.ClassPlus;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

public class MybatisPlusLoader extends Loader {
    @Override
    public void build(String fileName, String basePackage, Container container, Application application) {
        try {

            JarFile jarFile = new JarFile(fileName);
            AppClassLoader jarLauncher = new AppClassLoader(jarFile);


            List<Class<?>> classList = ClassPlus.scanPackage(basePackage, jarLauncher);

            ClassPlus.processApp(container, application, classList, jarLauncher);


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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            throw new EngineException(e);
        }
    }

    @Override
    public void install(List<Application> apps) {

    }

    @Override
    public void reboot(List<Application> apps) {

    }
}
