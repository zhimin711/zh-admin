package com.zh.cloud.admin.utils;

import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.SQLUtils;
import com.ch.utils.StringExtUtils;
import io.ebean.Query;

import java.util.Map;

/**
 * decs:
 *
 * @author 01370603
 * @date 2020/9/5
 */
public class QueryUtils {


    public static <T> void eq(Query<T> query, T record) {
        Map<String, Object> vm = BeanExtUtils.getDeclaredFieldValueMap(record);
        if (!vm.isEmpty()) {
            vm.forEach((k, v) -> {
                if (CommonUtils.isEmpty(v)) return;
                query.where().eq(k, v);
            });
        }
    }

    public static <T> void eq(Query<T> query, T record, String... properties) {
        if (properties == null || properties.length == 0) {
            return;
        }
        for (String property : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, property);
            if (CommonUtils.isEmpty(v)) continue;
            query.where().eq(property, v);
        }
    }

    public static <T> void likeRight(Query<T> query, T record, String... properties) {
        if (properties == null || properties.length == 0) {
            return;
        }
        for (String property : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, property);
            if (v instanceof String) {
                query.where().like(property, SQLUtils.likeSuffix((String) v));
            }
        }
    }

    public static <T> void likeLeft(Query<T> query, T record, String... properties) {
        if (properties == null || properties.length == 0) {
            return;
        }
        for (String property : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, property);
            if (v instanceof String) {
                query.where().like(property, SQLUtils.likePrefix((String) v));
            }
        }
    }

    public static <T> void likeAny(Query<T> query, T record, String... properties) {
        if (properties == null || properties.length == 0) {
            return;
        }
        for (String property : properties) {
            Object v = BeanExtUtils.getValueByProperty(record, property);
            if (v instanceof String) {
                query.where().like(property, SQLUtils.likeAny((String) v));
            }
        }
    }
}
