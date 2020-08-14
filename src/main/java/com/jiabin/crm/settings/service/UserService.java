package com.jiabin.crm.settings.service;

import com.jiabin.crm.exception.LoginException;
import com.jiabin.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
