package com.yuyaogc.lowcode.engine.annotation;


import com.yuyaogc.lowcode.engine.util.CascadeType;
import com.yuyaogc.lowcode.engine.util.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {

    CascadeType[] cascade() default {};

    FetchType fetch() default FetchType.LAZY;

}
