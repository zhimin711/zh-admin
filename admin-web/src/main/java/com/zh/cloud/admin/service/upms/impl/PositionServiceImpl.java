package com.zh.cloud.admin.service.upms.impl;

import com.ch.result.InvokerPage;
import com.ch.result.ResultUtils;
import com.zh.cloud.admin.model.upms.Position;
import com.zh.cloud.admin.model.upms.Role;
import com.zh.cloud.admin.service.upms.PositionService;
import com.zh.cloud.admin.utils.QueryUtils;
import io.ebean.PagedList;
import io.ebean.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * decs:
 *
 * @author 01370603
 * @date 2020/9/11
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Override
    public void save(Position record) {
        record.save();
    }

    @Override
    public void update(Position record) {
        record.update();
    }

    @Override
    public InvokerPage.Page<Position> findPage(Position record, int pageNum, int pageSize) {
        Query<Position> query = getBaseQuery(record);
        PagedList<Position> page = query.setFirstRow(ResultUtils.calcPageStart(pageNum, pageSize)).setMaxRows(pageSize).findPagedList();

        return InvokerPage.Page.build(page.getTotalCount(), page.getList());
    }

    @Override
    public Position find(Long id) {
        return Position.find.byId(id);
    }

    @Override
    public void delete(Long id) {
//        Position.find.deleteById(id);
    }

    @Override
    public List<Position> findAll() {
        return Position.find.all();
    }


    private Query<Position> getBaseQuery(Position record) {
        Query<Position> query = Position.find.query();
        QueryUtils.eq(query, record);
        return query;
    }
}
