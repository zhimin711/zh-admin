package com.zh.cloud.admin.utils;

import com.ch.Constants;
import com.ch.StatusS;
import com.ch.pojo.VueRecord;
import com.ch.utils.CommonUtils;
import com.ch.utils.StringExtUtils;
import com.google.common.collect.Lists;
import com.zh.cloud.admin.et.PermissionType;
import com.zh.cloud.admin.model.upms.Permission;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 01370603
 */


public class VueRecordUtils {


    public static List<VueRecord> convertParentsByType(List<Permission> records, String type) {
        if (CommonUtils.isEmpty(records)) {
            return Lists.newArrayList();
        }

        if (PermissionType.isMenu(type)) {
            return convertCategory(records);
        }
        return records.stream().map(r -> convertAuthByType(r, type)).collect(Collectors.toList());
    }

    private static List<VueRecord> convertCategory(List<Permission> records) {
        List<VueRecord> categories = Lists.newArrayList();
        records.stream().filter(r -> PermissionType.isCatalog(r.getType())).forEach(r -> {
            VueRecord vueRecord = convertPermission(r);
            categories.add(vueRecord);
            if (r.getChildren() == null || r.getChildren().isEmpty()) {
                return;
            }
            List<Permission> list = r.getChildren().stream().filter(e -> PermissionType.isCatalog(e.getType())).collect(Collectors.toList());
            if (list.isEmpty()) {
                return;
            }
            vueRecord.setChildren(convertCategory(list));
            r.getChildren().forEach(e -> { //三级目录
                if (PermissionType.isCatalog(e.getType())) {
                    VueRecord vueRecord1 = convertPermission(e);
                    vueRecord1.setLabel(r.getName() + " >> " + e.getName());
//                    categories.add(vueRecord1);
                }
            });

        });
        return categories;
    }

    private static VueRecord convertAuthByType(Permission record, String type) {
        VueRecord vueRecord = convertPermission(record);
        if (PermissionType.isButton(type) && !vueRecord.isDisabled()) {
            boolean disabled = CommonUtils.isEquals(record.getType(), Constants.ENABLED) && CommonUtils.isEmpty(record.getChildren());
            vueRecord.setDisabled(disabled);
        }
        if (CommonUtils.isNotEmpty(record.getChildren())) {
            vueRecord.setChildren(convertParentsByType(record.getChildren(), type));
        }
        return vueRecord;
    }


    private static VueRecord convertPermission(Permission record) {
        VueRecord vueRecord = new VueRecord();
        vueRecord.setLabel(record.getName());
        vueRecord.setValue(record.getId().toString());
        vueRecord.setDisabled(!CommonUtils.isEquals(Constants.ENABLED, record.getStatus()));
        return vueRecord;
    }

    public static List<Permission> convertTreePermission(List<Permission> records) {
        Map<String, List<Permission>> permissionMap = records.parallelStream().collect(Collectors.groupingBy(Permission::getParentId));
        permissionMap.forEach((k, v) -> {
            v.sort(Comparator.comparing(Permission::getSort));
            v.forEach(r -> {
                String pid = StringExtUtils.linkStrIgnoreZero(Constants.SEPARATOR_2, r.getParentId(), r.getId().toString());
                if (permissionMap.get(pid) != null) {
                    r.setChildren(permissionMap.get(pid));
                }
            });
        });

        return permissionMap.get(StatusS.BEGIN);
    }

    public static List<VueRecord> convertTree(List<Permission> records) {
        return com.ch.utils.VueRecordUtils.covertIdTree(convertTreePermission(records));
    }
}
