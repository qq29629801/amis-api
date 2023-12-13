package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.cglib.Proxy;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.exception.EngineLogger;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.util.BeanUtils;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模型方法
 */
public class EntityMethod extends Entity {
    private static EngineLogger log = EngineLogger.me(EntityMethod.class);

    private String name;
    private String displayName;
    private EntityClass entity;
    private Application application;
    private String className;
    private Map<String, Param> paramIns = new LinkedHashMap<>();
    private Map<String, Param> paramOuts = new LinkedHashMap<>();

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


    private Method method;

    public void setMethod(Method method) {
        this.method = method;
    }

    public EntityMethod() {

    }

    public EntityMethod(EntityClass entity) {
        this.entity = entity;
    }


    public Class getClazz() {
        AppClassLoader appClassLoader = (AppClassLoader) this.getApplication().getClassLoader();
        try {
            return appClassLoader.loadClass(this.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod() {
        if(method == null){
            AppClassLoader appClassLoader = (AppClassLoader) this.getApplication().getClassLoader();
            try {
                Class clazz = appClassLoader.loadClass(this.getClassName());
                for (Method method1 : clazz.getDeclaredMethods()) {
                    if (StringUtils.equals(method1.getName(), this.getName())) {
                        method = method1;
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }


    public <T> T invoke(Map<String, Object> inArgsValues) throws Exception {
        Parameter[] params = getMethod().getParameters();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Parameter parameter = params[i];
            String argName = parameter.getName();
            Object arg = inArgsValues.get(argName);
            args[i] = BeanUtils.toObject(parameter, arg);
        }
        return invoke(args);
    }

    public <T> T invoke(Object... args) {
        Connection conn = Db.getConfig().getThreadLocalConnection();
        if (conn != null) {
            try {
                return (T) getMethod().invoke(Proxy.getObject(this), args);
            } catch (Exception e) {
                throw new EngineException(e);
            }
        }

        try {
            conn = Db.getConfig().getConnection();
            Db.getConfig().setThreadLocalConnection(conn);
            try {
                return (T) getMethod().invoke(Proxy.getObject(this), args);
            } catch (Exception e) {
                log.error("method invoke exception",e);
                throw new EngineException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Db.getConfig().removeThreadLocalConnection();
            if (conn != null) {
                try{conn.close();}catch(Exception e){System.err.println(e.getMessage());}
            }
        }
    }

    public EntityClass getEntity() {
        return entity;
    }

    public void setEntity(EntityClass entity) {
        this.entity = entity;
    }
}
