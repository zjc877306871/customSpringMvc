package com.zhangjc.customMvc.sevelet;/**
 * Created by user on 2018/11/19.
 */

import com.zhangjc.customMvc.adapter.HandlerAdapterService;
import com.zhangjc.customMvc.annotation.TomAutowired;
import com.zhangjc.customMvc.annotation.TomController;
import com.zhangjc.customMvc.annotation.TomRequestMapping;
import com.zhangjc.customMvc.annotation.TomService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


/**
 * @ClassName HandleSevelet
 * @Description TODO
 * @Autor user
 * @Date 2018/11/19 17:03
 * @Version 1.0
 **/
public class HandleSevelet extends HttpServlet {

    Map<String, Object> beansMap = new HashMap<String, Object>();
    List<String> classNamesList = new ArrayList<>();
    Map<String, Object> mappingMap = new HashMap<>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);


        //加载class文件
        loadPackage("com.zhangjc");

        for(String name : classNamesList){
            System.out.println(name);
        }

        //实例化对象
        instance();
        for(Map.Entry<String, Object> entry : beansMap.entrySet()){
            System.out.println(entry.getKey());
        }

        //ioc
        ioc();
        // 4、建立一个path与method的映射关系
        HandlerMapping();



    }




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径  /customSpringMvc/busy/query
        String url = req.getRequestURI();
        //获取上下文根路径  /customSpringMvc
        String context = req.getContextPath();
        url = url.replace(context,"");
        //通过路径获取到对应的方法
        Method method = (Method) mappingMap.get(url);
        //从实例容器中 获取对应的controller
        Object instance = beansMap.get("/" + url.split("/")[1]);

        //获取处理参数的处理器
        HandlerAdapterService service = (HandlerAdapterService) beansMap.get("tomAdapterHandle");

        Object[] params = service.getParams(req, resp, method, beansMap);
        try {
            method.invoke(instance,params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }


    private void loadPackage(String packagepath) {

        //通过类加载器获取指定包下面的类资源
        String resourceName = "/" + replaceAll(packagepath);
        URL url = this.getClass().getClassLoader().getResource(resourceName);

        //获取资源的路径，并生成文件信息
        String fileStr = url.getFile();
        File file = new File(fileStr);

        //拿到所有类com.zhangjc下的customMvc文件夹
        String[] filePathList = file.list();
        for(String path : filePathList){
            File filePath = new File(fileStr + path);

            if(filePath.isDirectory()){
                //递归遍历所有的文件夹
                loadPackage(packagepath + "." + path);
            }else{
                //将获取到的类放到一起，准备实例化
                classNamesList.add(packagepath + "." + filePath.getName());
            }

        }

    }

    private void instance(){

        if(classNamesList.size() <= 0){
            System.out.println("没有任何类！！！！");
            return;
        }
        for(String name : classNamesList) {
            try {
                name = name.replace(".class", "");
                //获取类的class文件
                Class<?> clazz = Class.forName(name);
                // 将扫描到的类，获取类名，并判断是否标记了TomController注解
                if (clazz.isAnnotationPresent(TomController.class)) {
                    TomController tomController = clazz.getAnnotation(TomController.class);
                    Object instance = clazz.newInstance();
                    //获取TomRequestMapping的值
                    TomRequestMapping tomRequestMapping = clazz.getAnnotation(TomRequestMapping.class);
                    String value = tomRequestMapping.value();
                    beansMap.put(value,instance);
                }else if(clazz.isAnnotationPresent(TomService.class)){
                    TomService tomService = clazz.getAnnotation(TomService.class);
                    Object instance = clazz.newInstance();
                    //获取TomService的值
                    String value = tomService.value();
                    beansMap.put(value,instance);
                }else{
                    continue;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 依赖式注入
     */
    private void ioc() {
        //判断bean集合是否有值
        if(beansMap.entrySet().size() <0){
            System.out.println("容器集合没有内容");
            return;
        }
        //遍历已经存在的bean。
        for(Map.Entry<String, Object> entry : beansMap.entrySet()){
            //获取bean的实例
            Object instance = entry.getValue();
            //获取对象的class对象
            Class<?> clazz = instance.getClass();
            //判断该类是不是有TomController
            if(clazz.isAnnotationPresent(TomController.class)){
                //获取类的属性值
                Field[] fields = clazz.getFields();
                for(Field field : fields){
                    if(field.isAnnotationPresent(TomAutowired.class)){
                        //获取TomAutowired注解的类属性
                        TomAutowired tomAutowired = (TomAutowired)field.getAnnotation(TomAutowired.class);
                        String value = tomAutowired.value();

                        //开启设置权限
                        field.setAccessible(true);
                        try {
                            //对该属性设置
                            field.set(instance, beansMap.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        continue;
                    }

                }
            }else {
                continue;
            }
        }
    }

    /**
     * 建立起映射关系
     */
    private void HandlerMapping() {
        if(beansMap.entrySet().size() <0){
            System.out.println("容器中没有对象");
            return;
        }
        //遍历所有的对象
        for(Map.Entry<String, Object> entry : beansMap.entrySet()){
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if(clazz.isAnnotationPresent(TomController.class)){
                String classpath = null;
                if(clazz.isAnnotationPresent(TomRequestMapping.class)){
                    TomRequestMapping requestMapping = clazz.getAnnotation(TomRequestMapping.class);
                    classpath = requestMapping.value();
                }
                Method[] methods = clazz.getMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(TomRequestMapping.class)){
                        TomRequestMapping requestMapping = method.getAnnotation(TomRequestMapping.class);
                        String value = requestMapping.value();
                        //将路径和对应的方法存放到一个map中
                        mappingMap.put(classpath + value, method);
                    }else {
                        continue;
                    }
                }

            }else {
                continue;
            }
        }

    }

    private String replaceAll(String path){
        return path.replace(".","/");
    }





}

