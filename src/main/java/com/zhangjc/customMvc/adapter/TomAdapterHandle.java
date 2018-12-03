package com.zhangjc.customMvc.adapter;/**
 * Created by user on 2018/11/26.
 */

import com.zhangjc.customMvc.annotation.TomService;
import com.zhangjc.customMvc.resolve.ParamsResolve;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TomAdapterHandle
 * @Description TODO
 * @Autor user
 * @Date 2018/11/26 13:32
 * @Version 1.0
 **/
@TomService("tomAdapterHandle")
public class TomAdapterHandle implements HandlerAdapterService {

    @Override
    public Object[] getParams(HttpServletRequest request, HttpServletResponse response,
                              Method method, Map<String, Object> beansMap) {
        //首先获取该方法内有多少参数类型
        Class<?>[] paramsClazzs = method.getParameterTypes();
        Object[] args = new Object[paramsClazzs.length];
        //获取解析器对象的集合
        Map<String, Object> resolveMap = beansOfType(beansMap,ParamsResolve.class);

        int paramIndex = 0;
        int i = 0;
        //对不同的参数类进行不同的处理------采用策略模式
        for(Class<?> clazz : paramsClazzs){
            for(Map.Entry<String, Object> entry : resolveMap.entrySet()){
                ParamsResolve paramsResolve = (ParamsResolve) entry.getValue();
                if(paramsResolve.support(clazz,paramIndex,method)){
                    args[i++] = paramsResolve.queryArgs(clazz,paramIndex,method,request,response);
                }

            }
            paramIndex++;
        }

        return args;
    }

    private Map<String,Object> beansOfType(Map<String, Object> beansMap, Class<ParamsResolve> paramsResolveClass) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> beans : beansMap.entrySet()) {
            //拿到实例-->反射对象-->它的接口(接口有多实现,所以为数组)
            //获取该对象实现的接口
            Class<?>[] interfaces = beans.getValue().getClass().getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                for (Class<?> clazz : interfaces) {
                    if (clazz.isAssignableFrom(paramsResolveClass)) {
                        resultMap.put(beans.getKey(), beans.getValue());
                    }
                }

            }
        }
        return resultMap;
    }

}
