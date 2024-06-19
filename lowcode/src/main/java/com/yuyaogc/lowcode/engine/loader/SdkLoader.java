package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
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
            try (Context context = new Context(null, Db.getConfig())) {

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


                application.onEvent(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new EngineException(e);
        }
    }






}
