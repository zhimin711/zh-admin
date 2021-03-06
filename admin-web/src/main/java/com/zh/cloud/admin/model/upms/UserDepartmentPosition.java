package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author zhimin.ma
 * 用户与组织和职位实体类
 */
@Entity
@Table(name = "st_user_department_position")
@Data
@EqualsAndHashCode(callSuper = false)
@IdClass(UserDepartmentPositionKey.class)
public class UserDepartmentPosition extends Model {

    public static final UserDepartmentPositionFinder find = new UserDepartmentPositionFinder();

    public static class UserDepartmentPositionFinder extends Finder<UserDepartmentPositionKey, UserDepartmentPosition> {

        /**
         * Construct using the default EbeanServer.
         */
        public UserDepartmentPositionFinder() {
            super(UserDepartmentPosition.class);
        }

    }

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "position_id")
    private Long positionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", updatable = false, insertable = false)
    private Position position;
}
