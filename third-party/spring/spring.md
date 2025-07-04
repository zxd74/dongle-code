# 依赖
```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<!-- Import dependency management from Spring Boot -->
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>3.4.4</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>

        <!-- Import dependency management from Spring cloud -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2024.0.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!-- Import dependency management from Spring AI -->
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-M7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
	</dependencies>
</dependencyManagement>
```
## JDK版本对应
JDK|Spring Framework|Spring Boot(Dependencies)|Spring Cloud|Spring Cloud Dependencies
---|---|---|---|---
17+|6.2.x|3.5.x|4.3.x|2025.0.x
17+|6.2.x|3.4.x|4.2.x|2024.0.x
17+|6.1.x|3.3.x|4.1.x|2023.0.x
17+|6.0.x|3.0.x|4.0.x|2022.0.x
8+|5.3.x|2.6.x|3.1.x|2021.0.x

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
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:mvc="http://www.springframework.org/schema/mvc"
xsi:schemaLocation="http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd">

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
</beans>
```

提示：MVC接口需要使用@Controller或@RestController等注解

## 任务配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:task="http://www.springframework.org/schema/task"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/task
            http://www.springframework.org/schema/task/spring-task-3.0.xsd">

            <!--定时任务注解驱动-->
            <task:annotation-driven />

            <!-- 指定任务器配置 -->
            <task:annotation-driven scheduler="myScheduler"/>
            <task:executor id="executor" pool-size="3" />
            <task:scheduler id="myScheduler" pool-size="5"/>

            <!-- 任务列表 -->
            <task:scheduled-tasks>
                <task:scheduled ref="dataTask" method="calc" cron="30 0 * * * ?"/>
            </task:scheduled-tasks>
</beans>
```
提示：任务类需要配置@Component等注解

# 自定义处理
## 属性绑定
* **java方法**: 通过`InputStream`读取配置文件，并通过`Properties`对象存储配置文件中的键值对
  * 读取配置文件`new FileInputStream("/config.properties")`
  * 读取资源文件`this.getClass().getResourceAsStream("/config.properties")`
```java
Properties prop = readPropertiesFile("config.properties");
// 输出读取到的属性值
System.out.println("host : " + prop.getProperty("host"));

// 可定义一个静态方法，用于读取属性文件并返回 Properties 对象
public static Properties readPropertiesFile(String fileName) throws IOException {
    // 声明 FileInputStream 对象，用于读取文件
    FileInputStream fis = null;
    // 声明 Properties 对象，用于存储从文件中读取的属性
    Properties prop = null;
    try {
        // 创建 FileInputStream 对象，用于从指定文件读取数据
        fis = new FileInputStream(fileName);
        // 创建 Properties 对象，用于存储属性文件中的键值对
        prop = new Properties();
        // 加载属性文件内容到 Properties 对象中
        prop.load(fis);
    } catch (FileNotFoundException e) {
        // 捕获文件未找到的异常，并打印堆栈跟踪信息
        e.printStackTrace();
    } catch (IOException e) {
        // 捕获 I/O 异常，并打印堆栈跟踪信息
        e.printStackTrace();
    } finally {
        // 确保文件流在读取结束后关闭
        if (fis != null) {
            fis.close();
        }
    }
    // 返回存储了属性内容的 Properties 对象
    return prop;
}
```
* 使用`Environment`：通过`@Resource`注解引入`Environment`对象，通过`getProperty()`方法获取配置文件中的属性值
```java
@Resource
private Environment env;

env.getProperty("env.var1");
```
* 使用`@Value`注解：通过`@Value`注解，将配置文件中的属性值注入到类的属性中
```java
@Value("${env.var1}")
private String var1;
```
* 使用`@ConfigurationProperties`注解：通过`@ConfigurationProperties`注解，将配置文件中的属性值注入到类的属性中
```java
@Configuration
@ConfigurationProperties(prefix = "env")
public class MyConf {
    private String var1;
    private String var2;
}
```
* 使用`@PropertySources`注解：通过`@PropertySources`注解，将多个配置文件中的属性值注入到类的属性中
```java
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:dongle.properties", encoding = "utf-8"),
    @PropertySource(value = "classpath:dongle1.properties", encoding = "utf-8")
})
public class PropertySourcesConf {
    @Value("${env.var10}")
    private String var10;
    @Value("${env.var9}")
    private String var9;
}
```
* 使用`YamlPropertiesFactoryBean`加载YAML文件
```java
@Configuration
public class MyYamlConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer yamlConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("dongle.yml"));
        configurer.setProperties(Objects.requireNonNull(yaml.getObject()));
        return configurer;
    }
}

@Value("${env.var11}")
private String var11;
```
* 自定义读取：通过`PropertySources`对象获取配置文件中的属性值
```java
@Autowired
private PropertySources propertySources;

for (PropertySource<?> propertySource : propertySources) {
    log.info("自定义获取 配置获取 name {} ,{}", propertySource.getName(), propertySource.getSource());
}
```

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
1. 引入`springboot`的`configuration`模块`processor`和`autoconfigure`
2. 创建功能模块
3. 配置`Configuration`自动配置类，关联功能模块类
4. 创建`Spring.factories`文件，配置`EnableAutoConfiguration`，添加功能模块自动配置类信息
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
## Web
* Spring MVC
  * `@Controller/@RestController`: 控制器
  * `@RequestMapping`: 请求映射
  * `@RequestBody`: 请求体
  * `@PathVariable`: 路径变量
  * `@RequestParam`: 请求参数
  * `@RequestHeader`: 请求头
  * `@CookieValue`: cookie值
  * `@ModelAttribute`: 模型属性
