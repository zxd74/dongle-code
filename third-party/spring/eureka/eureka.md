# 注册中心
## 依赖
``` xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
```
## 创建Eureka注册中心
创建SpringBoot项目，对Application使用@EnableEurekaServer注解
``` java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DongleEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleEurekaApplication.class, args);
    }

}
``` 
### 配置eureka
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
## 创建客户端
创建springboot项目，使用@EnableEurekaClient注解
``` java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DongleEurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongleEurekaClientApplication.class, args);
    }

}
```
### 配置eureka
``` properties
server.port=8080

spring.application.name=dongle-eureka-client

# 指定本服务注册到eureka上的服务名
eureka.instance.appname=dongle-eureka-server
eureka.client.register-with-eureka=false
# 注意结尾必须有/，eureka客户端不会处理结尾，会自动拼接
eureka.client.service-url.defaultZone=http://localhost:9000/eureka/
```
### 启动客户端

# 监听
## eureka服务监听
见文件EurekaListener.java
## 获取eureka服务列表
见文件EurekaDiscovery.java

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
见feign目录


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
```properties
eureka.preferSameZone=true
# 是否使用dns解析
eureka.shouldUseDns=false
# eureka注册中心地址
eureka.serviceUrl.default=http://localhost:9000/eureka/
# 解码方式
eureka.decoderName=JacksonJson
# 决定是否将本服务注册到eureka中，true注册，false不注册
eureka.registration.enabled=true
# 服务名
eureka.name=eureka-client-service
# 若注册本服务，需将本服务端口告知
eureka.port=9300
## 其他配置请参考官网
````
## 注册
```java

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 */
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
## 整理
