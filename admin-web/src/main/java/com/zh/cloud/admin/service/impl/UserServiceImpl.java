package com.zh.cloud.admin.service.impl;

import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.zh.cloud.admin.common.exception.ServiceException;
import io.ebean.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.zh.cloud.admin.model.User;
import com.zh.cloud.admin.service.UserService;

import java.util.Map;
import java.util.Set;

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

    public PagedList<User> findPage(User record, int pageNum, int pageSize) {
        Query<User> query = User.find.query();
        ExpressionList<User> where = query.where();
        Map<String, Object> vm = BeanExtUtils.getDeclaredFieldValueMap(record);
        if (!vm.isEmpty()) {
            vm.forEach((k, v) -> {
                if (CommonUtils.isEmpty(v)) return;
                where.eq(k, v);
            });
        }
        query.setFirstRow(pageNum).setMaxRows(pageSize);
        return query.findPagedList();
    }
}
