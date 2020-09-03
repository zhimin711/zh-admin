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
@RequestMapping("/api/{env}/message")
public class MessageController {

    public static final LoadingCache<String, User> loginUsers = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(key -> null); // 用户登录信息缓存

    @Autowired
    UserService userService;

    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping
    public Result<User> loginMessage(@RequestParam String token, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            return loginUsers.getIfPresent(token);
        });
    }
}
