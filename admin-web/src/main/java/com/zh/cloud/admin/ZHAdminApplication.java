package com.zh.cloud.admin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动入口
 *
 * @author zhimin.ma @ 2020-10-20
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEncryptableProperties
public class ZHAdminApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZHAdminApplication.class);
//        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
