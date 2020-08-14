package com.jiabin.crm.workbench.service.impl;

import com.jiabin.crm.settings.dao.UserDao;
import com.jiabin.crm.settings.domain.User;
import com.jiabin.crm.utils.SqlSessionUtil;
import com.jiabin.crm.vo.PageinationVO;
import com.jiabin.crm.workbench.dao.ActivityDao;
import com.jiabin.crm.workbench.dao.ActivityRemarkDao;
import com.jiabin.crm.workbench.domain.Activity;
import com.jiabin.crm.workbench.domain.ActivityRemark;
import com.jiabin.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityRemarkDao activityRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public Boolean save(Activity a) {
        Boolean flag=true;
        int count=activityDao.save(a);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public PageinationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total=activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> datalist=activityDao.getActivityListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PageinationVO<Activity> vo=new PageinationVO<Activity>();
        vo.setTotal(total);
        vo.setDatalist(datalist);

        return vo;
    }

    public Boolean delete(String[] ids) {
        /*Boolean flag=true;
        int count=activityDao.delete(ids);
        if (count!=ids.length){
            flag=false;
        }
        return flag;*/
        Boolean flag=true;

        //查询出需要删除的备注的数量
        int count1=activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数，（实际删除的数量）
        int count2=activityRemarkDao.deleteByAids(ids);

        if (count1!=count2){
            flag=false;
        }

        //删除市场活动
        int count3=activityDao.delete(ids);
        if (count3!=ids.length){
            flag=false;
        }
        return flag;

    }

    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList
        List<User> uList=userDao.getUserList();

        //取市场活动信息
        Activity a=activityDao.getById(id);

        //将uList和市场活动信息存放在map中
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("a",a);

        //返回map
        return map;
    }

    public Boolean update(Activity a) {
        Boolean flag=true;
        int count=activityDao.update(a);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public Activity detail(String id) {
        Activity a=activityDao.detail(id);
        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList=activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }

    public Boolean deleteRemark(String id) {
        Boolean flag=true;
        int count=activityRemarkDao.deleteById(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean saveRemark(ActivityRemark ar) {
        Boolean flag=true;
        int count=activityRemarkDao.save(ar);
        if (count!=1){
            flag=false;
        }
        return flag;

    }

    public boolean updateRemark(ActivityRemark ar) {
        Boolean flag=true;
        int count=activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList=activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> aList=activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    public List<Activity> getActivityListByName(String name) {
        List<Activity> aList=activityDao.getActivityListByName(name);
        return aList;
    }
}
