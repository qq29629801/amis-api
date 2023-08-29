package com.yuyaogc.lowcode.engine.loader.lifecycle;

import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.loader.AppStateEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Lifecycle {
    static Map<AppStateEnum, Class> lifecycles = new HashMap<>();

    static {
        lifecycles.put(AppStateEnum.INSTALL, Install.class);
        lifecycles.put(AppStateEnum.UNINSTALL, Uninstall.class);
        lifecycles.put(AppStateEnum.INSTALLED, Install.class);
        lifecycles.put(AppStateEnum.UPDATABLE, Updatable.class);
    }

    public static Lifecycle getLifecycle(AppStateEnum status) {
        Class<?> clazz = lifecycles.get(status);
        if (clazz == null) {
            return new Install();
        }
        try {
            return (Lifecycle) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("创建Lifecycle%s失败,class=%s", status, clazz.getName()), e);
        }
    }

    public abstract void load(List<Application> applicationList);

}
