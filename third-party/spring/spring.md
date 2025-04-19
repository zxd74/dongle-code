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

## MCP
### MCP Client
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
### MCP Server
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
