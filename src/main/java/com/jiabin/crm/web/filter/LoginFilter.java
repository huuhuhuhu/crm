package com.jiabin.crm.web.filter;

import com.jiabin.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登录过的过滤器");
        //防止没有登陆直接通过网址进入网站查看网站内容
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;
        //不应该被拦截到资源,自动放行请求
        String path=request.getServletPath();
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);
        }
        //其他资源必须验证有没有成功登陆过
        else{
            HttpSession session=request.getSession();
            User user= (User) session.getAttribute("user");
            //如果user不为空，说明session中有user，则用户已经登录了
            if (user!=null){
                chain.doFilter(req,resp);
            }
            else{
                //说明没有成功登录，重定向到登陆页面
                /**
                 * 在实际项目开发中，对于路径的使用，不论操作的是前端还是后端，应该一律使用绝对路径
                 *
                 * 转发：
                 *      使用的是一种特殊的绝对路径的使用方式，这种绝对路径前面不加/项目名，这种路径也称之为内部路径
                 *      /login.jsp
                 *
                 * 重定向：
                 *      使用的是传统的决定路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                 *      /crm/login.jsp
                 *
                 * 此处使用重定向的原因：
                 *      转发之后，历经会停留在老路径上，而不是跳转之后最新资源的路径
                 *      我们应该在为用户跳转到登录页的同时，将浏览器的地址栏自动设置为当前的登录页的路径。（方便页面加载不完整时，
                 *      刷新页面。
                 */
                //request.getContextPath() 可以得到当前项目的项目名 "/crm"=request.getContextPath()
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }

        }


    }
}
