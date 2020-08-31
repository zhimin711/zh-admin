package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.User;
import com.alibaba.otter.canal.admin.service.UserService;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理控制层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/tools")
public class ToolsController {


    /**
     * 加密
     *
     * @param user 用户信息
     * @param env 环境变量
     * @param httpServletRequest httpServletRequest
     * @return 是否成功
     */
    @PutMapping(value = "encrypt")
    public BaseModel<String> encrypt(@RequestBody User user, @PathVariable String env,
                                    HttpServletRequest httpServletRequest) {
        if(StringUtils.isBlank(user.getOldPassword())|| StringUtils.isBlank(user.getPassword())){
            BaseModel<String> model = BaseModel.getInstance(null);
            model.setCode(50014);
            model.setMessage("Invalid token");
            return model;
        }

        System.setProperty("jasypt.encryptor.password", user.getOldPassword());
        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        String pwd = stringEncryptor.encrypt(user.getPassword());
        return BaseModel.getInstance(pwd);
    }

}
