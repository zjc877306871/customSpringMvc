package com.zhangjc.customMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2018/11/22.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD,ElementType.METHOD})
public @interface TomAutowired {
    String value() default "";
}
