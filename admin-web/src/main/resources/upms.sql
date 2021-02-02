CREATE DATABASE /*!32312 IF NOT EXISTS */ `ch_upms` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `ch_upms`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for st_user
-- ----------------------------
CREATE TABLE `st_user`
(
    `ID`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `USER_ID`         varchar(64)  NOT NULL COMMENT '用户编号',
    `USERNAME`        varchar(50)  NOT NULL COMMENT '用户帐号',
    `PASSWORD`        varchar(100) NOT NULL COMMENT '密码',
    `REAL_NAME`       varchar(100)          DEFAULT NULL COMMENT '真实姓名',
    `BIRTH`           date                  DEFAULT NULL COMMENT '出生日期',
    `SEX`             tinyint(1)            DEFAULT NULL COMMENT '性别: 0.女 1.男',
    `EMAIL`           varchar(50)           DEFAULT NULL COMMENT '电子邮箱',
    `MOBILE`          char(11)              DEFAULT NULL COMMENT '手机号码',
    `LOCKED`          char(1)      NOT NULL DEFAULT '0' COMMENT '用户是否锁定: 0.否 1.是',
    `EXPIRED`         date                  DEFAULT NULL COMMENT '过期日期',
    `TYPE`            char(1)      NOT NULL DEFAULT '1' COMMENT '类型: 0.系统 1.普通',
    `STATUS`          char(1)      NOT NULL DEFAULT '1' COMMENT '状态: 0.禁用 1.启动',
    `deleted`         tinyint(1)            DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `LAST_LOGIN_AT`   datetime              DEFAULT NULL COMMENT '登录时间',
    `LAST_LOGIN_IP`   varchar(30)           DEFAULT NULL COMMENT '用户登录IP地址',
    `ERROR_COUNT`     int(2)       NOT NULL DEFAULT '0' COMMENT '当天登录错误次数',
    `REMARK`          varchar(100)          DEFAULT NULL COMMENT '备注',
    `CREATE_AT`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `CREATE_BY`       varchar(64)           DEFAULT NULL COMMENT '创建人',
    `UPDATE_AT`       datetime              DEFAULT NULL COMMENT '更新时间',
    `UPDATE_BY`       varchar(64)           DEFAULT NULL COMMENT '更新人',
    `department_id`   varchar(100)          DEFAULT NULL COMMENT '组织ID',
    `position_id`     bigint(20)            DEFAULT NULL COMMENT '职位ID',
    `department_name` varchar(200)          DEFAULT NULL COMMENT '组织名称',
    `position_name`   varchar(200)          DEFAULT NULL COMMENT '职位名称',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_ST_U_USER_ID` (`USER_ID`) USING BTREE,
    UNIQUE KEY `UK_ST_U_USERNAME` (`USERNAME`) USING BTREE,
    UNIQUE KEY `UK_ST_U_EMAIL` (`EMAIL`) USING BTREE,
    KEY `IDX_MOBILE_REAL_NAME` (`MOBILE`, `REAL_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='后台用户表';

-- ----------------------------
-- Table structure for st_role
-- ----------------------------
CREATE TABLE `st_role`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `NAME`        varchar(32) NOT NULL COMMENT '名称',
    `CODE`        varchar(64) NOT NULL COMMENT '代码',
    `TYPE`        char(1)     NOT NULL DEFAULT '2' COMMENT '类型: 1.系统 2.自定义)',
    `DESCRIPTION` varchar(255)         DEFAULT NULL COMMENT '描述',
    `STATUS`      char(1)     NOT NULL DEFAULT '1' COMMENT '状态: 0.禁用 1.启动',
    `deleted`     tinyint(1)           DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `CREATE_BY`   varchar(64)          DEFAULT NULL COMMENT '创建人',
    `CREATE_AT`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`   varchar(64)          DEFAULT NULL COMMENT '更新人',
    `UPDATE_AT`   datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE KEY `UK_ST_R_CODE` (`CODE`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='后台角色表';
-- ----------------------------
-- Table structure for st_menu
-- ----------------------------
DROP TABLE IF EXISTS `st_menu`;
CREATE TABLE `st_menu`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `PARENT_ID`   varchar(80)          DEFAULT NULL COMMENT '上级菜单ID',
    `PARENT_NAME` varchar(180)         DEFAULT NULL COMMENT '上级菜单名称',
    `NAME`        varchar(32) NOT NULL COMMENT '菜单名称',
    `CODE`        varchar(64) NOT NULL COMMENT '菜单代码',
    `TYPE`        char(1)     NOT NULL DEFAULT '2' COMMENT '类型: C.目录 M.菜单 B.按钮)',
    `ICON`        varchar(64)          DEFAULT NULL COMMENT '图标代码',
    `URL`         varchar(150)         DEFAULT NULL COMMENT '地址',
    `METHOD`      varchar(50)          DEFAULT NULL COMMENT '请求方法',
    `SORT`        int(10)              DEFAULT '0' COMMENT '菜单顺序',
--     `REDIRECT` varchar(150) DEFAULT NULL COMMENT '转发地址',
    `HIDDEN`      tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否隐藏(0.否 1.是)',
--     `IS_SYS` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为系统权限(0.否 1.是)',
    `DESCRIPTION` varchar(255)         DEFAULT NULL COMMENT '描述',
    `STATUS`      char(1)     NOT NULL DEFAULT '1' COMMENT '状态: 0.禁用 1.启用',
    `CREATE_BY`   varchar(64)          DEFAULT NULL COMMENT '创建人',
    `CREATE_AT`   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`   varchar(64)          DEFAULT NULL COMMENT '更新人',
    `UPDATE_AT`   datetime             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `UK_ST_M_CODE` (`CODE`),
    KEY `IDX_ST_M_PID` (`PARENT_ID`),
    KEY `IDX_ST_M_URL` (`URL`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='系统表-菜单';
-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists st_department;
create table st_department
(
    id            bigint(20) not null auto_increment comment '部门id',
    pid           bigint(20)          default null comment '上级部门id',
    `parent_id`   varchar(150)        DEFAULT NULL COMMENT '上级部门ids',
    `parent_name` varchar(252)        DEFAULT NULL COMMENT '上级部门名称',
    name          varchar(55)         default '' comment '部门名称',
    sort          int(4)              default 1 comment '显示顺序',
    leader        varchar(20)         default null comment '负责人',
    phone         varchar(11)         default null comment '联系电话',
    email         varchar(50)         default null comment '邮箱',
    status        char(1)             default '0' comment '状态（0停用 1正常）',
    deleted       tinyint(1)          default 0 comment '删除标志（0代表存在 1代表删除）',
    create_by     varchar(64)         default '' comment '创建者',
    create_at     timestamp  not null default CURRENT_TIMESTAMP comment '创建时间',
    update_by     varchar(64)         default '' comment '更新者',
    update_at     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '更新时间',
    primary key (id),
    key idx_pid (`pid`),
    key idx_parent_id (`parent_id`)
) comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
INSERT INTO `st_department`(`id`, `parent_id`, `name`, `sort`, `leader`, `phone`, `email`, `status`, `deleted`,
                            `create_by`,
                            `create_at`, `update_by`, `update_at`)
VALUES (1, '0', '朝华科技', 1, '朝华', '17600807713', NULL, '0', 0, 'sys', '2020-07-03 14:24:05', 'admin',
        '2020-07-03 14:24:15');
-- ----------------------------
-- 3、职位信息表
-- ----------------------------
drop table if exists st_position;
create table st_position
(
    id        bigint(20)  not null auto_increment comment 'ID',
    code      varchar(64) not null comment '职位编码',
    name      varchar(50) not null comment '职位名称',
    sort      int(4)      not null comment '显示顺序',
    remark    varchar(500)         default null comment '备注',
    status    char(1)     not null comment '状态（0停用 1正常）',
    deleted   tinyint(1)           default 0 comment '删除标志（0代表存在 1代表删除）',
    create_by varchar(64)          default '' comment '创建者',
    create_at timestamp   not null default CURRENT_TIMESTAMP comment '创建时间',
    update_by varchar(64)          default '' comment '更新者',
    update_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '更新时间',
    primary key (id),
    key idx_code (`code`)
) comment = '职位信息表';

CREATE TABLE `st_user_role`
(
    `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
    `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
    `STATUS`  char(1)    NOT NULL DEFAULT '0' COMMENT '状态：1.用户默认角色',
    PRIMARY KEY (`USER_ID`, `ROLE_ID`) USING BTREE,
    KEY `IDX_ST_UR_ROLE_ID` (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户角色表';

CREATE TABLE `st_role_menu`
(
    `ROLE_ID`       bigint(20) NOT NULL COMMENT '角色ID',
    `PERMISSION_ID` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`ROLE_ID`, `PERMISSION_ID`) USING BTREE,
    KEY `IDX_ST_RM_MENU_ID` (`PERMISSION_ID`) USING BTREE
) COMMENT ='后台角色菜单表';

CREATE TABLE `st_user_department_position`
(
    `user_id`       bigint(20)   NOT NULL COMMENT '用户ID',
    `department_id` varchar(100) NOT NULL COMMENT '组织ID',
    `position_id`   bigint(20)   NOT NULL COMMENT '职位ID',
    PRIMARY KEY (`user_id`, `department_id`, `position_id`),
    KEY `idx_dept_id` (`department_id`)
) COMMENT ='用户与组织和职位关联表';

SET FOREIGN_KEY_CHECKS = 1;
