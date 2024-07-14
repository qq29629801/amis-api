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

                this.doInstall(fileName, basePackage, container, application ,context);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new EngineException(e);
        }
    }

    @Override
    public void install(String fileName, String basePackage, Container container, Application application, Context context) {
        try {
            //TODO
            this.doInstall(fileName, basePackage, container, application ,context);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public void addModule(List<Model> installs){
        Graph graph = new Graph(6);
        graph.addEdge(0L, 1L);

    }


}
