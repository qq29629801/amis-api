package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.container.Graph;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 应用加载
 * Created by bzGhost on 20230322.
 */
public class SdkLoader extends Loader {

    public static Logger logger = LoggerFactory.getLogger(SdkLoader.class);


    @Override
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

    @Override
    public void install(String fileName, String basePackage, Container container, Application application, Context context) {
        try {
            // TODO 找到旧的类加载器，停止端口


            //TODO
            this.doInstall(fileName, basePackage, container, application ,context);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
