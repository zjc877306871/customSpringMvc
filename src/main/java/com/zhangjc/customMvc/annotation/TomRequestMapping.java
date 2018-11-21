package com.zhangjc.customMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2018/11/19.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value ={ElementType.TYPE, ElementType.METHOD})
public @interface TomRequestMapping {
    String value() default "";
}
