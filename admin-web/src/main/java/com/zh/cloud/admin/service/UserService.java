package com.zh.cloud.admin.service;

import com.zh.cloud.admin.model.User;

/**
 *
 */
public interface UserService {

    User find4Login(String username, String password);

    void update(User user);
}
