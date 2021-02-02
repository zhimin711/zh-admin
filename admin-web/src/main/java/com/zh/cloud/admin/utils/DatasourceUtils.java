package com.zh.cloud.admin.utils;

import com.ch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * DatasourceUtils
 *
 * @author 01370603
 */
@Slf4j
public class DatasourceUtils extends org.springframework.jdbc.datasource.DataSourceUtils {

    public static final String INFORMATION_SCHEMA = "information_schema";

    private DatasourceUtils() {
    }

    /**
     * jdbc:mysql://localhost:3306 -- jdbc:mysql://112.74.51.162:3306/admin
     * jdbc:oracle:thin:@localhost:1521:XE
     * jdbc:postgresql://localhost:5432/postgres
     * jdbc:sqlite:identifier.sqlite
     */
    public enum DbType {
        MYSQL, ORACLE, POSTGRESQL, SQLITE;

        public static DbType fromValue(Object object) {
            if (!(object instanceof String)) {
                return MYSQL;
            }
            String v = (String) object;
            if (CommonUtils.isEmpty(v)) {
                return MYSQL;
            }
            switch (v.toUpperCase()) {
                case "MYSQL":
                    return MYSQL;
                case "ORACLE":
                    return ORACLE;
                case "POSTGRESQL":
                    return POSTGRESQL;
                case "SQLITE":
                    return SQLITE;
            }
            return MYSQL;
        }
    }

    public static String getDriverClass(DbType type) {
        if (CommonUtils.isEmpty(type)) {
            throw new IllegalArgumentException("ConnType is require!");
        }
        String clazz = "com.mysql.jdbc.Driver";
        switch (type) {
            case MYSQL:
                clazz = "com.mysql.cj.jdbc.Driver";
                break;
            case ORACLE:
                clazz = "oracle.jdbc.driver.OracleDriver";
                break;
            case POSTGRESQL:
                clazz = "com.postgresql.Driver";
                break;
        }
        return clazz;
    }

    public static String getConnUrl(DbType type, String url, Integer port, String database) {
        if (CommonUtils.isEmpty(type)) {
            throw new IllegalArgumentException("ConnType is require!");
        } else if (CommonUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is require!");
        } else if (CommonUtils.isEmpty(port)) {
            throw new IllegalArgumentException("port is require!");
        } else if (CommonUtils.isEmpty(database)) {
            throw new IllegalArgumentException("database is require!");
        }
        String connUrl;
        switch (type) {
            case MYSQL:
                connUrl = "jdbc:mysql://" + url + ":" + port + "/" + database;
                break;
            case ORACLE:
                connUrl = "jdbc:oracle:thin:@" + url + ":" + port + ":" + database;
                break;
            case POSTGRESQL:
                connUrl = "jdbc:postgresql://" + url + ":" + port + "/" + database;
                break;
            default:
                connUrl = "";
        }
        return connUrl;
    }

    public static List<String> getMySQLSchemas(JdbcTemplate jdbcTemplate) {
        String sql = "select SCHEMA_NAME from " + INFORMATION_SCHEMA + ".schemata WHERE SCHEMA_NAME != '" + INFORMATION_SCHEMA + "'";
        return jdbcTemplate.queryForList(sql, String.class);
    }


}
