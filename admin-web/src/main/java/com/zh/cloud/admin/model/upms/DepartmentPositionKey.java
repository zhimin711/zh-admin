package com.zh.cloud.admin.model.upms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * decs:
 *
 * @author zhimin.ma
 * @date 2020/9/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DepartmentPositionKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long departmentId;

    private Long positionId;
}
