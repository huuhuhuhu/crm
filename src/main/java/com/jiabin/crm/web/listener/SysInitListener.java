package com.jiabin.crm.web.listener;

import com.jiabin.crm.settings.domain.DicValue;
import com.jiabin.crm.settings.service.DicService;
import com.jiabin.crm.settings.service.impl.DicServiceImpl;
import com.jiabin.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * Author 北京动力节点
 */
public class SysInitListener implements ServletContextListener {

    /*

        该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，马上执行该方法

        event：该参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数能取得什么对象
                例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象

     */
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("上下文域对象创建了");

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        /*

            应该管业务层要
            7个list

            可以打包成为一个map
            业务层应该是这样来保存数据的：
                map.put("appellationList",dvList1);
                map.put("clueStateList",dvList2);
                map.put("stageList",dvList3);
                ....
                ...




         */
        Map<String, List<DicValue>> map = ds.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for(String key:set){

            application.setAttribute(key, map.get(key));

        }

        System.out.println("服务器缓存处理数据字典结束");

        //===================================================================================
        /*
            数据字典处理完毕后，处理Stage2Possibility.properties文件
            1. 解析该文件
            2.将该属性文件中的键值对关系处理成为Java中的键值对关系
            Map<String(阶段），String(可能性）>
            保存在上下文作用域对象中
         */
        //解析属性配置文件
        Map<String,String> pMap=new HashMap<>();
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=rb.getKeys();
        while(e.hasMoreElements()){
            //阶段
            String key=e.nextElement();
            //可能性
            String value=rb.getString(key);
            pMap.put(key,value);
        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);



    }



}
