package com.zh.cloud.admin.controller.upms;

import com.ch.result.InvokerPage;
import com.ch.result.PageResult;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.zh.cloud.admin.controller.LoginController;
import com.zh.cloud.admin.model.BaseModel;
import com.zh.cloud.admin.model.upms.Role;
import com.zh.cloud.admin.model.upms.User;
import com.zh.cloud.admin.service.upms.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 解决管理控制层
 *
 * @author zhimin.ma 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param record   条件
     * @param env      环境变量
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return token
     */
    @GetMapping(value = {"/{num:[0-9]+}/{size:[0-9]+}"})
    public Result<Role> page(Role record,
                             @PathVariable(value = "num") int pageNum,
                             @PathVariable(value = "size") int pageSize,
                             @PathVariable String env) {
        InvokerPage.Page<Role> page = roleService.findPage(record, pageNum, pageSize);
        return PageResult.success(page);
    }

    /**
     * 获取信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
    @GetMapping(value = "/{id}")
    public Result<Role> info(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> roleService.find(id));
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PutMapping(value = "")
    public Result<String> update(@RequestBody Role record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            roleService.update(record);
            return "";
        });
    }

    /**
     * 用户退出
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/{id}")
    public Result<String> delete(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(()->{
            roleService.delete(id);
            return "";
        });
    }
}
