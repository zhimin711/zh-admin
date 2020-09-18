package com.zh.cloud.admin.security;

import com.zh.cloud.admin.pojo.RoleVo;
import lombok.Data;

import java.util.List;

/**
 * desc:权限对象
 *
 * @author zhimin.ma
 * @date 2020/9/18 8:08
 */
@Data
public class PermissionRoles {

    private String url;

    private String method;

    private List<Long> roleIds;

    private List<RoleVo> roles;
}
