package com.zh.cloud.admin.service.upms.impl;

import com.ch.e.PubError;
import com.ch.result.InvokerPage;
import com.ch.result.ResultUtils;
import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
import com.zh.cloud.admin.common.exception.ServiceException;
import io.ebean.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;

import java.util.Map;

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
            ExceptionUtils._throw(PubError.USERNAME_OR_PASSWORD);
        }
        User user = User.find.query().where().eq("username", username).findOne();
        if (user == null) {
            ExceptionUtils._throw(PubError.EXISTS, "用户不存在!");
        }
        return user;
    }

    public void update(User user) {
        User userTmp = User.find.query().where().eq("username", user.getUsername()).findOne();
        if (userTmp == null) {
            throw new ServiceException();
        }
        user.update("username", "nn:password");
    }

    public InvokerPage.Page<User> findPage(User record, int pageNum, int pageSize) {
        Query<User> query = getBaseQuery(record);
//        Query<User> queryCnt = query.copy();
//        List<User> records = query.setFirstRow(ResultUtils.calcPageStart(pageNum, pageSize)).setMaxRows(pageSize).findList();
        PagedList<User> page = query.setFirstRow(ResultUtils.calcPageStart(pageNum, pageSize)).setMaxRows(pageSize).findPagedList();

        return InvokerPage.Page.build(page.getTotalCount(), page.getList());
    }


    private Query<User> getBaseQuery(User record) {
        Query<User> query = User.find.query();
//        query.fetch("canalCluster", "name").setDisableLazyLoading(true);


        Map<String, Object> vm = BeanExtUtils.getDeclaredFieldValueMap(record);
        if (!vm.isEmpty()) {
            vm.forEach((k, v) -> {
                if (CommonUtils.isEmpty(v)) return;
                query.where().eq(k, v);
            });
        }

        return query;
    }
}
