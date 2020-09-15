package com.zh.cloud.admin.controller.upms;

import com.ch.Constants;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param env 环境变量
     * @param id  用户id
     * @return 用户信息
     */
    @GetMapping(value = "/{id:[0-9]+}")
    public Result<?> info(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> userService.find(id));
    }


    /**
     *  创建信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PostMapping(value = "")
    public Result<?> add(@RequestBody User record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {

            record.setType(Constants.ENABLED);
//            userService.save(record);
            return "";
        });
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @param env  环境变量
     * @param id   id
     * @return 是否成功
     */
    @PutMapping(value = "/{id:[0-9]+}")
    public Result<?> update(@RequestBody User user, @PathVariable String env,
                            @PathVariable Long id) {
        userService.update(user);
        return Result.success();
    }

    /**
     * 用户删除
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/{id}")
    public Result<?> delete(@PathVariable String env, @PathVariable Long id) {
        return Result.failed();
    }


    /**
     * 获取用户角色信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
    @GetMapping(value = "/{id}/roles")
    public Result<?> roles(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapList(() -> userService.findRoles(id));
    }

    /**
     * 获取用户角色信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
    @PutMapping(value = "/{id}/roles")
    public Result<?> roleSave(@PathVariable String env, @PathVariable Long id, @RequestBody List<Long> roleIds) {
        return ResultUtils.wrapFail(() -> {
            userService.saveRoles(id, roleIds);
            return true;
        });
    }
}
