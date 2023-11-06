# 集成
1. 依赖mybatis或spring boot mybatis starter
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
</dependency>
```
2. 依赖数据库驱动，如mysql
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.49</version>
</dependency>
```
3. 定义实体对象
```java
@Data // lombok 工具
public class User {
    private Long id;
    private String name;
    private int age;
}
```
4. 定义Mapper接口
```java
@Repository
public interface UserDao{
    List<User> findByName(String name);
}
```
5. 编写自定义查询接口方法，并编写mapper.xml文件
```java
    List<User> findByName(String name);
```
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dongle.sys.order.dao.mapper.UserDao">

    <select id="findByName" resultType="com.dongle.sys.order.dao.entity.User">
        select id from consumer where `name` = #{name}
    </select>

</mapper>
```
6. **开启Mapper扫描**(**必需**)
```java
@SpringBootApplication
@MapperScan("com.dongle.sys.order.dao") // 单路径
// @MapperScans(@MapperScan(basePackages = {"com.dongle.sys.order.dao"})) // 多路径
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
7. 配置mybatis：mybatis-config.xml（**mybatis自用配置**）
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
8. 配置mybatis-spring
    ```yml
    spring:
        datasource:
            driver-class-name: com.mysql.jdbc.Driver
            username: root
            password: Dongle@123
            url: jdbc:mysql://db.dongle.com:3306/dongle-order?useUnicode=true&characterEncodeing=utf8&autoReconnect=true&useSSL=false
    mybatis:
        type-aliases-package: com.dongle.sys.order.dao.entity  # 实体类所在包
        mapper-locations: classpath*:mapper/*.xml # mapper映射文件地址
    ```
# 代码生成器
1. 依赖/插件
    ```xml
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.9</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.6</version>
                <configuration>
                    <configurationFile>src/main/resources/mybatis/mybatis-generator.xml</configurationFile>
                    <overwrite>true</overwrite>
                    <verbose>false</verbose>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.47</version>
                    </dependency>
                    <dependency>
                        <groupId>com.itfsw</groupId>
                        <artifactId>mybatis-generator-plugin</artifactId>
                        <version>1.3.10</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>

    ```
2. 配置代码生成器配置
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

            <plugin type="com.itfsw.mybatis.generator.plugins.SelectOneByExamplePlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.BatchInsertPlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.ModelColumnPlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.LimitPlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.ModelBuilderPlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.ExampleEnhancedPlugin"/>
            <plugin type="com.itfsw.mybatis.generator.plugins.ExampleTargetPlugin">
                <!-- 修改Example类生成到目标包下 -->
                <property name="targetPackage" value="com.dongle.adx.dao.example"/>
            </plugin>
            <!-- 注释 -->
            <commentGenerator>
                <property name="suppressAllComments" value="true"/><!-- 是否取消注释 -->
                <property name="suppressDate" value="true"/> <!-- 是否生成注释代时间戳-->
            </commentGenerator>

            <!--jdbc-->
            <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                            connectionURL="jdbc:mysql://db.dongle.com/dongle_adx?useUnicode=true&amp;characterEncoding=utf8"
                            userId="root" password="Dongle@123"/>
            <!--实体类model-->
            <javaModelGenerator targetPackage="com.dongle.adx.dao.model" targetProject="src/main/java"/>

            <!--生成mapper.xml配置文件-->
            <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources"/>

            <!--生成dao接口-->
            <javaClientGenerator type="XMLMAPPER" targetPackage="com.dongle.adx.dao.mapper" targetProject="src/main/java"/>
            
            <!--关联需要生成的表及其配置信息-->
            <table tableName="ad_app" domainObjectName="AdApp">
                <generatedKey column="id" sqlStatement="JDBC"/>
            </table>
        </context>
    </generatorConfiguration>
   ```
3. 执行代码生成
   ```bash
   mvn mybatis-generator:generate
   ```