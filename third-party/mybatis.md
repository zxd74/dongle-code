# generator
[官网: http://mybatis.org/generator/index.html]

* `generatorConfiguration`
  * `context` 数据库配置
    * `jdbcConnection` 数据库连接
    * `javaModelGenerator` 实体目录
    * `sqlMapGenerator` Mapper目录
    * `javaClientGenerator` Dao接口目录
    * `table` 表配置

1, mybatis-config.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--iBatis 和 MyBatis 的全局配置文件使用不同的 DTD 约束，在将应用由
iBatis 升级至 MyBatis 时需要注意（两者的映射文件 DTD 约束也不相同）-->
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- 全局映射器启用缓存 -->
        <setting name="cacheEnabled" value="true" />
        <!-- 查询时，关闭关联对象即时加载以提高性能 -->
        <setting name="lazyLoadingEnabled" value="false" />
        <!-- 设置关联对象加载的形态，此处为按需加载字段(加载字段由SQL指 定)，不会加载关联表的所有字段，以提高性能 -->
        <setting name="aggressiveLazyLoading" value="false" />
        <!-- 对于未知的SQL查询，允许返回不同的结果集以达到通用的效果 -->
        <setting name="multipleResultSetsEnabled" value="true" />
        <!-- 允许使用列标签代替列名 -->
        <setting name="useColumnLabel" value="true" />
        <!-- 允许使用自定义的主键值(比如由程序生成的UUID 32位编码作为键值)，数据表的PK生成策略将被覆盖 -->
        <setting name="useGeneratedKeys" value="true" />
        <!-- 给予被嵌套的resultMap以字段-属性的映射支持 -->
        <setting name="autoMappingBehavior" value="FULL" />
        <!-- 对于批量更新操作缓存SQL以提高性能 -->
        <setting name="defaultExecutorType" value="REUSE" />
        <!-- 数据库超过25000秒仍未响应则超时 -->
        <setting name="defaultStatementTimeout" value="25000" />
        <!-- 集成log4j2 -->
		<!-- <setting name="logImpl" value="LOG4J2" /> -->
    </settings>
</configuration>
```
1. mybatis-generator.xml
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--执行generator插件生成文件的命令： call mvn mybatis-generator:generate -e -->
    <!-- 引入配置文件 -->
    <!--<properties resource="mybatis.properties"/>-->
    <!--classPathEntry:数据库的JDBC驱动,换成你自己的驱动位置 可选 -->
    <!--<classPathEntry location="D:\generator_mybatis\mysql-connector-java-5.1.24-bin.jar" /> -->

    <!-- 一个数据库一个context -->
    <!--defaultModelType="flat" 大数据字段，不分表 -->
    <context id="MysqlTables" targetRuntime="MyBatis3" defaultModelType="flat">
        <!-- 关键字列，增加分隔符处理       -->
        <property name="autoDelimitKeywords" value="true"/>
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!-- 代码生成插件 com.itfsw:mmybatis-generator-plugin-->
        <!-- <plugin type="com.itfsw.mybatis.generator.plugins.SelectOneByExamplePlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.BatchInsertPlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.ModelColumnPlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.LimitPlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.ModelBuilderPlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.ExampleEnhancedPlugin"/>
        <plugin type="com.itfsw.mybatis.generator.plugins.ExampleTargetPlugin">
            <property name="targetPackage" value="com.dongle.dao.model"/>
        </plugin>  -->

        <!-- 注释 -->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/><!-- 是否取消注释 -->
            <property name="suppressDate" value="true"/> <!-- 是否生成注释代时间戳-->
            <property name="addRemarkComments" value="true"/> <!--增加表和列注释-->
        </commentGenerator>

        <!--jdbc-->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://db.dongle.com/data?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false"
                        userId="root" password="Dongle@123"/>
        
        <!--实体类model-->
        <javaModelGenerator targetPackage="com.dongle.dao.model" targetProject="src/main/java" />
        <!--生成mapper.xml配置文件-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources" />
        <!--生成dao接口-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.dongle.dao" targetProject="src/main/java" />
        
        <!--对应数据库表-->
        <table tableName="user" schema="dongle" domainObjectName="User"/>
        <table tableName="data" schema="dongle" domainObjectName="Data">
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>
        <!--为所有表都执行generator-->
        <table tableName="%"> 
            <!--生成的model实体的属性，使用实际的表列名作为实体类的属性名，满足驼峰命名法-->
            <property name="useActualColumnNames" value="true" />
        </table>
    </context>
</generatorConfiguration>
```
2. 执行generator，以maven为例
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.4.2</version>
                <dependencies>
                    <dependency>
                        <!-- 导入对应数据库驱动器 -->
                        <groupId>com.mysql</groupId>
                        <artifactId>mysql-connector-j</artifactId>
                        <version>${mysql.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

# 自定义插件（java+maven）
* 通过项目本身运行mybatis generator，只能使用java的`public static void main(String[] args)`
* 通过maven插件方式运行，但需要自定义插件单独作为外院依赖项才能通过命令执行

```xml
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>${mybatis.version}</version>
</dependency>
```

## CommentGenerator
```java
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * @author Dongle
 * @desc
 * @since 2023/9/11 011 15:57
 */
public class MyCommonGenerator implements CommentGenerator {


    public static void main(String[] args) {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File(MyCommonGenerator.class.getClassLoader().getResource("mybatis-generator.xml").getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        try {
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Properties properties;

    private boolean suppressDate;

    private boolean suppressAllComments;

    /**
     * If suppressAllComments is true, this option is ignored.
     */
    private boolean addRemarkComments;

    private SimpleDateFormat dateFormat;

    public MyCommonGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
        addRemarkComments = false;
    }

    /**
     * 默认配置
     *
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));

        addRemarkComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));

        String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
        if (StringUtility.stringHasValue(dateFormatString)) {
            dateFormat = new SimpleDateFormat(dateFormatString);
        }
    }

    /**
     * 实体类添加的注释
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass,
                                     IntrospectedTable introspectedTable) {
        if (suppressAllComments || !addRemarkComments) {
            return;
        }

        topLevelClass.addJavaDocLine("/**"); //$NON-NLS-1$
        String remarks = introspectedTable.getRemarks();
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));  //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" * " + remarkLine);  //$NON-NLS-1$
            }
        }
        topLevelClass.addJavaDocLine(" * @author Dongle");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable().toString());
        topLevelClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    /**
     * 实体类的属性注释，数据库中自定义注释
     *
     * @param field
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addFieldComment(Field field,
                                IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        // 有注释才增加注释设置，否则不增加
        String remarks = introspectedColumn.getRemarks();
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            field.addJavaDocLine("/**"); //$NON-NLS-1$
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));  //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);  //$NON-NLS-1$
            }
            field.addJavaDocLine(" */"); //$NON-NLS-1$
        }
    }
}
```