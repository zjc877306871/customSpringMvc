package com.zhangjc.customMvc.resolve;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by user on 2018/11/26.
 */
public interface ParamsResolve {

    //校验是否支持该类型参数的解析
    public boolean support(Class<?> clazz, int paramIndex, Method method);

    public Object queryArgs(Class<?> clazz, int paramIndex, Method method, HttpServletRequest request,
                              HttpServletResponse response);

}
