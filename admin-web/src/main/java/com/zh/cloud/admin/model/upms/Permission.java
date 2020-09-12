package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台权限表
 * </p>
 *
 * @author zhimin.ma
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "st_menu")
@ApiModel(value="Permission对象", description="后台权限表")
public class Permission extends Model {

    public static final PermissionFinder find = new PermissionFinder();

    public static class PermissionFinder extends Finder<Long, Permission> {

        /**
         * Construct using the default EbeanServer.
         */
        public PermissionFinder() {
            super(Permission.class);
        }

    }

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

    @ApiModelProperty(value = "上级菜单ID")
    private String parentId;

    @ApiModelProperty(value = "上级菜单名称")
    private String parentName;

    @ApiModelProperty(value = "类型: C.目录 M.菜单 B.按钮)")
    private String type;

    @ApiModelProperty(value = "代码")
    @Size(min = 1, max = 100, message = "权限标识长度不能超过100个字符")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "链接地址")
    private String url;

//    @ApiModelProperty(value = "转发地址")
//    private String redirect;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "菜单顺序")
    private Integer sort;

//    @ApiModelProperty(value = "是否隐藏(0.否 1.是)")
//    private Boolean hidden;

//    @ApiModelProperty(value = "是否为系统权限(0.否 1.是)")
//    private Boolean isSys;

//    @ApiModelProperty(value = "请求方法")
//    private String method;

    @ApiModelProperty(value = "状态: 0.禁用 1.启用")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @WhenCreated
    private Date createAt;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @WhenModified
    private Date updateAt;

    @Transient
    List<Permission> children;
}
