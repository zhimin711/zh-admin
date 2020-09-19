package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zhimin.ma
 * 用户与组织和职位实体类
 */
@Entity
@Table(name = "st_department_position")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(DepartmentPositionKey.class)
public class DepartmentPosition extends Model {

    public static final DepartmentPositionFinder find = new DepartmentPositionFinder();

    public static class DepartmentPositionFinder extends Finder<DepartmentPositionKey, DepartmentPosition> {

        /**
         * Construct using the default EbeanServer.
         */
        public DepartmentPositionFinder() {
            super(DepartmentPosition.class);
        }

    }

    @Id
    @Column(name = "department_id")
    private Long departmentId;
    @Id
    @Column(name = "position_id")
    private Long positionId;

    public DepartmentPosition(Long departmentId, Long positionId) {
        this.departmentId = departmentId;
        this.positionId = positionId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", updatable = false, insertable = false)
    private Department department;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", updatable = false, insertable = false)
    private Position position;
}
