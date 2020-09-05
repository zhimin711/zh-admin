package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * 后台角色表
 * </p>
 *
 * @author zhimin.ma
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "st_role")
@ApiModel(value = "Role对象", description = "后台角色表")
public class Role extends Model {

    public static final RoleFinder find = new RoleFinder();

    public static class RoleFinder extends Finder<Long, Role> {

        /**
         * Construct using the default EbeanServer.
         */
        public RoleFinder() {
            super(Role.class);
        }

    }

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "类型: 1.系统 2.自定义)")
    private String type;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态: 0.禁用 1.启动")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @WhenCreated
    private Date createAt;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;


}