* Spring Boot Web: Servlet Web，同步阻塞
* Spring Boot WebFlux: Reactive Web,响应式

### Servlet Web
* 默认使用`Tomcat Servlet`作为服务器
* 每个线程对应一个请求，IO阻塞型
* 默认配置属性`org.springframework.boot.autoconfigure.web.ServerProperties`,针对不同容器，有额外配置
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.6.15</version>
</dependency>
```
### Reactive Web(WebFlux)
* **与业务代码无关**，只需将`spring-boot-starter-web`替换为`spring-boot-starter-webflux`即可。
* 默认使用`Netty`作为服务器，其它的需要手动添加依赖。
* 少量线程处理多个请求，不阻塞
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <version>2.6.15</version>
</dependency>
```

### MVC
```xml
<!-- 当使用spring boot starter web/webflux 自动引入-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
</dependency>
```
#### 过滤器 Filter
    过滤器是对数据进行过滤，预处理过程

**实现方式**
* 通过`Componenent`注册
* 通过`@Configuration+@Bean`注册
* 通过`@WebFilter`注册(**独特的**)
```java
@Component
public class WebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 根据请求链路进行预处理
    }
}
```
* 通过`@WebFilter`设置过滤器的匹配规则
```java
@WebFilter(filterName = "filter1",urlPatterns = {"/hello/*"})
public class WebFilter implements Filter {
    ...
}
```
* 通过`@Configuration+@Bean`注册
```java
@Configuration
public class WebFilterConfig {

    @Bean
    public FilterRegistrationBean<WebFilter> webFilter(){
        FilterRegistrationBean<WebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new WebFilter());
        registrationBean.addUrlPatterns("/hello/*");
        return registrationBean;
    }
}
```

#### 拦截器 Interceptor
特性	|拦截器(Interceptor)	|AOP
----|:--:|:--:
作用范围	|主要针对HTTP请求，作用于Controller层	|可以作用于任何Spring管理的Bean
实现原理	|基于Servlet过滤器链	|基于动态代理
粒度	|方法级别(Controller方法)	|更细粒度(方法、构造器、字段等)
依赖	|依赖于Spring MVC框架	|不依赖特定框架
执行时机	|主要在请求处理前后	|可以在方法调用前后、异常时、返回后等
配置方式	|实现HandlerInterceptor接口并注册	|使用注解或XML配置切面

* **拦截器属于AOP的一种实现**
* 拦截器是**Spring MVC框架的一部分**，专门用于拦截HTTP请求，作用于Controller层
* 粒度是方法级别，依赖于Spring MVC框架
* 执行时机主要在请求处理前后，配置方式是实现`HandlerInterceptor`接口并注册。

