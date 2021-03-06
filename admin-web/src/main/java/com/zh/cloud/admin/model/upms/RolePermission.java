package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhimin.ma
 * 角色权限体类
 */
@Entity
@Table(name = "st_role_menu")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(RolePermissionKey.class)
public class RolePermission extends Model {

    public static final RolePermissionFinder find = new RolePermissionFinder();

    public static class RolePermissionFinder extends Finder<RolePermissionKey, RolePermission> {

        /**
         * Construct using the default EbeanServer.
         */
        public RolePermissionFinder() {
            super(RolePermission.class);
        }

    }

    @Id
//    @Column(name = "role_id")
    private Long roleId;

    @Id
//    @Column(name = "permission_id")
    private Long permissionId;

    public RolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", updatable = false, insertable = false)
    private Permission permission;
}
