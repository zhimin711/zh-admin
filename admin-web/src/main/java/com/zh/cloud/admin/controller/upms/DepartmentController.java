package com.zh.cloud.admin.controller.upms;

import com.alibaba.fastjson.JSONObject;
import com.ch.Constants;
import com.ch.e.PubError;
import com.ch.pojo.KeyValue;
import com.ch.pojo.VueRecord;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
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
    public Result<?> tree(Department record,
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
    @GetMapping(value = "/{id:[0-9]+}")
    public Result<?> detail(@PathVariable String env, @PathVariable Long id) {
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
    public Result<?> add(@RequestBody Department record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            checkAndFill(record);
            departmentService.save(record);
            return "";
        });
    }

    private void checkAndFill(Department record) {
        if (CommonUtils.isEmpty(record.getPid())) {
            ExceptionUtils._throw(PubError.NON_NULL, "上级ID不能为空！");
        }
        KeyValue parentKv = departmentService.findParentKV(record.getPid());
        record.setParentId(parentKv.getKey());
        record.setParentName(parentKv.getValue());
        if (CommonUtils.isEmpty(record.getStatus())) {
            record.setStatus(Constants.ENABLED);
        }
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/{id:[0-9]+}")
    public Result<?> edit(@RequestBody Department record, @PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            checkAndFill(record);
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
    @DeleteMapping(value = "/{id:[0-9]+}")
    public Result<?> delete(@PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            departmentService.delete(id);
            return "";
        });
    }


    @ApiOperation(value = "获取树", notes = "树")
    @GetMapping({"/t2/{type}"})
    public Result<?> tree(@PathVariable String type) {
        return ResultUtils.wrapList(() -> {
            List<Department> list = departmentService.findTree(null);
            if("1".equals(type)) {
                List<JSONObject> objList = VueRecordUtils.jsonIdLabelTreeByIdAndName(list);
                objList.forEach(r -> r.put("isRoot", true));
                return objList;
            }
            return VueRecordUtils.jsonValueLabelTreeByIdAndName(list);
        });
    }
}
