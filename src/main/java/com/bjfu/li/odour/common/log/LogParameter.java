package com.bjfu.li.odour.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
public @interface LogParameter {
    String value() default "-1";
}
