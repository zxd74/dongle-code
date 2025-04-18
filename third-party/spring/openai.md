# Spring AI OpenAI
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
* 注意：
  * `spring-ai`在`1.0.0-M6`及以前版本时，自动装配使用`spring-ai-{xxx}-spring-boot-starter`
  * 使用`1.0.0-M7`及以后版本时，使用`spring-ai-starter-model-{XXX}`
* `ChatClient`和`ChatModel`都可以实现对话，区别面向层级和精细控制


## OpenAI starter
* 依赖
```xml
<dependency>
    <groupId>io.springboot.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    <version>1.0.0-M6</version>
</dependency>
```
* 配置
```properties
spring:
  ai:
    openai:
      api-key: "sk-your-openai-api-key-here"  # 替换为你的 OpenAI API 密钥
      base-url: "https://api.openai.com/v1"    # 默认值，通常无需修改
      chat:
        options:
          model: "deepseek-r1:671b"                # 默认模型
          temperature: 0.7                       # 生成文本的随机性控制
          max-tokens: 500
```
## OpenAI
```java
OpenAiApi openAiApi = OpenAiApi.builder()
			.baseUrl(resolved.baseUrl())
			.apiKey(new SimpleApiKey(resolved.apiKey()))
			.headers(resolved.headers())
			.completionsPath(chatProperties.getCompletionsPath())
			.embeddingsPath(OpenAiEmbeddingProperties.DEFAULT_EMBEDDINGS_PATH)
			.restClientBuilder(restClientBuilder)
			.webClientBuilder(webClientBuilder)
			.responseErrorHandler(responseErrorHandler)
			.build();
OpenAiChatModel chatModel = OpenAiChatModel.builder()
        .openAiApi(openAiApi)
        .defaultOptions(chatProperties.getOptions())
        .toolCallingManager(toolCallingManager)
        .retryTemplate(retryTemplate)
        .observationRegistry(observationRegistry.getIfUnique(() -> ObservationRegistry.NOOP))
        .build();
// chatModel.call # ChatMode适用于高级功能

ChatClient.Builder builder = ChatClient.builder(chatModel, (ObservationRegistry)observationRegistry.getIfUnique(() -> {
            return ObservationRegistry.NOOP;
        }), (ChatClientObservationConvention)observationConvention.getIfUnique(() -> {
            return null;
        }));
ChatClient chatClient = builder.build();
// chatClient.call # ChatClient适用于简单对话功能
```

# 核心
* 注入`ChatModel`或`ChatClient`
