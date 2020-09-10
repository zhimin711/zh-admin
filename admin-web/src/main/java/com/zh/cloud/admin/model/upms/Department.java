package com.zh.cloud.admin.model.upms;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author zhimin.ma
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "st_department")
@ApiModel(value = "Department对象", description = "部门表")
public class Department extends Model {
    public static final DepartmentFinder find = new DepartmentFinder();

    public static class DepartmentFinder extends Finder<Long, Department> {

        /**
         * Construct using the default EbeanServer.
         */
        public DepartmentFinder() {
            super(Department.class);
        }

    }

    @ApiModelProperty(value = "部门id")
    @Id
    private Long id;

    @ApiModelProperty(value = "上级部门id")
    private Long pid;

    @ApiModelProperty(value = "上级部门id")
    private String parentId;

    @ApiModelProperty(value = "上级部门名称")
    private String parentName;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门状态（0停用 1正常）")
    private String status;

    @JsonIgnore  //json忽略
    @ApiModelProperty(value = "已删除（0否 1是）")
    private Boolean deleted;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @WhenCreated
    private Date createAt;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @WhenModified
    private Date updateAt;

    @Transient
    List<Department> children;

}
