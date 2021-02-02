package com.zh.cloud.admin.utils;

import com.ch.utils.CommonUtils;
import com.zh.cloud.admin.pojo.tools.MyBatis3Config;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * decs:
 *
 * @author 01370603
 * @date 2021/2/1
 */
@Slf4j
public class MyBatisGeneratorUtil {

    private MyBatisGeneratorUtil() {
    }

    /**
     * 解析远程 XML 文件
     *
     * @return Document xml 文档
     */
    private static Document loadTemplate() throws DocumentException {
        SAXReader reader = new SAXReader();
        InputStream is = MyBatisGeneratorUtil.class.getClassLoader().getResourceAsStream("templates/generatorConfig.xml");
        return reader.read(is);
    }


    public static Document assemble() throws Exception {
        return assemble(new MyBatis3Config());
    }

    public static Document assemble(MyBatis3Config config) throws Exception {
        Document doc = loadTemplate();
        Element root = doc.getRootElement();

        Element context = root.element("context");
        // MyBatis3 MyBatis3Simple
        context.addAttribute("id", config.getUid());
        context.addAttribute("targetRuntime", config.getTargetRuntime());

        if (config.isEnableJavaFormatter()) {
            addElementProperty(context, "javaFormatter", "org.mybatis.generator.api.dom.DefaultJavaFormatter");
        }
        if (config.isEnableXmlFormatter()) {
            addElementProperty(context, "xmlFormatter", "org.mybatis.generator.api.dom.DefaultXmlFormatter");
        }
        if (config.isEnableSerialVersionUID()) {
            addPlugin(context, "org.mybatis.generator.plugins.SerializablePlugin");
        }
        if (config.isEnableCaseInsensitiveLike()) {
            addPlugin(context, "org.mybatis.generator.plugins.CaseInsensitiveLikePlugin");
        }
        if (config.isEnableToString()) {
            addPlugin(context, "org.mybatis.generator.plugins.ToStringPlugin");
        }

        if (config.isCustomMapperPlugin()) {
            Element plugin = addPlugin(context, config.getMapperPlugin().getClazz());

            addElementProperty(plugin, "mappers", config.getMapperPluginMappers());

            addElementProperty(plugin, "caseSensitive", "" + config.isMapperPluginCaseSensitive());

            if (config.isMapperPluginLombok()) {
                addElementProperty(plugin, "lombok", "@Data");
            }
            addElementProperty(plugin, "swagger", "" + config.isMapperPluginSwagger());

            addElementProperty(plugin, "needsDto", "" + config.isMapperPluginNeedsDto());
            if (config.isMapperPluginLombokDto()) {
                addElementProperty(plugin, "lombokDto", "@Data");
            }
            addElementProperty(plugin, "swaggerDto", "" + config.isMapperPluginSwaggerDto());

        }
        addCommentGenerator(context, config);

        addJdbcConnection(context, "");

        addJavaTypeResolver(context, config);

        addJavaModelGenerator(context, config);

        addSqlMapGenerator(context, "");

        addJavaClientGenerator(context, config);

        addTable(context, config);
        return doc;
    }

    private static void addCommentGenerator(Element element, MyBatis3Config config) {
        Element element2 = element.addElement("commentGenerator").addAttribute("type", "com.ch.generator.mybatis.MyCommentGenerator");

        addElementProperty(element2, "suppressDate", "" + config.isCommentSuppressDate());
        addElementProperty(element2, "suppressAllComments", "" + config.isCommentSuppressAllComments());
        addElementProperty(element2, "suppressJpa", "" + config.isCommentSuppressJpa());
        if (CommonUtils.isNotEmpty(config.getCommentAuthor()))
            addElementProperty(element2, "author", config.getCommentAuthor());
    }

    private static void addJavaTypeResolver(Element element, MyBatis3Config config) {
        Element element2 = element.addElement("javaTypeResolver");

        if (config.isJavaTypeResolver2Int()) {
            element2.addAttribute("type", "com.ch.generator.mybatis.MyJavaTypeResolver");
        } else {
            element2.addAttribute("type", "com.ch.generator.mybatis.MyJavaTypeResolverV1");
        }
        addElementProperty(element2, "forceBigDecimals", "" + config.isJavaTypeResolverForceBigDecimals());
    }

    private static void addJavaModelGenerator(Element element, MyBatis3Config config) {
        Element element2 = element.addElement("javaModelGenerator");
        element2.addAttribute("targetPackage", "${package}.model");
        element2.addAttribute("targetProject", "${src_main_java}");

        if (config.getJavaModelGeneratorRootClassMode() == 1) {
            addElementProperty(element2, "rootClass", "com.ch.mybatis.context.BaseEntity");
        } else if (config.getJavaModelGeneratorRootClassMode() == 2) {
            addElementProperty(element2, "rootClass", "com.ch.mybatis.context.BaseEntityWithStatus");
        }

    }

    private static void addJdbcConnection(Element element, String typeValue) {
        Element element2 = element.addElement("jdbcConnection");
        element2.addAttribute("driverClass", "${driverClass}");
        element2.addAttribute("connectionURL", "${connectionURL}");
        element2.addAttribute("userId", "${userId}");
        element2.addAttribute("password", "${password}");

    }

    private static void addSqlMapGenerator(Element element, String typeValue) {
        Element element2 = element.addElement("sqlMapGenerator");
        element2.addAttribute("targetPackage", "${package}.mapper");
        element2.addAttribute("targetProject", "${src_main_java}");

    }

    private static void addJavaClientGenerator(Element element, MyBatis3Config config) {
        Element element2 = element.addElement("javaClientGenerator");
        element2.addAttribute("targetPackage", "${package}.mapper");
        element2.addAttribute("targetProject", "${src_main_resources}");
        element2.addAttribute("type", config.getJavaClientGeneratorType().name());

    }

    private static Element addPlugin(Element element, String typeValue) {
        return element.addElement("plugin").addAttribute("type", typeValue);
    }

    private static void addTable(Element element, MyBatis3Config config) {
        Element element2 = element.addElement("table");
        element2.addAttribute("tableName", "${tableName}");
        if (CommonUtils.isNotEmpty(config.getDbTablePK())) {
            Element element3 = element2.addElement("generatedKey");
            element3.addAttribute("column", "${tablePk}");
            element3.addAttribute("sqlStatement", "Mysql");
            element3.addAttribute("identity", "true");
        }
    }

    private static void addElementProperty(Element element, String name, String value) {
        element.addElement("property").addAttribute("name", name).addAttribute("value", value);
    }

    public static void generate(String configXml, Properties properties) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(configXml.getBytes());
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(properties, warnings);
        Configuration config = cp.parseConfiguration(is);

        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    public static Properties properties(MyBatis3Config config) {

        Properties properties = new Properties();

        properties.put("driverClass", DatasourceUtils.getDriverClass(config.getDbType()));

//        properties.put("connectionURL", "jdbc:mysql://canalmgr-m.dbsit.sfcloud.local:3306/ch_upms");
        properties.put("connectionURL", DatasourceUtils.getConnUrl(config.getDbType(), config.getDbHost(), config.getDbPort(), config.getDbName()));

        properties.put("userId", config.getDbUsername());
        properties.put("password", config.getDbPassword());

        properties.put("src_main_java", config.getOutputDir());
        properties.put("src_main_resources", config.getOutputDir());
        properties.put("package", config.getOutputPackage());

        properties.put("tableName", config.getDbTableName());
        properties.put("tablePk", config.getDbTablePK());
        return properties;
    }
}
