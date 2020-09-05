package com.zh.cloud.admin.utils;

import com.ch.utils.BeanExtUtils;
import com.ch.utils.CommonUtils;
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
}
