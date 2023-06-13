# 依赖
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```
# 使用
1. Application类使用@EnableFeignClients注解
```java
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
public class DongleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleApplication.class, args);
    }

}
```
## 定义Feign接口服务
```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

// @FeignClient 关联准备调用的分布式服务名
@FeignClient("dongle-eureka-server")
public interface FeignService {

    @RequestMapping("/base/hello")
    public String baseHello();
}
```
# 注意
feign一般可做为负载均衡和熔断措施，作用域分布式服务，一般依赖于eureka服务发现与注册