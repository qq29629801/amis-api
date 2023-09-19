package com.yuyaogc.lowcode.engine.container;

import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public Container() {
    }


    public Collection<Application> getApps() {
        return apps.values();
    }


    public Application get(String name) {
        return apps.get(name);
    }

    public void add(String name, Application application) {
        apps.put(name, application);
    }

    public void remove(String name) {
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
