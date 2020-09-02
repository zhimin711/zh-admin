package com.zh.cloud.admin.controller;

import com.ch.result.PageResult;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.User;
import com.zh.cloud.admin.service.UserService;
import io.ebean.PagedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理控制层
 *
 * @author zhimin.ma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/user")
public class UserController {

    public static final LoadingCache<String, User> loginUsers = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(key -> null); // 用户登录信息缓存

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param record 账号密码
     * @param env    环境变量
     * @return token
     */
//    @PostMapping(value = "/{page}/{size}")
    @GetMapping(value = {"/{num:[0-9]+}/{size:[0-9]+}"})
    public PageResult<User> page(User record,
                                 @PathVariable(value = "num") int pageNum,
                                 @PathVariable(value = "size") int pageSize,
                                 @PathVariable String env) {
        PagedList<User> pageInfo = userService.findPage(record, pageNum, pageSize);
        return PageResult.success(pageInfo.getTotalCount(), pageInfo.getList());
    }

    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping(value = "/{id}")
    public BaseModel<User> info(@RequestParam String token, @PathVariable String env, @PathVariable Long id) {
        User user = loginUsers.getIfPresent(token);
        if (user != null) {
            return BaseModel.getInstance(user);
        } else {
            BaseModel<User> model = BaseModel.getInstance(null);
            model.setCode(50014);
            model.setMessage("Invalid token");
            return model;
        }
    }

    /**
     * 修改用户信息
     *
     * @param user               用户信息
     * @param env                环境变量
     * @param httpServletRequest httpServletRequest
     * @return 是否成功
     */
    @PutMapping(value = "")
    public BaseModel<String> update(@RequestBody User user, @PathVariable String env,
                                    HttpServletRequest httpServletRequest) {
        userService.update(user);
        String token = (String) httpServletRequest.getAttribute("token");
        loginUsers.put(token, user);
        return BaseModel.getInstance("success");
    }

    /**
     * 用户退出
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping
    public BaseModel<String> delete(@PathVariable String env) {
        return BaseModel.getInstance("success");
    }
}
