package com.zh.cloud.admin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * desc:
 *
 * @author zhimi
 * @date 2020/9/16 22:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Role Pojo", description = "角色信息")
public class RoleVo {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "代码")
    private String code;
}
