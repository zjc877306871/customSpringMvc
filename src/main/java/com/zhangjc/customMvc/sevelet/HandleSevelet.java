package com.zhangjc.customMvc.sevelet;/**
 * Created by user on 2018/11/19.
 */

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
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);


        //加载class文件
        loadPackage("com,zhangjc");

        for(String name : classNamesList){
            System.out.println(name);
        }

        //实例化对象
        instance();
        for(Map.Entry<String, Object> entry : beansMap.entrySet()){
            System.out.println(entry.getKey());
        }


    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


    private void loadPackage(String packagepath) {

        //通过类加载器获取指定包下面的类资源
        URL url = this.getClass().getClassLoader().getResource("/" + replaceAll(packagepath));

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

    private String replaceAll(String path){
        return path.replace("\\.","/");
    }


}
