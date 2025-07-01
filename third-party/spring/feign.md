原生`feign`是由`netflix`提供支持，但后来由于停止维护，由Spring团队开发了统一的`spring-cloud-openfeign`

Feign只是相当于一个**接口代理包**(**统筹所有公共服务接口**，并整合相关实体Model)，没有实际处理逻辑，仅仅关联注册中心上的服务。

# 依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
## 定义Feign接口服务
```java
// @FeignClient 关联准备调用的分布式服务名
@FeignClient("dongle-eureka-server")
public interface FeignService {

    @RequestMapping("/base/hello")
    public String baseHello();
}
```

## 其它服务调用
1. 添加构建的自定义Feign服务
```xml
<!--自定义Feign服务依赖-->

<!--额外需要：服务注册/发现，web服务-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
2. 开启`Feign`代理：在`Application`类中使用`@EnableFeignClients`注解
```java
@SpringBootApplication
@EnableDiscoveryClient // 需要服务注册/发现才能有效
@EnableFeignClients
public class DongleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleApplication.class, args);
    }

}
```
3. 使用`@Autowired`注入`FeignClient`
4. 调用`FeignClient`接口方法
   1. 直接在`Controller`中注入并调用
   2. 或者在`Service`中注入并调用
```java
@RestController
public class FooController {
    @Autowired
    private FeignService client; // 使用Feign代理的服务

    @RequestMapping("xxx")
    public String foo() {
        return client.xxx();
    }
}

@Service
public class FooService {
    @Autowired
    private FeignService client; // 使用Feign代理的服务

    public String getFoo() {
        return client.xxx();
    }
}
```

# 注意
feign一般可做为**负载均衡**和**熔断措施**，作用于分布式服务，一般**依赖于类似eureka的服务发现与注册**
