package com.yuyaogc.lowcode.engine.context;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * 上下文
 */
public class Context implements AutoCloseable {
    private Map<String, Object> arguments;
    private Map<String, Object> result;

    private String app;
    private String model;
    private String service;
    private Config config;
    private String userId;


    public String getApp() {
        return app;
    }

    public String getModel() {
        return model;
    }

    public String getService() {
        return service;
    }

    public String getUserId() {
        return userId;
    }

    public Context(String userId, Config config) {
        if (StringUtil.isEmpty(userId)) {
            userId = "super";
        }
        if(StringUtil.isEmpty(this.app)){
            this.app = "base";
        }
        this.userId = userId;
        this.config = config;
    }

    public Map<String, Object> getResult() {
        if (result == null) {
            result = new HashMap<>(0);
        }
        return result;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public EntityClass getEntity() {
        EntityClass entityClass = getApp(this.app).getEntity(this.model);
        if(!Objects.isNull(entityClass)){
            return entityClass;
        }
        for(Application application:Container.me().getApps()){
            EntityClass entityClass1 = application.getEntity(this.model);
            if(!Objects.isNull(entityClass1)){
                return entityClass1;
            }
        }

       throw new EngineException(String.format("Application %s EntityClass %s is null" , this.app, this.model));
    }



    public Context get(String model){
        if(StringUtil.isEmpty(model)){
            throw new EngineException("model is Empty");
        }
        String[] names =  model.split("\\.");
        this.app = names[0];
        this.model = names[1];
        return this;
    }




    public Application getApp(String appName) {
        return Container.me().get(appName);
    }


    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.model = (String) arguments.get("model");
        this.service = (String) arguments.get("service");
        this.app = (String) arguments.getOrDefault("app", "base");
        this.arguments = arguments;
    }

    public Map<String, Object> getArgs() {
        return (Map<String, Object>) this.arguments.get("args");
    }

    @Override
    public void close() throws Exception {

    }

    /**
     * api调用
     *
     * @return
     */
    public Object call() throws Exception {
        LinkedList<EntityMethod> methods = getEntity().getMethod(this.service);
        if(Objects.isNull(methods)){
            throw new EngineException(String.format("模型%s服务%s", this.model, this.service));
        }
        EntityMethod entityMethod = methods.get(0);
        return getResult().put("data", entityMethod.invoke(getArgs()));
    }

    /**
     * 跨应用调用方法
     *
     * @param method 方法名
     * @param args   参数
     * @return 返回值
     */
    public <T> T call(String method, Object... args)  {
        LinkedList<EntityMethod> methods = getEntity().getMethod(method);
        if(Objects.isNull(methods)){
            throw new EngineException(String.format("模型%s服务%s", this.model, this.service));
        }
        EntityMethod entityMethod = methods.get(0);
       return entityMethod.invoke(args);
    }
}
