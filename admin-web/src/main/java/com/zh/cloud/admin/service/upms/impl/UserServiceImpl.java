package com.zh.cloud.admin.service.upms.impl;

import com.ch.NumS;
import com.ch.e.PubError;
import com.ch.result.InvokerPage;
import com.ch.result.ResultUtils;
import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
import com.google.common.collect.Lists;
import com.zh.cloud.admin.common.exception.ServiceException;
import com.zh.cloud.admin.model.upms.Role;
import com.zh.cloud.admin.model.upms.UserRole;
import com.zh.cloud.admin.model.upms.UserRoleKey;
import com.zh.cloud.admin.utils.QueryUtils;
import io.ebean.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public User find(Long id) {
        return User.find.byId(id);
    }


    private Query<User> getBaseQuery(User record) {
        Query<User> query = User.find.query();
        QueryUtils.eq(query, record);

        return query;
    }


    @Override
    public List<Role> findRoles(Long userId) {
        List<UserRole> list = findUserRoles(userId);
        if (list.isEmpty()) return Lists.newArrayList();
        return list.stream().map(UserRole::getRole).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<UserRole> findUserRoles(Long userId) {
        return UserRole.find.query().fetch("role").where().eq("userId", userId).findList();
    }

    @Override
    public void saveRoles(Long uid, List<Long> roleIds) {
        List<UserRole> list = findUserRoles(uid);
        if (CommonUtils.isEmpty(roleIds)) {
            list.forEach(UserRole::delete);
            return;
        }
        Map<Long, UserRole> roleMap = list.parallelStream().collect(Collectors.toMap(UserRole::getRoleId, Function.identity()));

        for (Long rid : roleIds) {
            roleMap.remove(rid);
            UserRoleKey userRolekey = new UserRoleKey(uid, rid);
            UserRole ur = UserRole.find.byId(userRolekey);
            if (ur != null) {
                continue;
            }
            new UserRole(uid, rid, NumS._0).save();
        }
        if (!roleMap.isEmpty()) roleMap.forEach((k, v) -> v.delete());
    }

}
