package com.zh.cloud.admin.controller;

import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 *
 * @author zhimin.ma 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/message")
public class MessageController {


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
            return null;
        });
    }
}
