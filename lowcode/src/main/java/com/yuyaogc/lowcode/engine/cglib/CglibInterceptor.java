package com.yuyaogc.lowcode.engine.cglib;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class CglibInterceptor implements MethodInterceptor, Serializable {

    private final EntityMethod entityMethod;
    private static final long serialVersionUID = 1L;

    private final Aspect aspect;


    public CglibInterceptor( Aspect aspect, EntityMethod entityMethod) {
        this.aspect = aspect;
        this.entityMethod = entityMethod;
    }



    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        if (aspect.before(entityMethod.getClazz(), method, args)) {
            try {
				result = proxy.invokeSuper(obj, args);
            } catch (InvocationTargetException e) {
                if (aspect.afterException(entityMethod.getClazz(), method, args, e.getTargetException())) {
                    throw e;
                }
            }
        }
        if (aspect.after(entityMethod.getClazz(), method, args, result)) {
            return result;
        }
        return null;
    }
}
