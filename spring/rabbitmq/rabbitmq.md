# Java原生
```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.4.3</version>
</dependency>
```
```java
public void rabbitMqConnect(){
        Connection conn = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername("userName");
            factory.setPassword("password");
            factory.setVirtualHost("path");
            factory.setHost("host");
            factory.setPort(5672);
            conn = factory.newConnection();
            channel = conn.createChannel();
            // 指定队列交换器模式
            channel.exchangeDeclare("exchange","direct");
            // 绑定queue队列
            String queueName = channel.queueDeclare().getQueue();
            channel.exchangeBind(queueName,"exchange","routekey");
            channel.queueDeclare(queueName, true, false, false, null);
            // 发布消息 bytes
            channel.basicPublish("",queueName,null,new byte[5]);

            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            Map<String,Object> headers = new HashMap<>(2);
            headers.put("id","test01");
            builder.headers(headers);
            channel.basicPublish("","",builder.build(),new byte[5]);
            channel.queueBind("","","",headers);

            // 消息订阅 Consumer
            channel.basicQos(1);
            channel.basicConsume("queueName", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

                }
            });
        }catch (Exception ex){
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
```


# Spring

## Spring Boot
1. 依赖
org.springframework.boot：spring-boot-starter-amqp
2. 配置
```yaml
spring:
	rabbitmq:
		virtual-host: /
		host: 
		port:
		username:
		password:
```
3. 生产
   1. 生产者无需关心Queue信息
```java
rabbitTemplate.convertAndSend(exchange_name,route_key, "发送了一条信息");
```
4. Queue配置：消费者需要
```java
// durable是否持久化，及服务端保留队列
// excusive
// autodelete 是否自动删除，即没有一个消费者后关闭队列
    @Bean
    public Queue queue() {
        return new Queue(queue_name, durable, exclusive, autoDelete);
    }
    // 交换机模式又Direct，Fanout，Topic，Header，Consumer(自定义)
	@Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(exchange_name, durable, autoDelete);
    }

    // 绑定交互模式
	@Bean
    public Binding binding() {
		// return new Binding(queue_name, Binding.DestinationType.QUEUE,exchange_name, route_key, null);
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(route_key);
    }
```
5. 消费
```java
	@RabbitListener(queues = "${queue_name}")
    public void receive(Object...args) {
        // 具体消费逻辑
    }
```

## Spring Cloud Bus
1. 依赖
```xml
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-bus</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```
2. 配置rabbitmq信息
```yaml
spring:
  application:
    name: cloud-bus-demo
  rabbitmq:
    host: rabbitmq.dongle.com
    port: 5672
    username: guest
    password: guest
    virtual-host: /

# 暴露bus端口，用于发布和清空消息
management:
  endpoints:
    web:
      exposure:
        include: bus-env,bus-refresh
```
3. 自动生成主题Topic交换器形式的springCloudBus，和Queue(springCloudBus.anonymous.序列ID)
4. 广告事件
```java
public class MyEvent extends RemoteApplicationEvent {
}

@Configuration
//@RemoteApplicationEventScan({"com.acme", "foo.bar"})
//@RemoteApplicationEventScan(basePackages = {"com.acme", "foo.bar", "fizz.buzz"})
@RemoteApplicationEventScan(basePackageClasses = BusConfiguration.class)
public class BusConfiguration {
    ...
}
```