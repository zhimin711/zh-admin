package com.zh.cloud.admin.service.upms.impl;

import com.ch.Constants;
import com.ch.NumS;
import com.ch.StatusS;
import com.ch.e.PubError;
import com.ch.pojo.KeyValue;
import com.ch.utils.CommonUtils;
import com.ch.utils.ExceptionUtils;
import com.ch.utils.SQLUtils;
import com.ch.utils.StringExtUtils;
import com.zh.cloud.admin.model.upms.*;
import com.zh.cloud.admin.service.upms.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author zhimin.ma
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
        Department record = find(id);
        if (CommonUtils.isEquals(record.getPid(), StatusS.BEGIN)) {
            ExceptionUtils._throw(PubError.NOT_ALLOWED);
        }
        record.delete();
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
        String name = StringExtUtils.linkStr(Constants.SEPARATOR_3, record.getParentName(), record.getName());
        return new KeyValue(key, name);
    }

    @Override
    public List<Position> findPositions(Long id) {
        return findDepartmentPositionList(id).parallelStream().map(DepartmentPosition::getPosition).collect(Collectors.toList());
    }

    private List<DepartmentPosition> findDepartmentPositionList(Long id) {
        return DepartmentPosition.find.query().fetch("position").where().eq("departmentId", id).findList();
    }

    @Override
    public void savePositions(Long id, List<Long> positionIds) {
        List<DepartmentPosition> list = findDepartmentPositionList(id);
        if (CommonUtils.isEmpty(positionIds)) {
            list.forEach(DepartmentPosition::delete);
            return;
        }
        Map<Long, DepartmentPosition> departmentPositionMap = list.parallelStream().collect(Collectors.toMap(DepartmentPosition::getPositionId, Function.identity()));

        for (Long pid : positionIds) {
            departmentPositionMap.remove(pid);
            DepartmentPositionKey departmentPositionKey = new DepartmentPositionKey(id, pid);
            DepartmentPosition ur = DepartmentPosition.find.byId(departmentPositionKey);
            if (ur != null) {
                continue;
            }
            new DepartmentPosition(id, pid).save();
        }
        if (!departmentPositionMap.isEmpty()) departmentPositionMap.forEach((k, v) -> v.delete());
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
