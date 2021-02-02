package com.zh.cloud.admin.pojo.tools;

import com.ch.toolkit.UUIDGenerator;
import com.zh.cloud.admin.utils.DatasourceUtils;
import lombok.Data;

/**
 * decs:
 *
 * @author 01370603
 * @date 2021/2/1
 */
@Data
public class MyBatis3Config {

    /**
     * MyBatis3 MyBatis3Simple
     */
    public enum TargetRuntime {
        MYBATIS3, MYBATIS3_SIMPLE
    }

    private TargetRuntime targetRuntime = TargetRuntime.MYBATIS3_SIMPLE;

    private String uid = UUIDGenerator.generate();

    private String outputDir;
    private String outputPackage;

    private DatasourceUtils.DbType dbType = DatasourceUtils.DbType.MYSQL;
    private String dbHost;
    private int dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private String dbTableName;
    private String dbTablePK;

    private boolean enableJavaFormatter = true;
    private boolean enableXmlFormatter = true;

    private boolean enableSerialVersionUID = true;

    private boolean enableCaseInsensitiveLike = true;

    private boolean enableToString = false;

    public String getTargetRuntime() {
        return targetRuntime == TargetRuntime.MYBATIS3 ? "MyBatis3" : "MyBatis3Simple";
    }

    public enum MapperPlugin {
        ZH("com.ch.generator.mybatis.ZHMapperPlugin"), TK("tk.mybatis.mapper.generator.MapperPlugin"), NONE("");
        private final String clazz;

        MapperPlugin(String clazz) {
            this.clazz = clazz;
        }

        public String getClazz() {
            return clazz;
        }
    }

    private MapperPlugin mapperPlugin = MapperPlugin.ZH;

    public boolean isCustomMapperPlugin() {
        return mapperPlugin != MapperPlugin.NONE;
    }

    private String mapperPluginMappers = "tk.mybatis.mapper.common.Mapper";
    private boolean mapperPluginCaseSensitive = true;
    private boolean mapperPluginLombok = true;
    private boolean mapperPluginSwagger = true;
    private boolean mapperPluginNeedsDto = true;
    private boolean mapperPluginLombokDto = true;
    private boolean mapperPluginSwaggerDto = true;

    private boolean commentSuppressDate = false;
    private boolean commentSuppressAllComments = false;
    private boolean commentSuppressJpa = false;
    private String commentAuthor;

    private boolean javaTypeResolver2Int = false;
    private boolean javaTypeResolverForceBigDecimals = false;

    private int javaModelGeneratorRootClassMode = 2;

    public enum JavaClientGeneratorType {
        ANNOTATEDMAPPER, XMLMAPPER
    }

    private JavaClientGeneratorType javaClientGeneratorType = JavaClientGeneratorType.ANNOTATEDMAPPER;
}
