package com.yuyaogc.lowcode.engine.container;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.Entity;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import com.yuyaogc.lowcode.engine.util.Tuple;

import java.util.*;

/**
 * 容器
 */
public class Container {

    private static Container container;

    public static Container me() {
        if (container == null) {
            container = new Container();
        }
        return container;
    }

    private Map<String, Application> apps = new HashMap<>();

    private Map<String, AppClassLoader> appClassLoaders = new HashMap<>();


    private Map<String, Entity> metas = new LinkedHashMap<>();

    public Container() {
    }


    public void putEntity(Entity entity){
        metas.put(entity.getId(), entity);
    }

    public Collection<Application> getApps() {
        return apps.values();
    }



    public AppClassLoader getClassLoader(String appName){
        return appClassLoaders.get(appName);
    }



    public void putClassLoader(String appName, AppClassLoader appClassLoader){
        appClassLoaders.put(appName, appClassLoader);
    }





    public Application get(String name) {
        return apps.get(name);
    }

    public void add(Application application) {
        apps.put(application.getName(), application);
    }

    public void remove(String name) {
        Application application =  get(name);
        if(!Objects.isNull(application)){
            application.onDestroy();
        }
        apps.remove(name);
    }




    public void clear(){
        apps.clear();
    }

    public EntityClass getEntityClass(String className) {
        for (Application app : apps.values()) {
            if (app.containsKey(className)) {
                return app.getEntity(className);
            }
        }
        return null;
    }

}
