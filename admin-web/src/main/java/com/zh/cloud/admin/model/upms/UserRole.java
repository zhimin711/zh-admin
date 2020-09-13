package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author zhimin.ma
 * 用户角色实体类
 */
@Entity
@Table(name = "st_user_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRole extends Model {

    public static final UserRoleFinder find = new UserRoleFinder();

    public static class UserRoleFinder extends Finder<Long, UserRole> {

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

    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private Role role;
}
