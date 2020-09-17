package com.zh.cloud.admin.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * desc:
 *
 * @author zhimi
 * @date 2020/9/16 22:49
 */
@Data
@ApiModel(value = "User Pojo", description = "用户信息")
public class UserVo {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty(value = "用户帐号")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "性别: 0.女 1.男")
    private Boolean sex;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    private RoleVo role;
}
