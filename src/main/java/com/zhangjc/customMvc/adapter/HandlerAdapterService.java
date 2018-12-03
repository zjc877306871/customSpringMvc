package com.zhangjc.customMvc.adapter;/**
 * Created by user on 2018/11/26.
 */

import com.zhangjc.customMvc.annotation.TomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName HandlerAdapterService
 * @Description TODO
 * @Autor user
 * @Date 2018/11/26 13:26
 * @Version 1.0
 **/

public interface HandlerAdapterService {

    public Object[] getParams(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object> beansMap);
}
