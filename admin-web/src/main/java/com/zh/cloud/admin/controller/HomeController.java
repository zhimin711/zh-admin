package com.zh.cloud.admin.controller;

import com.ch.e.PubError;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.ExceptionUtils;
import com.zh.cloud.admin.et.PermissionType;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.upms.Permission;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;
import com.zh.cloud.admin.service.upms.RoleService;
import com.zh.cloud.admin.utils.VueRecordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户中心控制层
 *
 * @author zhimin.ma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping(value = "/menus")
    public Result<?> menus(@RequestParam String token, @PathVariable String env) {
        return ResultUtils.wrapList(() -> {
            User user = LoginController.loginUsers.getIfPresent(token);
            if (user == null) {
                ExceptionUtils._throw(PubError.INVALID);
            }

            List<Permission> permissions = roleService.findPermissions(6L);
            return VueRecordUtils.convertTreePermission(permissions, PermissionType.MENU);
        });
    }

    /**
     * 获取用户信息
     *
     * @param token token
     * @param env   环境变量
     * @return 用户信息
     */
    @GetMapping(value = "/permissions")
    public Result<?> permissions(@RequestParam String token, @PathVariable String env) {
        User user = LoginController.loginUsers.getIfPresent(token);

        return null;
    }

}
