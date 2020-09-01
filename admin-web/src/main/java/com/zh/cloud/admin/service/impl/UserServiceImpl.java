package com.zh.cloud.admin.service.impl;

import com.zh.cloud.admin.common.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.zh.cloud.admin.model.User;
import com.zh.cloud.admin.service.UserService;

/**
 * 用户信息业务层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static byte[] seeds = "canal is best!".getBytes();

    public User find4Login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        User user = User.find.query().where().eq("username", username).findOne();
        if (user == null) {
            throw new ServiceException("user:" + username + " auth failed!");
        }


        user.setPassword("");
        return user;
    }

    public void update(User user) {
        User userTmp = User.find.query().where().eq("username", user.getUsername()).findOne();
        if (userTmp == null) {
            throw new ServiceException();
        }


        user.update("username", "nn:password");
    }
}
