package com.zh.cloud.admin.service;

import com.ch.result.InvokerPage;
import com.zh.cloud.admin.model.upms.User;

/**
 *
 */
public interface UserService {

    User find4Login(String username, String password);

    void update(User user);

    InvokerPage.Page<User> findPage(User record, int pageNum, int pageSize);
}
