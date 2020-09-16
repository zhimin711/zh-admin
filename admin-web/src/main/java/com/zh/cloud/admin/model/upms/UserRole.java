package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhimin.ma
 * 用户角色实体类
 */
@Entity
@Table(name = "st_user_role")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(UserRoleKey.class)
public class UserRole extends Model {

    public static final UserRoleFinder find = new UserRoleFinder();

    public static class UserRoleFinder extends Finder<UserRoleKey, UserRole> {

        /**
         * Construct using the default EbeanServer.
         */
        public UserRoleFinder() {
            super(UserRole.class);
        }

    }

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 是否为默认角色：0否 1是
     */
    private String status;

    public UserRole(Long userId, Long roleId, String status) {
        this.userId = userId;
        this.roleId = roleId;
        this.status = status;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private Role role;
}
