package com.yuyaogc.lowcode.engine.cglib;

import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import net.sf.cglib.proxy.Enhancer;

public class Proxy {
    public static <T> T getProxy(EntityMethod method) {
        Enhancer e = new Enhancer();
        e.setSuperclass(method.getClazz());
        ValidateAspect aspect = new ValidateAspect(method);
        CglibInterceptor c = new CglibInterceptor(aspect,method);
        e.setCallback(c);
        return (T) e.create();
    }
}
