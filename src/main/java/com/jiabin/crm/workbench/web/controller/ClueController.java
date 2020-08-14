package com.jiabin.crm.workbench.web.controller;

import com.jiabin.crm.settings.domain.User;
import com.jiabin.crm.settings.service.UserService;
import com.jiabin.crm.settings.service.impl.UserServiceImpl;
import com.jiabin.crm.utils.DateTimeUtil;
import com.jiabin.crm.utils.PrintJson;
import com.jiabin.crm.utils.ServiceFactory;
import com.jiabin.crm.utils.UUIDUtil;
import com.jiabin.crm.workbench.domain.Activity;
import com.jiabin.crm.workbench.domain.Clue;
import com.jiabin.crm.workbench.service.ActivityService;
import com.jiabin.crm.workbench.service.ClueService;
import com.jiabin.crm.workbench.service.impl.ActivityServiceImpl;
import com.jiabin.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到线索活动控制器");
        String path=request.getServletPath();
        //开头斜杠必须加
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);

        }

        else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }
        else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }
        else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }
        else if ("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }
        else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }
        else if ("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }
        else if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }
        else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索转换的操作");
        String clueId=request.getParameter("clueId");
        //接收是否需要创建交易的标记
        String flag=request.getParameter("flag");

        //如果需要创建交易
        if ("a".equals(flag)){

        }
        //不需要创建交易
        else{

        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查询）");
        String name=request.getParameter("name");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByName(name);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动的操作");
        String cid=request.getParameter("cid");
        String aids[]=request.getParameterValues("aid");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("展现模糊搜索的市场活动列表+排除掉已经关联指定线索的列表");
        String name=request.getParameter("name");
        String clueId=request.getParameter("clueId");
        Map<String,String> map=new HashMap<String, String>();
        map.put("name",name);
        map.put("clueId",clueId);
        //调用业务层
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除关联操作");
        String id=request.getParameter("id");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id查询关联的市场活动列表");
        String clueId=request.getParameter("clueId");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到详细信息页");
        String id=request.getParameter("id");
        //System.out.println(id);
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=cs.detail(id);
        System.out.println("======================================"+clue);
        request.setAttribute("c",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加线索操作");
        String id= UUIDUtil.getUUID();
        String fullname=request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        //创建时间，当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");
        Clue  clue=new Clue();
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setCreateBy(createBy);
        clue.setDescription(description);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setId(id);
        clue.setOwner(owner);
        clue.setJob(job);
        clue.setMphone(mphone);
        clue.setNextContactTime(nextContactTime);
        clue.setPhone(phone);
        clue.setSource(source);
        clue.setState(state);
        clue.setWebsite(website);
        clue.setCreateTime(createTime);

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.save(clue);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }


}
