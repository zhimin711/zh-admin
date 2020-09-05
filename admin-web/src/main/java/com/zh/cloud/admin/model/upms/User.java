package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author zhimin.ma
 * 用户信息实体类
 */
@Entity
@Table(name = "st_user")
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="后台用户表")
public class User extends Model {

    public static final UserFinder find = new UserFinder();

    public static class UserFinder extends Finder<Long, User> {

        /**
         * Construct using the default EbeanServer.
         */
        public UserFinder() {
            super(User.class);
        }

    }

//    private String roles;
//    private String introduction;
//    private String avatar;
//    private String name;

    @Transient
    private String oldPassword;

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty(value = "用户帐号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "出生日期")
    private Date birth;

    @ApiModelProperty(value = "性别: 0.女 1.男")
    private Boolean sex;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "用户是否锁定: 0.否 1.是")
    private String locked;

    @ApiModelProperty(value = "过期日期")
    private Date expired;

    @ApiModelProperty(value = "类型: 0.系统 1.普通")
    private String type;

    @ApiModelProperty(value = "状态: 0.禁用 1.启动")
    private String status;

    @ApiModelProperty(value = "登录时间")
    private Date lastLoginAt;

    @ApiModelProperty(value = "用户登录IP地址")
    private String lastLoginIp;

    @ApiModelProperty(value = "当天登录错误次数")
    private Integer errorCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @WhenCreated
    private Date createAt;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @Transient
    private String department;
//    @Transient
//    private List<DepartmentDuty> dutyList;
}
