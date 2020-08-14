package com.jiabin.crm.settings.web.controller;

import com.jiabin.crm.settings.domain.User;
import com.jiabin.crm.settings.service.UserService;
import com.jiabin.crm.settings.service.impl.UserServiceImpl;
import com.jiabin.crm.utils.MD5Util;
import com.jiabin.crm.utils.PrintJson;
import com.jiabin.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path=request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录的操作");
        String loginAct=request.getParameter("loginAct");
        String loginPwd=request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5的密文形式
        loginPwd= MD5Util.getMD5(loginPwd);
        //接收浏览器的ip地址
        String ip=request.getRemoteAddr();
        System.out.println("================ip地址"+ip);

        //业务层开发，统一使用代理类形态的接口对象
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user=us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            //如果程序执行到此处，说明业务层没有为contrller抛出任何的异常
            //表示登陆成功
            PrintJson.printJsonFlag(response,true);
        } catch (Exception e) {
            e.printStackTrace();
            String msg=e.getMessage();
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }


}
