package com.zh.cloud.admin.service.upms;

import com.ch.Status;
import com.ch.result.InvokerPage;
import com.zh.cloud.admin.et.PermissionType;
import com.zh.cloud.admin.model.upms.Permission;

import java.util.List;

/**
 *
 */
public interface PermissionService {

    void save(Permission record);

    void update(Permission record);

    InvokerPage.Page<Permission> findPage(Permission record, int pageNum, int pageSize);

    Permission find(Long id);

    void delete(Long id);

    /**
     * A C M B
     * @param type 类型
     * @return tree
     */
    List<Permission> findTreeByTypeAndStatus(String type, Status status);

    List<Permission> findAll();

    List<Permission> findAllByType(PermissionType permissionType);

    List<Long> findRoleIds(Long id);
}
