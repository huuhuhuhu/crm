package com.jiabin.crm.settings.service.impl;

import com.jiabin.crm.exception.LoginException;
import com.jiabin.crm.settings.dao.UserDao;
import com.jiabin.crm.settings.domain.User;
import com.jiabin.crm.settings.service.UserService;
import com.jiabin.crm.utils.DateTimeUtil;
import com.jiabin.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userdao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map=new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user=userdao.login(map);

        if (user==null){
            throw new LoginException("账号密码错误");
        }

        //如果程序能够执行到这，说明账号密码正确
        //继续验证其他条件

        //验证失效时间
        String expireTime=user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState=user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        //判断是否是允许访问的ip地址
        String allowIps=user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址不允许访问");
        }
        return  user;
    }

    public List<User> getUserList() {
        List<User> uList=userdao.getUserList();
        return uList;
    }
}
