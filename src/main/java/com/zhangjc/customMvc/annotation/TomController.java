package com.zhangjc.customMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by user on 2018/11/19.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TomController {

    String value() default "";
}
