# 依赖

## 基本依赖

org.springframework：

1. spring-beans
2. spring-core
3. spring-context-support
4. spring-context

## web依赖

org.springframework：

1. spring-webmv  MVC接口
2. spring-web  web服务
3. spring-aop  AOP拦截

## 数据依赖

org.springframework：

1. spring-jdbc 数据库访问
2. spring-tx  事务处理

# 配置

## 基本配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context-3.0.xsd">
    <!--包(组件)扫描配置-->
	<context:component-scan base-package="com.iwanvi.freebook.ad"></context:component-scan>
</beans>
```

## MVC配置

```xml
xmlns:mvc="http://www.springframework.org/schema/mvc"
xsi:schemaLocation="http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd"

    <!--MVC注解驱动，配置处理器适配器-->
	<mvc:annotation-driven/>
	<!--配置拦截器-->
	<mvc:interceptors>
		<!--登录拦截器-->
		<mvc:interceptor>
			<mvc:mapping path="/**"/>
			<mvc:exclude-mapping path="/index"/>
			<bean class="com.iwanvi.freebook.ad.aop.LoginInterceptor"/>
		</mvc:interceptor>
	</mvc:interceptors>
```

提示：MVC接口需要使用@Controller或@RestController等注解

## 任务配置

```xml
xmlns:task="http://www.springframework.org/schema/task"
xsi:schemaLocation="http://www.springframework.org/schema/task
    		http://www.springframework.org/schema/task/spring-task.xsd"

	<!--定时任务注解驱动-->
	<task:annotation-driven />
	<!-- 指定定时任务 -->
	<task:annotation-driven scheduler="myScheduler"/>
	<task:executor id="executor" pool-size="3" />
	<task:scheduler id="myScheduler" pool-size="5"/>
```

提示：任务类需要配置@Component等注解


# 自定义处理

## 跨域处理
```java
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSConfig implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html; charset=utf-8");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,token");
        chain.doFilter(request,response);
    }
}
```

## 异常统一处理

```java
import com.ad.adx.manage.model.AdmResponse;
import com.ad.adx.manage.utils.AdmException;
import com.ad.adx.manage.utils.AdmResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AdmExceptionHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger("AdmExceptionHandler");

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AdmResponse exceptionHandler(HttpServletRequest req,Exception ex){
        LOGGER.error(ex.getMessage(),ex);
        if (ex instanceof AdmException){
            return AdmResponseUtil.error(ex.getMessage());
        }else{
            return AdmResponseUtil.error("请求异常，联系技术排查！");
        }
    }
}
```

## 自定义Starter模块
1. 引入springboot的configuration模块processor和autoconfigure
2. 创建功能模块
3. 配置Configuration自动配置类，关联功能模块类
4. 创建Spring.factories文件，配置EnableAutoConfiguration，添加功能模块自动配置类信息
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import(HelloWorldConfiguration.class)
@Import(HelloWorldImportSelector.class)
public @interface EnableHelloWorld {
}
```
```java
@Configuration  // 模式注解装配
@EnableHelloWorld // @Enable自定义装配
@ConditionalOnSystemProperty(name = "user.name",value="Dongle") // 条件装配
public class HelloWorldAutoConfiguration {

}
```
```java
public class HelloWorldImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{HelloWorldConfiguration.class.getName()};
    }
}
```
```java
@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld(){
        return "Hello World";
    }
}
```
```properties
# spring.factories 文件配置EnableAutoConfiguration
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.dongle.spring.boot.configuration.HelloWorldAutoConfiguration
```


# 模块
## AOP
1. 添加aop启动块依赖(aspectj)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
2. 开启AOP代理
   * 通过注解@EnableAspectJAutoProxy开启
   * 通过配置spring.aop.auto=true开启
3. 创建AOP类，并使用@Aspect注解
```java
@Aspect
@Component
public class WebAop {

}
```
4. 定义切入点方法：execution，within
```java
    @Pointcut("within(com.dongle.spring.demo..*)")  // 切入点
    public void readPointcut(){}

    @Pointcut("execution(public * *(..))")
    public void readPointcut1(){}

    @Pointcut("readPointcut() && readPointcut1()")
    public void readPointcut2(){}
```
1. 配置通知类型：前置，后置，环绕，异常，返回
```java

    // 除环绕通知外，所有方法都可以使用JoinPoint作为切入点参数
    @Before("readPointcut2()")
    public void read(JoinPoint joinPoint){
        // 业务处理

        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();
    }

    // 环绕通知需使用ProceedingJoinPoint作为接入点参数，主动调用proceed方法，否则不会进入实际方法执行
    @Around("readPointcut2()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
```
### 注解驱动
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogRecode {
    String value() default "";
}
```
```
@Pointcut("@annotation(com.dongle.spring.demo.springmodel.aop.LogRecode)")
public void logRecode(){}
```


## Config配置中心
### 服务端
1. 添加cloud config server依赖
```xml
<!-- 版本请匹配cloud依赖 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
2. 添加配置spring.cloud.config
```yml
# 本地目录
spring:
  profiles:
    active: native
  cloud:
    config:
      server:
        # native 代表本地目录
        native:
          search-locations: file://${configpath} # 如/Data/Temp/config
        # git仓库
        git:
          uri: https://github.com/config-repo
```
3. 开启服务作为ConfigServer
```java
@SpringBootApplication
@EnableConfigServer
public class CloudConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudConfigApplication.class, args);
    }
}
```
### 客户端
1. 添加cloud config client依赖(不关联web，需额外依赖)
```xml

```
2. 配置配置中心地址，即config server
```yml
# /{application}/{profile}[/{label}]
# /{application}-{profile}.yml
# /{label}/{application}-{profile}.yml
# /{application}-{profile}.properties
# /{label}/{application}-{profile}.properties
# label 是仓库分支，如git的master分支
spring:
  profiles:
    active: dev
  application:
    name: client
  config:
    # spring 2.4 提供新方式spring.config.import 取代spring.cloud.config#uri的方式
    import: optional:configserver:http://localhost:8080
```
3. 正常读写配置即可，可以利用@Value加载配置
```java
@Component
public class PropertyReader {

    @Value("${name}")
    private String name;

    public String getName() {
        return name;
    }
}

```