package com.zh.cloud.admin.service;

import com.ch.result.InvokerPage;
import com.zh.cloud.admin.model.upms.Role;
import com.zh.cloud.admin.model.upms.User;

import java.util.List;

/**
 *
 */
public interface UserService {

    User find4Login(String username, String password);

    void update(User user);

    InvokerPage.Page<User> findPage(User record, int pageNum, int pageSize);

    User find(Long id);

    List<Role> findRoles(Long userId);

    void saveRoles(Long id, List<Long> roleIds);
}
