CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ch_upms` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `ch_upms`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for st_menu
-- ----------------------------
DROP TABLE IF EXISTS `st_menu`;
CREATE TABLE `st_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `PARENT_ID` varchar(80) DEFAULT NULL COMMENT '上级菜单ID',
  `PARENT_NAME` varchar(80) DEFAULT NULL COMMENT '上级菜单ID',
  `NAME` varchar(32) NOT NULL COMMENT '菜单名称',
  `CODE` varchar(64) NOT NULL COMMENT '菜单代码',
  `ICON` varchar(64) DEFAULT NULL COMMENT '图标代码',
  `URL` varchar(150) DEFAULT NULL COMMENT '地址',
  `SORT` int(10) DEFAULT '0' COMMENT '菜单顺序',
  `TYPE` char(1) NOT NULL DEFAULT '2' COMMENT '类型: C.目录 M.菜单 B.按钮)',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '描述',
  `STATUS` char(1) NOT NULL DEFAULT '1' COMMENT '状态: 0.禁用 1.启用',
  `CREATE_BY` varchar(64) DEFAULT NULL COMMENT '创建人',
  `CREATE_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_BY` varchar(64) DEFAULT NULL COMMENT '更新人',
  `UPDATE_AT` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ST_M_CODE` (`CODE`),
  KEY `IDX_ST_M_PID` (`PARENT_ID`),
  KEY `IDX_ST_M_URL` (`URL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统表-菜单';


SET FOREIGN_KEY_CHECKS = 1;
