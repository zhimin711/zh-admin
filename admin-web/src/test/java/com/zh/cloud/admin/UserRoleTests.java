package com.zh.cloud.admin;


import com.alibaba.fastjson.JSON;
import com.zh.cloud.admin.model.upms.UserRole;
import com.zh.cloud.admin.model.upms.UserRoleKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zhimin.ma
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZHAdminApplication.class)
@Slf4j
public class UserRoleTests {


    @Test
    public void testUserRoleKey() {
        UserRole r = UserRole.find.byId(new UserRoleKey(1L,7L));
        log.info("{}", JSON.toJSON(r));
    }

    @Test
    public void testFindRoles() {
        List<UserRole> roles = UserRole.find.query().fetch("role").where().eq("userId", 1L).findList();
        roles.forEach(r -> {
            log.info("{}", JSON.toJSON(r));
        });
    }


}
