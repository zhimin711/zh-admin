package com.zh.cloud.admin.service.upms;

import com.ch.result.InvokerPage;
import com.zh.cloud.admin.model.upms.Role;

import java.util.List;

/**
 *
 */
public interface RoleService {

    void save(Role record);

    void update(Role record);

    InvokerPage.Page<Role> findPage(Role record, int pageNum, int pageSize);

    Role find(Long id);

    void delete(Long id);

    List<Role> findByUserId(Long userId);
}
