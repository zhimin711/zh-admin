package com.zh.cloud.admin.service.upms;

import com.zh.cloud.admin.model.upms.Department;

import java.util.List;

/**
 *
 */
public interface DepartmentService {

    void save(Department record);

    void update(Department record);

    Department find(Long id);

    void delete(Long id);

    List<Department> findTree(Department record);

}
