package com.zh.cloud.admin.service.upms.impl;

import com.ch.e.PubError;
import com.ch.result.InvokerPage;
import com.ch.result.ResultUtils;
import com.ch.utils.ExceptionUtils;
import com.google.common.collect.Lists;
import com.zh.cloud.admin.model.upms.Role;
import com.zh.cloud.admin.model.upms.UserRole;
import com.zh.cloud.admin.service.upms.RoleService;
import com.zh.cloud.admin.utils.QueryUtils;
import io.ebean.PagedList;
import io.ebean.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息业务层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    public void save(Role record) {
        Role userTmp = Role.find.query().where().eq("code", record.getCode()).findOne();
        if (userTmp != null) {
            ExceptionUtils._throw(PubError.EXISTS, "角色代码存在！");
        }
        record.save();
    }

    public void update(Role record) {
        Role userTmp = Role.find.query().where().eq("code", record.getCode()).findOne();
        if (userTmp == null) {
            ExceptionUtils._throw(PubError.NOT_EXISTS, "角色代码不存在！");
        }
        record.update("name", "nn:code", "status");
    }

    public InvokerPage.Page<Role> findPage(Role record, int pageNum, int pageSize) {
        Query<Role> query = getBaseQuery(record);
        PagedList<Role> page = query.setFirstRow(ResultUtils.calcPageStart(pageNum, pageSize)).setMaxRows(pageSize).findPagedList();

        return InvokerPage.Page.build(page.getTotalCount(), page.getList());
    }

    @Override
    public Role find(Long id) {
        return Role.find.byId(id);
    }

    @Override
    public void delete(Long id) {
        Role.find.deleteById(id);
    }


    private Query<Role> getBaseQuery(Role record) {
        Query<Role> query = Role.find.query();
        QueryUtils.likeAny(query, record, "code", "name");
        return query;
    }

    @Override
    public List<Role> findAll() {
        return Role.find.all();
    }
}
