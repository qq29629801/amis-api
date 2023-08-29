package com.yuyaogc.lowcode.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface APPInfo {
    String name() default "";

    String displayName() default "";

    String version() default "1.0.0";

    String scanPackage() default "";

    String[] depends() default {};
}
