package com.zh.cloud.admin.model;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import lombok.Data;

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

    @Id
    private Long id;
    private String username;
    private String password;
    private String roles;
    private String introduction;
    private String avatar;
    private String name;
    @WhenCreated
    private Date creationDate;

    @Transient
    private String oldPassword;

}
