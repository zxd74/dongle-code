# 支持Dubbo
## Spring Boot
1. 添加依赖
```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.8</version>
</dependency>
```
2. 通过@EnableDubbo开启Dubbo
```java
@SpringBootApplication
@EnableDubbo
public class DubboDemoConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboDemoConsumerApplication.class, args);
    }

}

```
3. 定义公共接口包
4. 生产(提供)者通过@DubboService注册服务(实现接口)，可绑定特定属性
```java
@DubboService(version = "1.0.0")
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return name + " say hello by v1.0.0";
    }
}

```
5. 消费(调用)者通过@DubboReference依赖服务(接口)，可关联特定属性
```java
@RestController
@RequestMapping("/demo")
public class DemoController {

    @DubboReference(version = "1.0.0")
    private DemoService demoService;

    @DubboReference(version = "2.0.0")
    private DemoService demo1Service;

    @DubboReference(version = "*")
    private DemoService demo2Service;

    @RequestMapping("sayHello")
    public String sayHello(@RequestParam int type){
        if (type == 1) return demoService.sayHello("Dongle");
        if (type == 2) return demo1Service.sayHello("Kevin");
        if (type == 3) return demo2Service.sayHello("Shelldon");

        return "";
    }
}
```
6. 配置dubbo相关信息
```properties
dubbo.application.name=dubbo-demo-consumer # 必需属性，需要注册到注册中心的
dubbo.protocol.name=dubbo
dubbo.protocol.port=-1 # 端口无效时，由各协议默认指定，如dubbo协议默认为20880，gRPC为50051
```
7. 生产者要早于消费者启动哟~~~


## 注册中心
    注册中心只需加载依赖并配置地址即可，代码无需处理，dubbo内部处理。
### Nacos
1. 添加依赖
```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-nacos</artifactId>
    <version>2.7.8</version>
</dependency>
```
2. 配置注册中心地址及其他信息
```properties
dubbo.registry.address=nacos://docker.dongle.com:8848
dubbo.registry.username=nacos
dubbo.registry.password=nacos
```

# 功能使用