```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在Controller方法执行前调用
        return true; // 返回false则中断请求
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 在Controller方法执行后，视图渲染前调用
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 在整个请求完成后调用
    }
}
```
#### Listener
  监听器对一些特定事件的监听，当事件发生时，执行相应的操作,不仅仅是web mvc，其它业务也是有各自的监听器，只不过不过多关注。

* `javax.servlet.ServletContextListener`、
* `javax.servlet.ServletRequestListener`、
* `javax.servlet.ServletRequestAttributeListener`、
* `javax.servlet.http.HttpSessionListener`、
* `javax.servlet.http.HttpSessionAttributeListener`

支持三种方法注册Bean：`@Componenet，@Configuration+@Bean,@WebListener`
```java
@WebListener
public class DemoListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContextListener 初始化上下文");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContextListener 销毁");
    }
}
```


## AOP
    适用于任意Bean任意粒度的切面编程

1. 添加aop启动块依赖(aspectj)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
2. 开启AOP代理
   * 通过注解`@EnableAspectJAutoProxy`开启，**默认开启**
   * 通过配置`spring.aop.auto=true`开启
3. 创建AOP类，并使用`@Aspect`注解
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
5. 配置通知类型：前置，后置，环绕，异常，返回
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

**原理**
* 实际使用了`org.aspectj:aspectjweaver`包，
* 通过`@EnableAspectJAutoProxy`注解开启AOP功能，
* `@Aspect`注解定义切面
* `@Pointcut`注解定义切入点：可以是注解`@Annotation`，也可以是类`within`，也可以是方法`execution`
  * **增强**：`@Before,@After,@Around,@AfterReturning,@AfterThrowing`注解定义通知,对应通知分类

**Advice分类**
* `MethodBeforeAdvice` 方法前通知
* `AfterReturningAdvice` 方法后通知
* `MethodInterceptor` 方法环绕通知
* `ThrowsAdvice` 异常通知
* `IntroductionInterceptor` 引入通知

### 实现方式
* **Spring API实现**
  * 所有通知分类都继承自`Advice`接口
  * `Interceptor`是拦截器，属于AOP的一种实现
```java
public class Log implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"执行了"+method.getName()+"方法");
    }
}
/**  <aop:config></aop:config>
<!--切入点：expression:表达式，execution(要执行的位置！ * * * * *) -->
<aop:pointcut id="pointcut" expression="execution(* com.lili.service.UserServiceImpl.*(..))"/>
<!--执行环绕增加！-->
<aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
<aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
 */
```
* **自定义类实现**
```java
public class OneSelfPointcut {
    public void before() {
        System.out.println("使用前");
    }
    public void after() {
        System.out.println("使用后");
    }
}
/** <aop:config></aop:config>
<!--自定义切面， ref 要引用的类-->
<aop:aspect ref="oneSelfPointcut">
<!--切入点-->
<aop:pointcut id="point" expression="execution(* com.lili.service.UserServiceImpl.*(..))"/>
<!--通知-->
<aop:before method="before" pointcut-ref="point"/>
<aop:after method="after" pointcut-ref="point"/>
*/
```
* **注解实现**
```java
@Aspect// 标注此类是一个切面
public class AnnotationPointCut {
    @Before("execution(* com.lili.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("方法执行前");
    }
    @After("execution(* com.lili.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("方法执行后");
    }
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

## Test
* 单元测试：功能测试
* 切片测试：链路测试，如controller+Service
* 集成测试：完整Spring上下文测试
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**测试相关API**
* 单元测试：`@Mock`,`@InjectMocks`,`@BeforeEach`,`@Test`
* 切片测试：`@WebMvcTest`,`MockMvc`,`MockMvcRequestBuilders`,`MockMvcResultMatchers`
* 集成相关：`@SpringBootTest`,`@LocalServerPort`,`TestRestTemplate`,`ResponseEntity`,`HttpStatus`

### 单元测试
* `@Mock` 用于对指定对象进行模拟
* `@InjectMocks` 用于对指定对象进行注入
* `@BeforeEach` 用于在测试方法执行前执行
* `@Test` 用于标记测试方法
* `MockitoAnnotations.openMocks(this)` 用于初始化mock对象
* `Mockito.when().thenReturn()` 用于模拟方法返回值
* `Mockito.verify().xxxmethod()` 用于验证方法是否被调用
* `Assertions.assertThat()` 用于断言结果

```java
public class ServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserWhenExists(){
        // 1. 准备mock数据
        List<User> list = new ArrayList<>(); 
        // ...
        Mockito.when(userDao.all()).thenReturn(list);

