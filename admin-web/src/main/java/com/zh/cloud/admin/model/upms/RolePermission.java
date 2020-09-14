package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author zhimin.ma
 * 角色权限体类
 */
//@Entity
//@Table(name = "st_role_permission")
//@Data
//@EqualsAndHashCode(callSuper = false)
public class RolePermission extends Model {

//    public static final RolePermissionFinder find = new RolePermissionFinder();

    public static class RolePermissionFinder extends Finder<Long, RolePermission> {

        /**
         * Construct using the default EbeanServer.
         */
        public RolePermissionFinder() {
            super(RolePermission.class);
        }

    }

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", updatable = false, insertable = false)
    private Permission permission;
}
