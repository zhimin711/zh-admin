package com.zh.cloud.admin.controller.upms;

import com.ch.Constants;
import com.ch.pojo.VueRecord;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.VueRecordUtils;
import com.zh.cloud.admin.model.upms.Department;
import com.zh.cloud.admin.service.upms.DepartmentService;
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
@RequestMapping("/api/{env}/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 分页查询
     *
     * @param record 条件
     * @param env    环境变量
     * @return token
     */
    @GetMapping(value = {"/t/"})
    public Result<Department> tree(Department record,
                                   @PathVariable String env) {
        List<Department> list = departmentService.findTree(record);
        return Result.success(list);
    }

    /**
     * 获取详细信息
     *
     * @param env 环境变量
     * @param id  主键
     * @return 信息
     */
//    @GetMapping(value = "/{id}")
    public Result<Department> detail(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> departmentService.find(id));
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PostMapping(value = "")
    public Result<String> add(@RequestBody Department record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
//            if (DepartmentType.isCatalog(record.getType()) && CommonUtils.isNotEmpty(record.getCode())) {
//                record.setCode(record.getCode().toUpperCase());
//            }
            if (CommonUtils.isEmpty(record.getStatus())) {
                record.setStatus(Constants.ENABLED);
            }
//            record.setType(Constants.ENABLED);
            departmentService.save(record);
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
//    @PutMapping(value = "/{id}")
    public Result<String> edit(@RequestBody Department record, @PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            departmentService.update(record);
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
            departmentService.delete(id);
            return "";
        });
    }


    @ApiOperation(value = "获取权限树", notes = "a.全部 b.按钮 c.目录 m.菜单 (不区分大小写)")
    @GetMapping({"/t2/"})
    public Result<VueRecord> tree() {
        return ResultUtils.wrapList(() -> {
            List<Department> list = departmentService.findTree(null);
            return VueRecordUtils.covertIdTree(list);
        });
    }
}