        // 2. 执行测试
        list = userService.info();

        // 3. 验证结果
        assertThat(list.size()).isEqualTo(5);
        Mockito.verify(userDao).all();
    }
}
```

### 切片测试
#### WebMVC测试
* `@WebMvcTest` 专用于特定功能的测试，如**WebMvc**
* `MockMvc` 用于模拟HTTP请求的对象
* `MockMvcRequestBuilders#get/post/xxx` 构建HTTP请求
* `MockMvcResultMatchers#status,content,jsonPath` 用于断言HTTP响应
```java
@WebMvcTest(IndexController.class)
public class IndexControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturn200() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string("OK"));
    }
}
```
#### JPA测试
```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository repo;
    
    @Test
    void shouldFindByEmail() {
        entityManager.persist(new User("test@example.com"));
        
        User user = repo.findByEmail("test@example.com");
        
        assertThat(user).isNotNull();
    }
}
```

### 集成测试
* `@SpringBootTest` 用于启动整个Spring应用
* `@LocalServerPort` 用于获取启动的端口号
* `TestRestTemplate` 用于发送HTTP请求
* `ResponseEntity` 用于接收HTTP响应
* `HttpStatus` 用于断言HTTP响应状态码
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringApplicationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Test
    void shouldReturnUser(){
        ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/",String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

### 技巧
1. 测试配置隔离
```java
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "logging.level.root=ERROR"
})
```
2. 数据库回滚
```java
@Transactional
@Rollback
@DataJpaTest
class TransactionalTest {
    // 测试后自动回滚
}
```
1. 自定义Mock:`mock()`
```java
@Test
void testWithCustomMock() {
    UserService mockService = mock(UserService.class, 
        withSettings().defaultAnswer(Answers.RETURNS_SMART_NULLS));
    
    // ...
}
```

### 最佳实践
* **命名规范**：
  * 测试类：被测试类名+Test（如UserServiceTest）
  * 测试方法：should_When_格式（如shouldReturnNullWhenUserNotExists）
* **测试覆盖率**：
```bash
# 使用Jacoco生成报告
mvn test jacoco:report
```
* **避免常见错误**：
  * 不要在生产代码中写`System.out.println`
  * 避免测试依赖执行顺序
  * Mock时使用ArgumentMatchers而非固定值

## Cloud
### Bus
    集成RabbitMQ和Kafka用轻量级消息代理连接分布式系统的节点
* 依赖
```xml
<!-- rabbitmq -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<!-- kafka -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-kafka</artifactId>
</dependency>
```
* 配置
```yml
spring:
  rabbitmq:
    host: mybroker.com
    port: 5672
    username: user
    password: secret
```
```properties
management.endpoints.web.exposure.include=busrefresh # Bus Refresh Endpoint
management.endpoints.web.exposure.include=busenv # Bus Env Endpoint
management.endpoints.web.exposure.include=busshutdown # Bus Shutdown Endpoint

spring.cloud.bus.id  # 唯一标识，默认为spring.application.name,spring.application.index,
```
```java
@Configuration
//@RemoteApplicationEventScan({"com.acme", "foo.bar"})
//@RemoteApplicationEventScan(basePackages = {"com.acme", "foo.bar", "fizz.buzz"})
@RemoteApplicationEventScan(basePackageClasses = BusConfiguration.class)
public class BusConfiguration {
    ...
}
```

### Eureka
由Netflix提供支持，详情见[Eureka](./eureka.md)

### Consul
**统一服务发现和配置管理**,Eureka的替代品

* 安装Consul server
```bash
# 下载最新版 (1.16.0示例)
wget https://releases.hashicorp.com/consul/1.16.0/consul_1.16.0_linux_amd64.zip
unzip consul_1.16.0_linux_amd64.zip
sudo mv consul /usr/local/bin/

# 验证安装
consul --version
# 启动开发模式
consul agent -dev -client=0.0.0.0 -ui
```
* 依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```
* **服务发现**
```yml
spring:
  cloud:
    consul:
      host: localhost
      port: 8500
```
```java
@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
// DiscoveryClient 可以根据需要拉取一个或多个服务实例列表
@Service
public class Services {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    public String serviceUrl(){
        List<ServiceInstance> list = discoveryClient.getInstances("service-id");
        if (!list.isEmpty()){
            return list.get(0).getUri().toString();
        }
        return null;
    }

    public List<String> getServices(){
        return discoveryClient.getServices();
    }
}
```
#### 配置管理(集成Config)
```xml
spring:
  cloud:
    consul:
      config:
        enabled: true
        prefix: configuration
        defaultContext: apps
        profileSeparator: '::'
        format: YAML  # or PROPERTIES，FILES
```
### Config
#### 服务端
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
#### 客户端
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

### Gateway
    API网关，提供路由、过滤、监控等功能

```xml
<!-- Gateway For Reactive -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<!-- Gateway For MVC -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway-mvc</artifactId>
</dependency>
<!-- Gateway For WebFlux -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway-webflux</artifactId>
</dependency>
```
以`WebMvc`为例
* 配置
```yml
spring:
  cloud:
    gateway:
      mvc:
        routes:
        - id: after_route
          uri: https://example.org
          predicates:
          - After=2017-01-20T17:42:47.789-07:00[America/Denver]
```
```java
@Configuration
class RouteConfiguration {

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctionsAfter() {
        return route("after_route")
            .route(after(ZonedDateTime.parse("2017-01-20T17:42:47.789-07:00[America/Denver]")), http())
            .before(uri("https://example.org"))
            .build();
    }
}

class MyFilter implements Filter, Ordered {

    @Override
    public int getOrder() {
        return FormFilter.FORM_FILTER_ORDER - 1;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // ...
        filterChain.doFilter(request, response);
        // ...
    }
}
```
* Proxy Exchange Gateway：`ProxyExchange`
```java
@RestController
@SpringBootApplication
public class GatewaySampleApplication {

	@Value("${remote.home}")
	private URI home;

	@GetMapping("/test")
	public ResponseEntity<?> proxy(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(home.toString() + "/image/png").get();
	}

    @GetMapping("/proxy/path/**")
    public ResponseEntity<?> proxyPath(ProxyExchange<byte[]> proxy) throws Exception {
        String path = proxy.path("/proxy/path/");
        return proxy.uri(home.toString() + "/foos/" + path).get();
    }
}
```


## AI
### 依赖
* 由于spring ai发布情况限制，需要补充仓库和依赖管理配置
```xml
<repositories>
    <repository>
        <id>spring-snapshots</id>
        <name>Spring Snapshots</name>
        <url>https://repo.spring.io/snapshot</url>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-M7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
* starter模式
 * 在`1.0.0-M6`及以前版本时，自动装配使用`spring-ai-{xxx}-spring-boot-starter`
 * 使用`1.0.0-M7`及以后版本时，使用`spring-ai-starter-model-{XXX}`
    ```xml
    <!-- <= 1.0.0-M6 -->
    <dependency>
        <groupId>io.springboot.ai</groupId>
        <artifactId>spring-ai-{xxx}-spring-boot-starter</artifactId>
        <version>1.0.0-M6</version>
    </dependency>

    <!-- 1.0.0-M7 +  -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-{xxx}</artifactId>
    </dependency>
    ```
* 非starter模式
  * `spring-ai-{xxx}`
  * 需自主实例化所需**Model**类，如`ChatModel,ImageModel,AudioModel,ModerationModel`等等
    ```xml
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-{xxx}</artifactId>
        <version>1.0.0-M6</version>
    </dependency>
    ```

### ChatModel
* 以starter自动装配为例
  * 注入**Model**类，，以`ChatModel`或`ChatClient`为例
```java
@RestController
@RequestMapping("/ai")
public class AIController {

    private final ChatClient chatClient;
    // @Autowired
    // private final ChatModel chatModel;

    @Autowired
    public AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/chat")
    public String generateText(@RequestBody String prompt) {
        // chatModel.call(prompt);
        return chatClient.call(prompt);
    }
}
```
* **高级参数**`ChatOptions`调整
    ```java
    @PostMapping("/chat/advanced")
    public String advancedChat(@RequestBody String prompt) {
        // 自定义参数
        OpenAiChatOptions options = OpenAiChatOptions.builder()
            .withModel("gpt-4")
            .withTemperature(0.5)
            .withMaxTokens(2000)
            .build();

        // 构建 Prompt
        Prompt request = new Prompt(prompt, options);
        
        return chatClient.call(request);
    }
    ```
*** 流式响应** `StreamingChatClient`
```java
@Autowired
private StreamingChatClient streamingChatClient;

public Flux<String> streamChat(@RequestParam String prompt) {
    return streamingChatClient.stream(prompt);
}
```
* **异常处理**
```java
@RestControllerAdvice
public class AIExceptionHandler {

    @ExceptionHandler(OpenAiApiException.class)
    public ResponseEntity<String> handleOpenAiException(OpenAiApiException ex) {
        return ResponseEntity.status(ex.getStatusCode())
            .body("OpenAI 服务错误: " + ex.getMessage());
    }
}
```
* **多模态支持**（图片生成）
```java
@Autowired
private OpenAiImageClient imageClient;

@PostMapping("/generate-image")
public String generateImage(@RequestParam String prompt) {
    ImageResponse call = imageClient.call(
        new ImagePrompt(prompt)
    );
    return call.getResult().getOutput().getUrl();
}
```
* **嵌入向量**（Embeddings）
```java
@Autowired
private EmbeddingClient embeddingClient;

@PostMapping("/embed")
public List<Double> getEmbedding(@RequestBody String text) {
    return embeddingClient.embed(text);
}
```
* **测试**
  * 单元测试
    ```java
    @SpringBootTest
    public class AIControllerTest {

        @Autowired
        private ChatClient chatClient;

        @Test
        void testChat() {
            String response = chatClient.generate("Hello World!");
            assertNotNull(response);
            System.out.println("AI Response: " + response);
        }
    }
    ```
  * CURL测试
    ```sh
    curl -X POST "http://localhost:8080/ai/chat" \
        -H "Content-Type: application/json" \
        -d '{"prompt": "Hello World!"}'
    ```

* 安全与优化
  * 密钥管理：使用环境变量或 `Vault` 注入 `api-key`,禁止在代码中硬编码密钥
  * 性能调优：连接超时，读取超时等配置
  * 限流和熔断
    ```java
    // Resilience4j 熔断器配置
    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(50)  # 失败率阈值
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .build();
    }
    ```

### ImageModel
* 注入`ImageModel`
  * 使用`ImagePrompt`请求
```java
@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping
    public void generateImage(HttpServletResponse response, @RequestParam String prompt) {
        // 创建ImagePrompt并配置参数
        ImagePrompt prompt = new ImagePrompt(prompt, 
            DashScopeImageOptions.builder
                .withModel("wanx-v1") // 默认模型
                .withWidth(512)
                .withHeight(512)
                .build
        );
        
        // 调用生成接口
        ImageResponse response = imageModel.call(prompt);
        Image image = response.getResult.getOutput;
        
        // 返回图片流或URL
        try (InputStream in = new URL(image.getUrl).openStream) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream.write(in.readAllBytes);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC内饰错误_500);
        }
    }
}
```
* 响应处理
  * 直接返回流：通过`HttpServletResponse`写入图片流，适用于前端直接渲染。
  * 返回URL：获取`Image.getUrl`，需考虑临时URL的过期时间。
* 参数优化
  * 权重控制：通过`ImageMessage`的`weight`字段调整生成效果。
  * 多图生成：设置`ImageOptions.setN(3)`生成多张图片。
* 性能与存储
  * 频繁调用可能触发API限流，建议添加重试机制。
  * 临时图片URL需持久化存储（如阿里云OSS）。
* 扩展场景
  * 多模型切换：通过动态配置`spring.ai.model.image`属性，实现不同模型的无缝切换。
  * 与`Spring Security`集成：保护生成接口，限制调用频率和权限。

### VectorStore
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-vector-store-mariadb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-openai</artifactId>
    </dependency>
</dependencies>
```
```java
public class VectorStoreService {
    @Autowired
    VectorStore vectorStore;

    public void saveDocument(){
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

// Add the documents to MariaDB
        vectorStore.add(documents);

// Retrieve documents similar to a query
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
    }
}

public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
    return MariaDBVectorStore.builder(jdbcTemplate, embeddingModel)
            .dimensions(1536)                      // Optional: defaults to 1536
            .distanceType(MariaDBVectorStore.MariaDBDistanceType.COSINE) // Optional: defaults to COSINE
            .schemaName("mydb")                    // Optional: defaults to null
            .vectorTableName("custom_vectors")     // Optional: defaults to "vector_store"
            .contentFieldName("text")             // Optional: defaults to "content"
            .embeddingFieldName("embedding")      // Optional: defaults to "embedding"
            .idFieldName("doc_id")                // Optional: defaults to "id"
            .metadataFieldName("meta")           // Optional: defaults to "metadata"
            .initializeSchema(true)               // Optional: defaults to false
            .schemaValidation(true)              // Optional: defaults to false
            .removeExistingVectorStoreTable(false) // Optional: defaults to false
            .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
            .build();
}
```

### MCP
#### MCP Client
```xml
<!-- 标准MCP Client -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client</artifactId>
</dependency>
<!-- WebFlux -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client-webflux</artifactId>
</dependency>
```
* 自定义Client，实现`McpSyncClientCustomizer`
```java
//sync
@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {

        // Customize the request timeout configuration
        spec.requestTimeout(Duration.ofSeconds(30));

        // Sets the root URIs that this client can access.
        spec.roots(roots);

        // Sets a custom sampling handler for processing message creation requests.
        spec.sampling((CreateMessageRequest messageRequest) -> {
            // Handle sampling
            CreateMessageResult result = ...
            return result;
        });

        // Adds a consumer to be notified when the available tools change, such as tools
        // being added or removed.
        spec.toolsChangeConsumer((List<McpSchema.Tool> tools) -> {
            // Handle tools change
        });

        // Adds a consumer to be notified when the available resources change, such as resources
        // being added or removed.
        spec.resourcesChangeConsumer((List<McpSchema.Resource> resources) -> {
            // Handle resources change
        });

        // Adds a consumer to be notified when the available prompts change, such as prompts
        // being added or removed.
        spec.promptsChangeConsumer((List<McpSchema.Prompt> prompts) -> {
            // Handle prompts change
        });

        // Adds a consumer to be notified when logging messages are received from the server.
        spec.loggingConsumer((McpSchema.LoggingMessageNotification log) -> {
            // Handle log messages
        });
    }
}

