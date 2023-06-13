1. 启动NacosServer环境
2. 启动配置管理服务
   1. 添加nacos依赖
   2. 配置nacos配置信息
   3. 向nacos server发布配置
    ```shell
    curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true"
    ```
   4. 调用或查看配置管理服务 http://localhost:8080/config/get
3. 启动服务注册服务
   1. 添加依赖
   2. 配置nacos地址
   3. 向nacos server注册服务
    ```shell
    curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=example&ip=127.0.0.1&port=8080'
    ```
   4. 访问查看配置信息 http://localhost:8080/discovery/get?serviceName=example
    ```text
    [
        {
            "instanceId": "127.0.0.1-8080-DEFAULT-example",
            "ip": "127.0.0.1",
            "port": 8080,
            "weight": 1.0,
            "healthy": true,
            "cluster": {
            "serviceName": null,
            "name": "",
            "healthChecker": {
                "type": "TCP"
            },
            "defaultPort": 80,
            "defaultCheckPort": 80,
            "useIPPort4Check": true,
            "metadata": {}
            },
            "service": null,
            "metadata": {}
        }
    ]
    ```

# Spring 使用
1. 启动NacosServer&Spring的配置管理，实现配置的动态变更；
2. 启动NacosServer&Spring的服务发现模块，实现服务的注册与发现。

## 启动配置管理
1. 添加nacos依赖
```xml
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-spring-context</artifactId>
    <version>${latest.version}</version>
</dependency>
```
2. 添加 @EnableNacosConfig 注解启用 Nacos Spring 的配置管理服务。以下示例中，我们使用 @NacosPropertySource 加载了 dataId 为 example 的配置源，并开启自动更新：
```java
@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "127.0.0.1:8848"))
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class NacosConfiguration {

}
```
3. 通过 Nacos 的 @NacosValue 注解设置属性值。
```java
@Controller
@RequestMapping("config")
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }
}
```
4. 启动 Tomcat，调用 curl http://localhost:8080/config/get尝试获取配置信息。由于此时还未发布过配置，所以返回内容是 false。
5. 通过调用 Nacos Open API 向 Nacos Server 发布配置：dataId 为example，内容为useLocalCache=true
```shell
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true"
```
6. 再次访问 http://localhost:8080/config/get ，此时返回内容为true，说明程序中的useLocalCache值已经被动态更新了。

## 启动服务发现
1. 添加nacos依赖
2. 通过添加 @EnableNacosDiscovery 注解开启 Nacos Spring 的服务发现功能：
```java
@Configuration
@EnableNacosDiscovery(globalProperties = @NacosProperties(serverAddr = "127.0.0.1:8848"))
public class NacosConfiguration {

}
```
3. 使用 @NacosInjected 注入 Nacos 的 NamingService 实例：
```java
@Controller
@RequestMapping("discovery")
public class DiscoveryController {

    @NacosInjected
    private NamingService namingService;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
```

# Spring Boot
## 配置管理
1. 添加nacos依赖
```xml
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```
2. 在 application.properties 中配置 Nacos server 的地址：
```properties
nacos.config.server-addr=127.0.0.1:8848
```
3. 使用 @NacosPropertySource 加载 dataId 为 example 的配置源，并开启自动更新
4. 通过 Nacos 的 @NacosValue 注解设置属性值。
5. 启动 NacosConfigApplication，调用 curl http://localhost:8080/config/get，返回内容是 false。
6. 通过调用 Nacos Open API 向 Nacos server 发布配置：dataId 为example，内容为useLocalCache=true
7. 再次访问 http://localhost:8080/config/get，此时返回内容为true，说明程序中的useLocalCache值已经被动态更新了。

## 服务注册
1. 添加依赖
```xml
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-discovery-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```
2. 在 application.properties 中配置 Nacos server 的地址
```properties
nacos.discovery.server-addr=127.0.0.1:8848
```
3. 使用 @NacosInjected 注入 Nacos 的 NamingService 实例：
```java
@Controller
@RequestMapping("discovery")
public class DiscoveryController {

    @NacosInjected
    private NamingService namingService;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}

@SpringBootApplication
public class NacosDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosDiscoveryApplication.class, args);
    }
}
```


# Spring Cloud
## 配置管理服务
1.添加依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>${latest.version}</version>
</dependency>
```
2. 配置nacos地址
```properties
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```
3. 其他同Spring Boot

## 服务发现
1. 添加依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>${latest.version}</version>
</dependency>
```
2. 配置nacos地址
```properties
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```
3. 其他同Spring Boot