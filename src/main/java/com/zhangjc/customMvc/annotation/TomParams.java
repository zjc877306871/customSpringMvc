package com.zhangjc.customMvc.annotation;/**
 * Created by user on 2018/11/19.
 */

import java.lang.annotation.*;

/**
 * @ClassName TomParams
 * @Description TODO
 * @Autor user
 * @Date 2018/11/19 17:29
 * @Version 1.0
 **/
@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TomParams {
    String value() default "";
}
