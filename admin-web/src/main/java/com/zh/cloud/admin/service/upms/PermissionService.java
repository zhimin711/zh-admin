package com.zh.cloud.admin.service.upms;

import com.ch.result.InvokerPage;
import com.zh.cloud.admin.model.upms.Permission;

/**
 *
 */
public interface PermissionService {

    void save(Permission record);

    void update(Permission record);

    InvokerPage.Page<Permission> findPage(Permission record, int pageNum, int pageSize);

    Permission find(Long id);

    void delete(Long id);
}
