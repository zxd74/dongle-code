# 注解定时
spring boot无需添加依赖，自动注解
```java
@Component
public class Job {

    @Scheduled(cron = "*/10 * * * * *")
    public void checkJob(){
        // JOB CONTENT
    }
}
```
需要开启Schedule才可识别任务注解，在Application或Shedule配置类注解@EnableScheduling
```java
@SpringBootApplication
@EnableScheduling
public class DongleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleApplication.class, args);
    }

}
```
## 注意
### 注解方式Shedule排队执行
通过注解@Sheduled执行时排队执行的，即上一个Shedule执行完才会执行下一个，因为默认Shedule的执行线程池只有一个，无法多个并发执行，可修改线程池大小
```java
@Configuration
@EnableScheduling
public class ScheduleConfigure {
    // 重新定义bean：shutdown
    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(10);
    }
}
```
spring默认不允许重新定义bean，需要更改配置:
```properties
# 默认允许修改重载bean
spring.main.allow-bean-definition-overriding=true
```
# 配置定时
注解方式存在默认线程池限制，可通过配置文件直接配置，即不适用注解方式启动Shedule
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:task="http://www.springframework.org/schema/task"
	xsi:schemaLocation="http://www.springframework.org/schema/beans   
http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
http://www.springframework.org/schema/task 
http://www.springframework.org/schema/task/spring-task-3.0.xsd">

    <!-- 定义任务池 -->
	<task:scheduled-tasks>
		<!-- 每个任务定义一个task；需要注意ref关联的必须是已注册bean，method关联bean对象中的方法，cron代表时间表达式 -->
		<task:scheduled ref="job" method="checkJob" cron="0 10 * * * ?" />
	</task:scheduled-tasks>
	
</beans>
```
```java
@Component("job")
public class Job {

    @Scheduled(cron = "*/10 * * * * *")
    public void checkJob(){
        // JOB CONTENT
    }
}
```
## 注意
需要将配置在启动时引入
```java
@SpringBootApplication
@ImportResource({"spring/applicationContext.xml"})
public class DongleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleApplication.class, args);
    }

}
```