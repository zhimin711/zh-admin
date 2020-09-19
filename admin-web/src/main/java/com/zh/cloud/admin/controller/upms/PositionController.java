package com.zh.cloud.admin.controller.upms;

import com.ch.Constants;
import com.ch.result.Result;
import com.ch.result.ResultUtils;
import com.ch.utils.VueRecordUtils;
import com.zh.cloud.admin.model.upms.Position;
import com.zh.cloud.admin.service.upms.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制层
 *
 * @author zhimin.ma 2020-09-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

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
    public Result<?> page(Position record,
                          @PathVariable(value = "num") int pageNum,
                          @PathVariable(value = "size") int pageSize,
                          @PathVariable String env) {
        return ResultUtils.wrapPage(() -> positionService.findPage(record, pageNum, pageSize));
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
        return ResultUtils.wrapFail(() -> positionService.find(id));
    }

    /**
     * 修改信息
     *
     * @param record 信息
     * @param env    环境变量
     * @return 是否成功
     */
    @PostMapping(value = "")
    public Result<?> add(@RequestBody Position record, @PathVariable String env) {
        return ResultUtils.wrapFail(() -> {
            record.setStatus(Constants.ENABLED);
            positionService.save(record);
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
    @PutMapping(value = "/{id:[0-9]+}")
    public Result<?> edit(@RequestBody Position record, @PathVariable String env, @PathVariable Long id) {
        return ResultUtils.wrapFail(() -> {
            positionService.update(record);
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
            positionService.delete(id);
            return "";
        });
    }
    /**
     * 获取所有职位
     *
     * @param env 环境变量
     * @return 职位
     */
    @GetMapping(value = "/all/")
    public Result<?> all(@PathVariable String env) {
        return ResultUtils.wrapList(() -> {
            List<Position> records = positionService.findAll();
            return VueRecordUtils.jsonKeyLabelTreeByIdAndName(records);
        });
    }
}
