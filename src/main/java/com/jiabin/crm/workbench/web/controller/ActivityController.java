package com.jiabin.crm.workbench.web.controller;

import com.jiabin.crm.settings.domain.User;
import com.jiabin.crm.settings.service.UserService;
import com.jiabin.crm.settings.service.impl.UserServiceImpl;
import com.jiabin.crm.utils.DateTimeUtil;
import com.jiabin.crm.utils.PrintJson;
import com.jiabin.crm.utils.ServiceFactory;
import com.jiabin.crm.utils.UUIDUtil;
import com.jiabin.crm.vo.PageinationVO;
import com.jiabin.crm.workbench.domain.Activity;
import com.jiabin.crm.workbench.domain.ActivityRemark;
import com.jiabin.crm.workbench.service.ActivityService;
import com.jiabin.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path=request.getServletPath();
        //开头斜杠必须加
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);

        }

        else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }
        else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }
        else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }
        else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }
        else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }
        else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request,response);
        }
        else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }
        else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("备注修改操作");
        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        //System.out.println("===================="+editContent);
        String editFlag="1";
        String editBy= ((User) request.getSession().getAttribute("user")).getName();
        String editTime= DateTimeUtil.getSysTime();
        ActivityRemark ar=new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setId(id);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.updateRemark(ar);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存备注操作");
        String noteContent=request.getParameter("noteContent");
        String activityId=request.getParameter("activityId");
        //id采用工具生成
        String id= UUIDUtil.getUUID();
        //创建时间，当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);
        ar.setNoteContent(noteContent);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.saveRemark(ar);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id，取得备注信息列表");
        String activityId=request.getParameter("activityId");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList=as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response,arList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("进入跳转到详细信息页的操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a=as.detail(id);
        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改操作");

        String id= request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        //修改时间，当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy= ((User) request.getSession().getAttribute("user")).getName();
        //将这些参数封装成一个对象，将对数作为参数传入Service方法
        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        /*System.out.println("===================================================================");
        System.out.println(id);
        System.out.println(owner);
        System.out.println(name);
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(cost);
        System.out.println(description);
        System.out.println(editBy);
        System.out.println(editTime);*/

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.update(a);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");
        //可以通过请求中同名的参数，得到参数数组
        String ids[] =request.getParameterValues("id");
       /* for (String id:ids){
            System.out.println(id);
        }*/
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.delete(ids);
        PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询和分页查询）");

        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount=(pageNo-1)*pageSize;

        //因为这些参数需要传到后台进行处理，而又不能够包装成一个domain对象，所以存在一个map中，将map作为
        //方法参数传递到后台
        //后台的多个参数传递到前端，无法使用实体类封装时，使用vo类
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /*
            前端需要：市场活动信息列表
                      查询的总条数

             业务层拿到了以上两项信息后，如果做返回
             1.map 复用率低使用map
             2.vo  复用率高用vo

             将来分页查询，每个模块都有，所以我们选择使用一个通用vo,操作起来比较方便
         */
        PageinationVO<Activity> vo=as.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");
        //id采用工具生成
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        //创建时间，当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        //将这些参数封装成一个对象，将对数作为参数传入Service方法
        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.save(a);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=userService.getUserList();
        //使用工具类将uList包装成json,
        PrintJson.printJsonObj(response,uList);
    }


}
