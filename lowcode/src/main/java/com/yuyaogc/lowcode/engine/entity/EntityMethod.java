package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.cglib.Proxy;
import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.exception.EngineLogger;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.util.JsonUtil;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.Map;

/**
 * 模型方法
 */
public class EntityMethod extends Entity{
    private static EngineLogger log = EngineLogger.me(EntityMethod.class);

    private String name;
    private String displayName;
    private EntityClass entity;
    private Application application;

    private String className;
    private Method method;
    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public EntityMethod() {

    }

    public EntityMethod(EntityClass entity) {
        this.entity = entity;
    }


    public Method getMethod(){
        AppClassLoader appClassLoader = (AppClassLoader) this.getApplication().getClassLoader();
        try {
          this.clazz =  appClassLoader.loadClass(this.getClassName());
          for(Method method: clazz.getDeclaredMethods()){
              if(StringUtils.equals(method.getName(), this.getName())){
                  this.method = method;
                  return this.method;
              }
          }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public <T> T invoke(Map<String, Object> inArgsValues) throws Exception{
        Parameter[] params = getMethod().getParameters();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Parameter parameter = params[i];
            String argName = parameter.getName();
            Object arg = inArgsValues.get(argName);
            if (arg != null) {
                if (!parameter.getType().isAssignableFrom(arg.getClass())) {
                    // Class<?>
                    try {
                        String json = JsonUtil.objectToString(arg);
                        arg = JsonUtil.stringToObject(json, parameter.getType());
                    } catch (IOException e) {
                        log.error(EngineErrorEnum.JsonProcessingException, e);
                    }
                }

                // List<?>
                if (!parameter.getType().isAssignableFrom(Map.class)) {
                    if (parameter.getType().isAssignableFrom(arg.getClass())) {
                        String json = JsonUtil.objectToString(arg);
                        Type type = parameter.getParameterizedType();
                        if (type instanceof ParameterizedType) {
                            ParameterizedType type1 = (ParameterizedType) type;
                            if (type1.getActualTypeArguments().length > 0) {
                                if (type1.getActualTypeArguments()[0] instanceof Class) {
                                    Class<?> clazz = (Class<? extends Object>) type1.getActualTypeArguments()[0];
                                    try {
                                        arg = JsonUtil.stringToList(json, clazz);
                                    } catch (IOException e) {
                                        log.error(EngineErrorEnum.JsonProcessingException, e);
                                    }
                                }
                            }
                        }
                    }
                }

            }
            args[i] = arg;
        }

        return exe(args);
    }

    public <T> T exe(Object... args) {
        try {

            return (T) getMethod().invoke(Proxy.getProxy(this), args);
        } catch (IllegalAccessException e) {
            log.error(EngineErrorEnum.IllegalAccessException, e);
        } catch (InvocationTargetException e) {
            log.error(EngineErrorEnum.InvocationTargetException, e);
        }
        return null;
    }

    public EntityClass getEntity() {
        return entity;
    }

    public void setEntity(EntityClass entity) {
        this.entity = entity;
    }
}
