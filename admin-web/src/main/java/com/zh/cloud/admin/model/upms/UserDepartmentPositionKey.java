package com.zh.cloud.admin.model.upms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * @author zhimin.ma
 * 用户与组织和职位实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserDepartmentPositionKey {


    private Long userId;

    private Long departmentId;

    private Long positionId;

}
