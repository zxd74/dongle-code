    GraphQL的服务端在多个语言都有实现包括Haskell, JavaScript, Python, Ruby, Java, C#, Scala, Go, Elixir, Erlang, PHP, R,和 Clojure。

# Java版(原生)
[官网](https://www.graphql-java.com/)

1. 导入`graphql`依赖
```xmll
<dependency>
    <groupId>com.graphql-java</groupId>
    <artifactId>graphql-java</artifactId>
    <version>11.0</version>
</dependency>
<!--用于解析schema文件-->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>27.0-jre</version>
</dependency>
```
2. 定义`Schema`文件:`test.graphql`
```graphql
# 定义查询接口, 一个schema文件中只能定义一个Query对象
type Query {
    hello: String
    userById(id: ID!): Person
}
# 定义修改接口
type Mutation {
    hello: String
}

type Person {
  id: ID
  name: String
}
```
3. 装配`GraphQL`对象
   1. 定义`DataFetcher`类, 实现数据获取逻辑
      * 在执行查询时，通过Datafetcher获取一个字段的数据
   2. 解析`Schema`并绑定`DataFetcher`
      * 关联`Schema`文件,来初始化`GraphQL`类
```java
public interface DataFetcher<T> {
    T get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception;
}
```
   * **重要提示**:模式中的每个字段都有一个与之关联的DataFetcher。如果没有为特定字段指定任何DataFetcher，则使用默认的`PropertyDataFetcher`。
   * 也可以在`DataFetcher`中获取参数, 查询指定数据源获取数据进行处理：需要转换为`List<Map<String, String>>`
     * 由此可见，并不适合动态数据，只适合少量静态数据，用于开发测试
```java
@Configuration
public class GraphqlConfiguration {

    private static final List<Map<String, String>> users = Arrays.asList( // all data
            ImmutableMap.of("id", "user-1","name", "Dongle"),
            ImmutableMap.of("id", "user-2","name", "Kevin"),
            ImmutableMap.of("id", "user-3","name", "Sheldon")
    );

    private DataFetcher getUserById() {
        // dataFetchingEnvironment 封装着查询参数
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("id");
            return users.stream().filter(user -> user.get("id").equals(userId)).findFirst().orElse(null);
        };
    }

    @Bean
    public GraphQL graphQL() throws IOException {
        URL url = Resources.getResource("schema.graphql"); // 关联 Schema 定义资源文件
        String sdl = Resources.toString(url, StandardCharsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                // Mutation操作
                .type("Mutation", builder -> builder.dataFetcher("hello", new StaticDataFetcher("Mutation hello world")))
                // Query操作
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("Query hello world")))
                .type(newTypeWiring("Query").dataFetcher("userById", getUserById()))
                .build();
    }
}
```
4. 定义controller通过GraphQL查询数据
   * 在Spring Boot中不需要定义这个，默认会定义一个`/graphql`的Servlet
```java
@RestController
public class GraphQLController {
    @Autowired
    private GraphQL graphQL;

    @RequestMapping(value = "/graphql")
    public Map<String, Object> graphql(@RequestBody String request) {
        JSONObject req = JSON.parseObject(request);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            // 需要执行的查询语言
            .query(req.getString("query"))
            // 执行操作的名称，默认为null
            .operationName(req.getString("operationName"))
            // 获取query语句中定义的变量的值
            .variables(req.getJSONObject("variables"))
            .build();
        // 执行并返回结果
        return this.graphQL.execute(executionInput).toSpecification();
    }
}
```
##  查询
查询使用：访问`host/graphql`接口，传递查询语句即可
* graphql[query]查询语句
```graphql
query userByIds($id: ID = "user-3") {
    test: userById(id: $id) {
        userId: id
        name
    }
    test2: userById(id: "user-2") {
        id
        name
    }
    test3:hello
}
```
* graphql查询变量
```graphql
{
    "variables": {"id": "user-1"}
}
```
 * graphql请求语句**转换**json请求数据：
```json
{"query":"query userByIds($id: ID = \"user-3\") {\r\n    test: userById(id: $id) {\r\n        userId: id\r\n        name\r\n    }\r\n    test2: userById(id: \"user-2\") {\r\n        id\r\n        name\r\n    }\r\n    test3:hello\r\n}","variables":{"id": "user-1"}}
```
## 扩展：动态数据
```java
@Configuration
public class GraphqlConfiguration {
    @Autowired
    private GraphqlService service; // service为动态查询服务
    // ...
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("user", env-> service.getAll()))
                .build();
    }
}
```

# GraphQL Java Tool
* 依赖
```xml
<!-- 旧版 -->
<dependency>
    <groupId>com.graphql-java</groupId>
    <artifactId>graphql-java-tools</artifactId>
    <version>5.2.4</version>
</dependency>
<!-- 新版 -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-java-tools</artifactId>
    <version>13.1.1</version>
</dependency>
```
* Schema定义
```java
@GraphQLSchema
public class MySchema {
    @GraphQLQuery(name = "hello")
    public String hello() {
        return "Hello, World!";
    }
}
```
* 使用时结合`graphql-spring-boot-starter`更方便

# Spring Boot GraphQL Demo
* 依赖
```xml
<!-- 旧版 5.0.0 以前 -->
<denpendency>
    <groupId>com.graphql-java</groupId>
     <artifactId>graphql-spring-boot-starter</artifactId>
    <version>4.4.4</version>
</denpendency>

<!-- 新版，5.x.x 以上 [推荐]-->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>13.1.1</version>
</dependency>
```

* Java Tools 方式：自定义定义`GraphQLResolver`
  * `GraphQLQueryResolver`
  * `GraphQLMutationResolver`
  * `GraphQLSubscriptionResolver`
```yaml
graphql:
  servlet:
    exception-handlers-enabled: true
  graphiql: # 可以通过 GraphiQL 界面编写 GraphQL查询语句
    enabled: true
```
```java
@Component
public class QueryResolver implements GraphQLQueryResolver { // 对应Query操作类型
    @Autowired
    private UserService service;

    public String hello(){ // 对应Query中hello字段
        return "GraphQL Spring Boot Starter[Query] send Hello!";
    }

    public User userById(String id){ // 对应Query中userById字段
        return service.userById(id);
    }

    public List<User> user(){  // 对应Query中user字段
        return service.user();
    }
}

@Component
public class MutationResolver implements GraphQLMutationResolver { // 对应Mutation操作类型
    public String hello(){
        return "GraphQL Spring Boot Starter[Mutation] send Hello!";
    }
}
```
## 注解方式实现
* `@GrahqlResovler`
  * `@GraphQLQueryResolver`
  * `@GraphQLMutationResolver`
  * `@GraphQLSubscriptionResolver`
* `@GraphQLField`：定义字段
* `@GraphQLID`：定义ID类型
* `@GraphQLNonNull`：定义非空，可修饰参数，有返回值方法(验证返回值)
* `@GraphQLName`：定义字段名
* 相比Java Tools方式，注解方式更方便，但验证更全
```yaml
graphql:
  schema-strategy: ANNOTATIONS
  graphiql: # 可以通过 GraphiQL 界面编写 GraphQL查询语句
    enabled: true
```
```java
@Component
@GraphQLQueryResolver
public class QueryResolver implements ApplicationContextAware {
    private static UserService service;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        service = applicationContext.getBean(UserService.class);
    }

    @GraphQLField
    public static String hello(){
        return "GraphQL Spring Boot Starter[Query] send Hello!";
    }

    @GraphQLField
    public static User userById(final @GraphQLNonNull @GraphQLID String id){ // 根据schema.graphqls定义决定，传入参数为ID，需要表明参数为 @GraphQLI， 非空为@GraphQLNonNull
        return service.userById(id);
    }

    @GraphQLField
    public static List<User> user(){
        return service.user();
    }
}

@Component
@GraphQLMutationResolver
public class MutationResolver{
    @GraphQLField
    public static String hello(){
        return "GraphQL Spring Boot Starter[Mutation] send Hello!";
    }
}

// Model也需要标注 @GraphQLField
public class User {

    @GraphQLField
    @GraphQLID
    private String id;
    @GraphQLField
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```
