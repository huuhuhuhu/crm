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
import com.jiabin.crm.workbench.domain.Tran;
import com.jiabin.crm.workbench.domain.TranHistory;
import com.jiabin.crm.workbench.service.ActivityService;
import com.jiabin.crm.workbench.service.ClueService;
import com.jiabin.crm.workbench.service.CustomerService;
import com.jiabin.crm.workbench.service.TranService;
import com.jiabin.crm.workbench.service.impl.ActivityServiceImpl;
import com.jiabin.crm.workbench.service.impl.ClueServiceImpl;
import com.jiabin.crm.workbench.service.impl.CustomerServiceImpl;
import com.jiabin.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到交易活动控制器");
        String path=request.getServletPath();
        //开头斜杠必须加
        if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);

        }

        else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }
        else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }
        else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }
        else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(request,response);
        }
        else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }
        else if ("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计图表的数据");
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        /**
         * 业务层为我们返回total 和dataList
         * 通过map打包以上两项返回
         */
        Map<String,Object> map=ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改阶段的操作");
        String id=request.getParameter("id");
        String stage=request.getParameter("stage");
        String expectedDate=request.getParameter("expectedDate");
        String money=request.getParameter("money");
        String editTime= DateTimeUtil.getSysTime();
        String editBy= ((User) request.getSession().getAttribute("user")).getName();
        Tran t=new Tran();
        t.setEditTime(editTime);
        t.setEditBy(editBy);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setId(id);
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.changeStage(t);
        Map<String,Object> map=new HashMap<>();
        ServletContext application=this.getServletContext();
        Map<String,String> pMap= (Map<String, String>) application.getAttribute("pMap");
        t.setPossibility(pMap.get(stage));
        map.put("success",flag);
        map.put("t",t);
        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取交易历史列表");
        String tranId=request.getParameter("tranId");
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList=ts.getHistoryListByTranId(tranId);
        //阶段和可能性之间的关系
        ServletContext application=this.getServletContext();
        Map<String,String> pMap= (Map<String, String>) application.getAttribute("pMap");

        //将交易历史遍历
        for (TranHistory th:thList){
            String stage=th.getStage();
            String possibility=pMap.get(stage);
            th.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("进入交易的详情页");
        String id=request.getParameter("id");
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t=ts.detail(id);
        //处理可能性
        String stage=t.getStage();
        ServletContext application=this.getServletContext();
        Map<String,String> pMap= (Map<String, String>) application.getAttribute("pMap");
        String possibility=pMap.get(stage);
        /*
            当需要将一些字段封装到请求对象中时，如果字段很少，也可以在实体类中扩充
         */
        //request.setAttribute("possibility",possibility);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("交易的添加操作");
        String id=UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source=request.getParameter("source");
        String activityId=request.getParameter("activityId");
        String contactsId=request.getParameter("contactsId");
        //创建时间，当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        Tran t=new Tran();
        t.setType(type);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setActivityId(activityId);
        t.setStage(stage);
        t.setId(id);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setSource(source);
        t.setOwner(owner);
        t.setNextContactTime(nextContactTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setContactsId(contactsId);
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag=ts.save(t,customerName);
        if (flag){
            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");
        String name=request.getParameter("name");
        CustomerService  cs= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList=cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到跳转到交易添加页的操作");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }


}
