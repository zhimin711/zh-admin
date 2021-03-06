package com.zh.cloud.admin.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.config.UnderscoreNamingConvention;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Ebean 配置
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Configuration
public class EbeanConfig {

    @Bean("ebeanServer")
    public Database ebeanServer(DataSource dataSource) {
        DatabaseConfig serverConfig = new DatabaseConfig();
        serverConfig.setDefaultServer(true);
        serverConfig.setNamingConvention(new UnderscoreNamingConvention());
        List<String> packages = new ArrayList<>();
        packages.add("com.zh.cloud.admin.model");
        packages.add("com.zh.cloud.admin.model.upms");
        serverConfig.setPackages(packages);
        serverConfig.setName("ebeanServer");
        serverConfig.setDataSource(dataSource);
        serverConfig.setDatabaseSequenceBatchSize(1);
        serverConfig.setDdlGenerate(false);
        serverConfig.setDdlRun(false);

        return DatabaseFactory.create(serverConfig);
    }
}
