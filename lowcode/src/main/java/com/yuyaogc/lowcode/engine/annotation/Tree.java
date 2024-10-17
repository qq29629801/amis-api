package com.yuyaogc.lowcode.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tree {

    String label() default "";
    String columnName() default "";

    int length() default 255;


    /**
     * 指定父属性
     * @return
     */
    String parent() default "parentId";

    /**
     * 指定主键属性
     * @return
     */
    String primary() default "id";
}
