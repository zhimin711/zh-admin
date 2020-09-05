package com.zh.cloud.admin.controller;

import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理控制层
 *
 * @author zhimin.ma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/profile")
public class ProfileController {

    @Autowired
    UserService userService;


    /**
     * 修改个人信息
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
        LoginController.loginUsers.put(token, user);
        return BaseModel.getInstance("success");
    }

}
