package com.zh.cloud.admin.model.upms;

import io.ebean.annotation.Identity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * decs:
 *
 * @author 01370603
 * @date 2020/9/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserRoleKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;
}
