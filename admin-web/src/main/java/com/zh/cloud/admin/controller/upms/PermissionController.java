package com.zh.cloud.admin.controller.upms;

import com.ch.Constants;
import com.ch.NumS;
import com.ch.Status;
import com.ch.pojo.VueRecord;
import com.ch.result.InvokerPage;
import com.ch.result.PageResult;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.CommonUtils;
import com.zh.cloud.admin.et.PermissionType;
import com.zh.cloud.admin.model.upms.Permission;
import com.zh.cloud.admin.service.upms.PermissionService;
import com.zh.cloud.admin.utils.VueRecordUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<?> page(Permission record,
                                   @PathVariable(value = "num") int pageNum,
                                   @PathVariable(value = "size") int pageSize,
                                   @PathVariable String env) {
        return ResultUtils.wrapPage(() -> permissionService.findPage(record, pageNum, pageSize));
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
    public Result<?> add(@RequestBody Permission record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
//            if (PermissionType.isCatalog(record.getType()) && CommonUtils.isNotEmpty(record.getCode())) {
//                record.setCode(record.getCode().toUpperCase());
//            }
            if (CommonUtils.isEmpty(record.getParentId())) {
                record.setParentId(NumS._0);
            }
            if (CommonUtils.isEmpty(record.getStatus())) {
                record.setStatus(Constants.ENABLED);
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
    public Result<?> delete(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            permissionService.delete(id);
            return "";
        });
    }


    @ApiOperation(value = "获取权限树", notes = "a.全部 b.按钮 c.目录 m.菜单 (不区分大小写)")
    @GetMapping({"/t/{type}"})
    public Result<?> tree(@PathVariable String type) {
        return ResultUtils.wrapList(() -> {
            List<Permission> records = permissionService.findTreeByTypeAndStatus(type, Status.ALL);
            return VueRecordUtils.convertParentsByType(records, type);
        });
    }
}
