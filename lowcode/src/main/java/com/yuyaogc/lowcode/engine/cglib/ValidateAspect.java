package com.yuyaogc.lowcode.engine.cglib;


import com.yuyaogc.lowcode.engine.entity.EntityMethod;

import java.lang.reflect.Method;


public class ValidateAspect implements Aspect {
    private static final long serialVersionUID = 1L;

    public ValidateAspect(EntityMethod entityMethod) {
        this.entityMethod = entityMethod;
    }

    private EntityMethod entityMethod;



    @Override
    public boolean before(Object target, Method method, Object[] args) {
        switch (method.getName()) {
            case "create":
            case "update":
                break;
            default: {
            }
        }
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        return true;
    }
}
