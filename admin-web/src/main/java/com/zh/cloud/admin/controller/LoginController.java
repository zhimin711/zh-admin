package com.zh.cloud.admin.controller;

import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.User;
import com.zh.cloud.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理控制层
 *
 * @author zhimin.ma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}")
public class LoginController {

    public static final LoadingCache<String, User> loginUsers = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(key -> null); // 用户登录信息缓存

    @Autowired
    UserService userService;

    /**
     * 用户登录
     *
     * @param user 账号密码
     * @param env  环境变量
     * @return token
     */
    @PostMapping(value = "/login")
    public Result<String> login(@RequestBody User user, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            User loginUser = userService.find4Login(user.getUsername(), user.getPassword());
            String token = UUID.randomUUID().toString();
            loginUsers.put(token, loginUser);
            return token;
        });
    }

    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping(value = "/login/user")
    public Result<User> loginUser(@RequestParam String token, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            return loginUsers.getIfPresent(token);
        });
    }
    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping(value = "/login/message")
    public Result<User> loginMessage(@RequestParam String token, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            return loginUsers.getIfPresent(token);
        });
    }

    /**
     * 用户退出
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/logout")
    public BaseModel<String> logout(@PathVariable String env) {
        return BaseModel.getInstance("success");
    }
}
