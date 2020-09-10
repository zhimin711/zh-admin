package com.zh.cloud.admin.service.upms.impl;

import com.ch.Constants;
import com.ch.NumS;
import com.ch.e.PubError;
import com.ch.pojo.KeyValue;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
import com.ch.utils.SQLUtils;
import com.ch.utils.StringExtUtils;
import com.zh.cloud.admin.model.upms.Department;
import com.zh.cloud.admin.service.upms.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author zhimi
 * @date 2020/9/10 7:47
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Override
    public void save(Department record) {
        record.save();
    }

    @Override
    public void update(Department record) {

        Department orig = Department.find.byId(record.getId());
        if (orig == null) {
            ExceptionUtils._throw(PubError.NOT_EXISTS);
        }
        if (orig.getPid() != null && orig.getPid() == 0) {
            record.setPid(orig.getPid());
            record.setParentId(orig.getParentId());
            record.setParentName(orig.getParentName());
        }
        record.update();
    }

    @Override
    public Department find(Long id) {
        return Department.find.byId(id);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Department> findTree(Department record) {
        List<Department> records = Department.find.query().where().eq("parentId", NumS._0).order().asc("sort").findList();
        if (!records.isEmpty()) {
            findChildren(records);
        }
        return records;
    }

    @Override
    public KeyValue findParentKV(Long pid) {
        Department record = Department.find.byId(pid);
        if (record == null) {
            return new KeyValue();
        }
        if (CommonUtils.isEquals(Constants.DISABLED, record.getParentId())) {
            return new KeyValue(record.getId(), record.getName());
        }
        String key = StringExtUtils.linkStr(Constants.SEPARATOR_2, record.getParentId(), record.getId().toString());
        String name = StringExtUtils.linkStr(Constants.SEPARATOR_2, record.getParentName(), record.getName());
        return new KeyValue(key, name);
    }

    private void findChildren(List<Department> list) {
        if (list.isEmpty()) return;
        list.forEach(r -> {
            String pid2 = StringExtUtils.linkStrIgnoreZero(Constants.SEPARATOR_2, r.getParentId(), r.getId().toString());
            List<Department> subList = Department.find.query().where().like("parentId", SQLUtils.likeSuffix(pid2)).findList();
            if (subList.isEmpty()) return;
            Map<String, List<Department>> subMap = assembleTree(subList);
            r.setChildren(subMap.get(pid2));
            if (r.getChildren() != null) r.getChildren().sort(Comparator.comparing(Department::getSort));
        });
    }

    private Map<String, List<Department>> assembleTree(List<Department> subList) {
        Map<String, List<Department>> subMap = subList.stream().collect(Collectors.groupingBy(Department::getParentId));
        subMap.forEach((k, v) -> v.forEach(r -> {
            r.setChildren(subMap.get(StringExtUtils.linkStr(Constants.SEPARATOR_2, r.getParentId(), r.getId().toString())));
            if (r.getChildren() != null) r.getChildren().sort(Comparator.comparing(Department::getSort));
        }));
        return subMap;
    }
}
