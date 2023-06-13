# Slf4j

## slf4j + log4j支持

```xml
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
```

## Slf4j + log4j2支持

```xml
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>		
		<dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.15.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.15.0</version>
        </dependency>
		<!-- 不需要下面的好像也可以 -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.15.0</version>
        </dependency>
```

## 日志输出配置

```properties
#定义主要日志级别，及日志分类
log4j.rootLogger=DEBUG,console,file

#指定console控制台日志输出格式
log4j.appender.console=org.apache.log4j.ConsoleAppender
log4j.appender.console.layout=org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss} [%c]-[%p] %m%n

#指定日志文件输出地址及格式
log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
log4j.appender.file.File =/data/logs/apps/nvwa/nvwa-crontab/info.log
log4j.appender.file.DatePattern = '.'yyyy-MM-dd
log4j.appender.file.MaxBackupIndex=30
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss} [%c]-[%p] %m%n

#指定包或类定义独立日志级别
log4j.logger.java.sql.ResultSet=INFO
log4j.logger.java.sql.Connection=INFO
log4j.logger.java.sql.Statement=INFO
log4j.logger.java.sql.PreparedStatement=INFO
log4j.logger.org.springframework=INFO
log4j.logger.org.mybatis=INFO
log4j.logger.com.iwanvi.amp.dao=INFO
log4j.logger.com.jolbox.bonecp=INFO
log4j.logger.org.apache=INFO
log4j.logger.httpclient=INFO
```
