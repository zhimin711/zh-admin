package com.zh.cloud.admin.controller.upms;

import com.ch.Constants;
import com.ch.result.InvokerPage;
import com.ch.result.PageResult;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.CommonUtils;
import com.zh.cloud.admin.model.upms.Permission;
import com.zh.cloud.admin.service.upms.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理控制层
 *
 * @author zhimin.ma 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

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
    public Result<Permission> page(Permission record,
                             @PathVariable(value = "num") int pageNum,
                             @PathVariable(value = "size") int pageSize,
                             @PathVariable String env) {
        InvokerPage.Page<Permission> page = permissionService.findPage(record, pageNum, pageSize);
        return PageResult.success(page);
    }

    /**
     * 获取详细信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
    @GetMapping(value = "/{id}")
    public Result<Permission> detail(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> permissionService.find(id));
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PostMapping(value = "")
    public Result<String> add(@RequestBody Permission record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            if (CommonUtils.isNotEmpty(record.getCode())) {
                record.setCode(record.getCode().toUpperCase());
            }
//            record.setType(Constants.ENABLED);
            permissionService.save(record);
            return "";
        });
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/{id}")
    public Result<String> edit(@RequestBody Permission record, @PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            permissionService.update(record);
            return "";
        });
    }

    /**
     * 删除
     *
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/{id}")
    public Result<String> delete(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            permissionService.delete(id);
            return "";
        });
    }
}
