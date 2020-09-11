package com.zh.cloud.admin.service.upms;

import com.ch.result.InvokerPage;
import com.zh.cloud.admin.model.upms.Position;

/**
 *
 */
public interface PositionService {

    void save(Position record);

    void update(Position record);

    InvokerPage.Page<Position> findPage(Position record, int pageNum, int pageSize);

    Position find(Long id);

    void delete(Long id);
}
