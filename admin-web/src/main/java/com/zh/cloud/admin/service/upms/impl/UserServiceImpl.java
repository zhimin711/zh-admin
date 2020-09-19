package com.zh.cloud.admin.service.upms.impl;

import com.ch.Constants;
import com.ch.NumS;
import com.ch.StatusS;
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

import java.util.*;
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
        user.update("realName", "nn:password", "sex", "birth", "email", "mobile", "departmentId", "departmentName", "positionId", "positionName");
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
        QueryUtils.likeAny(query, record, "userId", "username", "realName");
        QueryUtils.likeRight(query, record, "departmentId");
        return query;
    }


    @Override
    public List<Role> findRoles(Long userId) {
        List<UserRole> list = findUserRoles(userId);
        if (list.isEmpty()) return Lists.newArrayList();
        if (list.size() > 1) {
            list.sort(Comparator.comparing(UserRole::getStatus));
        }
        return list.stream().map(UserRole::getRole).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<UserRole> findUserRoles(Long userId) {
        return UserRole.find.query().fetch("role").where().eq("userId", userId).findList();
    }

    /**
     * 角色ID为1是超级管理员且不允许分配
     *
     * @param uid     用户ID
     * @param roleIds 角色Id集合
     */
    @Override
    public void saveRoles(Long uid, List<Long> roleIds) {
        List<UserRole> list = findUserRoles(uid);
        if (CommonUtils.isEmpty(roleIds)) {
            list.forEach(r -> {
                if (CommonUtils.isEquals(r.getRoleId(), Constants.SUPER_ID)) {
                    return;
                }
                r.delete();
            });
            return;
        }
        Map<Long, UserRole> roleMap = list.parallelStream().collect(Collectors.toMap(UserRole::getRoleId, Function.identity()));

        boolean nonDefault = list.isEmpty() || list.parallelStream().noneMatch(e -> CommonUtils.isEquals(StatusS.SELECTED, e.getStatus()));
        for (Long rid : roleIds) {
            roleMap.remove(rid);
            if (CommonUtils.isEquals(rid, Constants.SUPER_ID)) {
                return;
            }
            UserRoleKey userRolekey = new UserRoleKey(uid, rid);
            UserRole ur = UserRole.find.byId(userRolekey);
            if (ur != null) {
                if (nonDefault) {
                    ur.setStatus(StatusS.SELECTED);
                    ur.update("status");
                    nonDefault = false;
                }
                continue;
            }
            if (nonDefault || roleIds.size() == 1) {
                new UserRole(uid, rid, StatusS.SELECTED).save();
                nonDefault = false;
            } else {
                new UserRole(uid, rid, StatusS.UNSELECTED).save();
            }
        }
        if (!roleMap.isEmpty()) roleMap.forEach((k, v) -> {
            if (CommonUtils.isEquals(v.getRoleId(), Constants.SUPER_ID)) {
                return;
            }
            v.delete();
        });
    }

    @Override
    public Role findDefaultRole(Long userId) {
        List<UserRole> records = UserRole.find.query().fetch("role").where().eq("userId", userId).eq("status", StatusS.SELECTED).findList();
        if (CommonUtils.isEmpty(records)) {
            records = this.findUserRoles(userId);
        }
        return CommonUtils.isEmpty(records) ? null : records.get(0).getRole();
    }

}
