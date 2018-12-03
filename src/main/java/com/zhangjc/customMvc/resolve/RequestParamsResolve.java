package com.zhangjc.customMvc.resolve;/**
 * Created by user on 2018/11/28.
 */

import com.zhangjc.customMvc.annotation.TomParams;
import com.zhangjc.customMvc.annotation.TomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName RequestParamsResolve
 * @Description TODO
 * @Autor user
 * @Date 2018/11/28 10:08
 * @Version 1.0
 **/
@TomService("requestParamsResolve")
public class RequestParamsResolve implements ParamsResolve {
    @Override
    public boolean support(Class<?> clazz, int paramIndex, Method method) {
        Annotation[] [] annotations = method.getParameterAnnotations();
        Annotation[] annotation = annotations[paramIndex];
        for(Annotation paramAnnotation : annotation){
            if(TomParams.class.isAssignableFrom(paramAnnotation.getClass())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object queryArgs(Class<?> clazz, int paramIndex, Method method,
                            HttpServletRequest request, HttpServletResponse response) {
        Annotation[] [] annotations = method.getParameterAnnotations();
        Annotation[] annotation = annotations[paramIndex];
        for(Annotation paramAnnotation : annotation){
            if(TomParams.class.isAssignableFrom(paramAnnotation.getClass())){
                TomParams params = (TomParams) paramAnnotation;
                String key = params.value();
                return request.getParameter(key);
            }
        }
        return null;

    }
}