// async
@Component
public class CustomMcpAsyncClientCustomizer implements McpAsyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.AsyncSpec spec) {
        // Customize the async client configuration
        spec.requestTimeout(Duration.ofSeconds(30));
    }
}
```
#### MCP Server
```xml
```xml
<!-- 标准MCP Server -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server</artifactId>
</dependency>
<!-- WebMvc -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
<!-- WebFlux -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
```
* 配置
```properties
# Using spring-ai-starter-mcp-server
spring:
  ai:
    mcp:
      server:
        name: stdio-mcp-server
        version: 1.0.0
        type: SYNC # or ASYNC
```
* Creating a Spring Boot Application with MCP Server
```java
@Service
public class WeatherService {

    @Tool(description = "Get weather information by city name")
    public String getWeather(String cityName) {
        // Implementation
    }
}

@SpringBootApplication
public class McpServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(McpServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

	@Bean
	public ToolCallbackProvider weatherTools(WeatherService weatherService) {
		return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
	}
}
```
* Tools
```java
@Bean
public ToolCallbackProvider myTools(...) {
    List<ToolCallback> tools = ...
    return ToolCallbackProvider.from(tools);
}

// low-level API
@Bean
public List<McpServerFeatures.SyncToolSpecification> myTools(...) {
    List<McpServerFeatures.SyncToolSpecification> tools = ...
    return tools;
}
```
* Resource Management
```java
@Bean
public List<McpServerFeatures.SyncResourceSpecification> myResources(...) {
    var systemInfoResource = new McpSchema.Resource(...);
    var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(systemInfoResource, (exchange, request) -> {
        try {
            var systemInfo = Map.of(...);
            String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
            return new McpSchema.ReadResourceResult(
                    List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to generate system info", e);
        }
    });

    return List.of(resourceSpecification);
}
```
