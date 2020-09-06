package com.zh.cloud.admin.service.upms.impl;

import com.ch.Constants;
import com.ch.result.InvokerPage;
import com.ch.result.ResultUtils;
import com.ch.utils.CommonUtils;
import com.ch.utils.SQLUtils;
import com.ch.utils.StringExtUtils;
import com.zh.cloud.admin.model.upms.Permission;
import com.zh.cloud.admin.service.upms.PermissionService;
import com.zh.cloud.admin.utils.QueryUtils;
import io.ebean.PagedList;
import io.ebean.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author zhimi
 * @date 2020/9/6 12:47
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public void save(Permission record) {
        record.save();
    }

    @Override
    public void update(Permission record) {
        record.update();
    }

    @Override
    public InvokerPage.Page<Permission> findPage(Permission record, int pageNum, int pageSize) {
        if (CommonUtils.isEmpty(record.getParentId())) {
            record.setParentId("0");
        }
        Query<Permission> query = getBaseQuery(record);
        PagedList<Permission> page = query.setFirstRow(ResultUtils.calcPageStart(pageNum, pageSize)).setMaxRows(pageSize).findPagedList();

        if (page.getTotalCount() > 0) {
            findChildren(page.getList());
        }
        return InvokerPage.Page.build(page.getTotalCount(), page.getList());
    }

    @Override
    public Permission find(Long id) {
        return Permission.find.byId(id);
    }

    @Override
    public void delete(Long id) {
        Permission.find.deleteById(id);
    }

    private Query<Permission> getBaseQuery(Permission record) {
        Query<Permission> query = Permission.find.query();
        QueryUtils.eq(query, record);
        return query;
    }

    private void findChildren(List<Permission> records) {
        if (records == null || records.isEmpty()) return;
        records.forEach(r -> {
            String pid2 = StringExtUtils.linkStrIgnoreZero(Constants.SEPARATOR_2, r.getParentId(), r.getId().toString());
            List<Permission> subList = Permission.find.query().where().like("parentId", SQLUtils.likeSuffix(pid2)).findList();
            if (subList.isEmpty()) return;
            Map<String, List<Permission>> subMap = assembleTree(subList);
            r.setChildren(subMap.get(pid2));
            if (r.getChildren() != null) r.getChildren().sort(Comparator.comparing(Permission::getSort));
        });
    }


    private Map<String, List<Permission>> assembleTree(List<Permission> subList) {
        Map<String, List<Permission>> subMap = subList.stream().collect(Collectors.groupingBy(Permission::getParentId));
        subMap.forEach((k, v) -> v.forEach(r -> {
            r.setChildren(subMap.get(StringExtUtils.linkStr(Constants.SEPARATOR_2, r.getParentId(), r.getId().toString())));
            if (r.getChildren() != null) r.getChildren().sort(Comparator.comparing(Permission::getSort));
        }));
        return subMap;
    }
}
