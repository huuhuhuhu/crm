package com.jiabin.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodeingFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中中文参数乱码
        req.setCharacterEncoding("utf-8");
        //过滤响应流中响应中文乱码
        resp.setContentType("text/html;charset=utf-8");

        //将请求放行
        chain.doFilter(req,resp);
    }
}
