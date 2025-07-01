# 注册中心
## 依赖
``` xml
<!-- 添加web依赖，用于eureka控制后台 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
## Eureka注册中心
创建SpringBoot项目，对Application使用`@EnableEurekaServer`注解,**所有服务都注册到该注册中心，都属于客户端**
``` java
@SpringBootApplication
@EnableEurekaServer
public class DongleEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleEurekaApplication.class, args);
    }
}
``` 
### 配置eureka注册中心
```properties
spring.application.name=eureka-server

#服务注册中心端口号
server.port=1110

#服务注册中心实例的主机名
eureka.instance.hostname=localhost

#是否向服务注册中心注册自己
eureka.client.register-with-eureka=false

#是否检索服务
eureka.client.fetch-registry=false

#服务注册中心的配置内容，指定服务注册中心的位置；注意结尾必须有/，eureka客户端不会处理结尾，会自动拼接
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
```
### 启动
启动项目即可创建注册中心，其他服务通过eureka注册中心服务地址即可注册或发现服务

# 服务注册
同服务发现

# 服务发现
## 依赖
``` xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
## 客户端
创建springboot项目，使用@EnableEurekaClient注解
``` java
@SpringBootApplication
@EnableDiscoveryClient // 过时 @EnableEurekaClient
public class DongleEurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleEurekaClientApplication.class, args);
    }

}
```
## 配置eureka客户端
``` properties
server.port=8080

spring.application.name=dongle-eureka-client

# 指定本服务注册到eureka上的服务名
eureka.instance.appname=dongle-eureka-server
eureka.client.register-with-eureka=false
# 注意结尾必须有/，eureka客户端不会处理结尾，会自动拼接
eureka.client.service-url.defaultZone=http://localhost:9000/eureka/
```

# 服务调用
## Restemplate + @LoadBalanced方式调用
注：使用@LoadBalanced注解Restemplate才拒用调用分布式服务的能力式
```java
@Configuration
public class RestTemplateConfigure {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
调用方式：http://{eureka-service-name}/{interface-path}
```java
    @Autowired
    private RestTemplate template;

    @Override
    public String hello() {
        return template.getForObject("http://dongle-eureka-server/base/hello", String.class);
    }
```
## Feign方式调用
见[feign](feign.md)

# 服务监听
## eureka服务监听
* `@EventListener` 注解监听eureka服务事件
* `EurekaInstanceCanceledEvent` 服务下线事件
* `EurekaInstanceRegisteredEvent` 服务上线事件
* `EurekaRegistryAvailableEvent` 注册中心启动事件
* `EurekaServerStartedEvent` Eureka Server启动事件
```java
@Component
public class EurekaListener {

    @EventListener
    //服务下线监听
    public void listen(EurekaInstanceCanceledEvent event){
        System.err.println(event.getServerId()+"\t"+event.getAppName()+"服务下线");
    }

    @EventListener
    //服务上线监听
    public void listen(EurekaInstanceRegisteredEvent event){
        InstanceInfo instanceInfo = event.getInstanceInfo();
        System.err.println(instanceInfo.getAppName()+instanceInfo.getHostName()+instanceInfo.getIPAddr()+"进行注册");
    }

    @EventListener
    //注册中心启动
    public void listen(EurekaRegistryAvailableEvent event){
        System.err.println("注册中心 启动");
    }

    @EventListener
    //Eureka Server启动
    public void listen(EurekaServerStartedEvent event){
        System.err.println("Eureka Server 启动");
    }
}
```
## 获取服务列表
* `EurekaClient`：Eureka客户端
* `Applications`：获取指定服务列表
```java
@Component
public class EurekaDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger("EurekaJob");

    @Autowired
    private EurekaClient eurekaClient;  // 注意是EurekaClient不是DiscoveryClient，该类型未被初始化

    @Scheduled(cron = "*/10 * * * * *")
    public void checkEureka(){
        // 发现所有服务列表 eurekaClient.getApplications();
        // 发现指定服务列表
        Applications applications = eurekaClient.getApplications("dongle-eureka-server");
        for (Application application : applications.getRegisteredApplications()){
            for (InstanceInfo info:application.getInstances()){
                LOGGER.info("ip:{},host:{},home:{}" ,info.getIPAddr() ,info.getHostName(),info.getHomePageUrl());
            }
        }
    }
}
```

# 非Spring Boot支持
使用原生eureka服务，spring cloud需要支持springboot
## 依赖
```xml
<dependency>
    <groupId>com.netflix.eureka</groupId>
    <artifactId>eureka-client</artifactId>
    <version>1.10.17</version>
    <exclusions>
        <exclusion>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 可依赖Springframework框架 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.8.RELEASE</version>
</dependency>
```
## 配置
* `EurekaServerConfigBean`
* `EurekaDashboardProperties`
* `EurekaClientConfigBean`
* `EurekaInstanceConfigBean`
```properties
eureka.preferSameZone=true
eureka.shouldUseDns=false
# eureka注册中心地址
eureka.serviceUrl.default=http://localhost:9000/eureka/
# 解码方式
eureka.decoderName=JacksonJson
eureka.registration.enabled=true # 决定是否将本服务注册到eureka中

eureka.name=eureka-client-service
# 若注册本服务，需将本服务端口告知
eureka.port=9300
## 其他配置请参考官网
```

## 注册
* `EurekaClient`：Eureka客户端
* `EurekaInstanceConfig`：Eureka实例配置
* `InstanceInfo`：Eureka实例信息
* `ApplicationInfoManager`：应用信息管理
* `DiscoveryClient`：Eureka客户端
```java
@Configuration
public class EurekaClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("EurekaClientConfig");

    @Bean
    public EurekaClient eurekaClient(){
        LOGGER.info("start register eureka service......");
        // 定义Eureka实例配置，若是注册服务，可自定义实现示例配置;不注册默认MyDataCenterInstanceConfig即可
        EurekaInstanceConfig instanceConfig = new EurekaInstanceConfig();
        InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
        ApplicationInfoManager applicationInfoManager= new ApplicationInfoManager(instanceConfig, instanceInfo);
        // 若注册，将实例状态设置为InstanceInfo.InstanceStatus.UP;不注册不需要
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
        // 
        EurekaClient client = new DiscoveryClient(applicationInfoManager,new DefaultEurekaClientConfig());
        LOGGER.info("end register eureka service.......");
        return client;
    }

    private static class EurekaInstanceConfig extends MyDataCenterInstanceConfig {
        @Override
        public String getHostName(boolean refresh) {
            String hostName;
            try {
                hostName = super.getIpAddress() + ":" + super.getAppname().toLowerCase() + ":" + super.getNonSecurePort();
            }catch (Exception ex){
                hostName = super.getHostName(refresh);
            }
            return hostName;
        }
    }
}
```

