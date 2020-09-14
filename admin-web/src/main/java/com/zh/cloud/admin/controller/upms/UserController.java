package com.zh.cloud.admin.controller.upms;

import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.zh.cloud.admin.controller.LoginController;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;
import com.zh.cloud.admin.service.upms.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理控制层
 *
 * @author zhimin.ma 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户分页查询
     *
     * @param record   用户信息
     * @param env      环境变量
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return token
     */
    @GetMapping(value = {"/{num:[0-9]+}/{size:[0-9]+}"})
    public Result<?> page(User record,
                          @PathVariable(value = "num") int pageNum,
                          @PathVariable(value = "size") int pageSize,
                          @PathVariable String env) {
        return ResultUtils.wrapPage(() -> userService.findPage(record, pageNum, pageSize));
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
        User user = LoginController.loginUsers.getIfPresent(token);
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
        LoginController.loginUsers.put(token, user);
        return BaseModel.getInstance("success");
    }

    /**
     * 用户删除
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/{id}")
    public Result<String> delete(@PathVariable String env, @PathVariable Long id) {
        return Result.failed();
    }


    /**
     * 获取详细信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
    @GetMapping(value = "/{id}/roles")
    public Result<?> roles(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapList(() -> roleService.findByUserId(id));
    }
}
