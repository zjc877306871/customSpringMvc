package com.zhangjc.customMvc.Controller;/**
 * Created by user on 2018/11/19.
 */

import com.zhangjc.customMvc.annotation.TomController;
import com.zhangjc.customMvc.annotation.TomParams;
import com.zhangjc.customMvc.annotation.TomRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName BusyController
 * @Description TODO
 * @Autor user
 * @Date 2018/11/19 17:25
 * @Version 1.0
 **/

@TomController
@TomRequestMapping("/busy")
public class BusyController {

    @TomRequestMapping("query")
    public String query(HttpServletRequest request, HttpServletResponse response, @TomParams("name") String name){
        return null;
    }
}
