package com.zhangjc.customMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2018/11/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TomService {

    String value() default "";
}
