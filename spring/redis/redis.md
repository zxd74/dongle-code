# Redis支持
```text
# 需要依赖spring框架
org.springframework.data:spring-data-redis

# 需要依赖spring-boot框架
org.springframework.boot:spring-boot-starter-data-redis

```
# 配置
```java

    @Resource(name = "redisTemplate")
	private ValueOperations<String, String> valOps; // 字符串key/value

	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOps; // 集合(不重复)

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOps; // hash

	@Resource(name="redisTemplate")
        private ZSetOperations<String, String> zsetOps;    // 排序集合
	
	@Resource(name = "redisTemplate")
	private ListOperations<String, String> listOps;    // 队列列表
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;  // 支持以上操作及其他操作

```
## 发布订阅
```java
// 需要当前类可以被spring扫描到，如加@Component
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        //监听精确频道(一个):
        listenerContainer.addMessageListener(addJdAdUpMqListener(), new ChannelTopic("精确订阅"));
        // 监听模糊频道（可能多个）:
        listenerContainer.addMessageListener(delJdAdUpMqListener(), new PatternTopic("模糊订阅"));
        return listenerContainer;
    }
    
    // 消息监听器
    public MessageListener addListener(){
        return (message, pattern) -> {
            try {
                // 消息处理
            }catch (Exception ex){
                LOGGER.error("订阅消息处理异常！",ex);
            }
        };
    }
    // 消息监听器
    public MessageListener delListener(){
        return (message, pattern) -> {
            try {
                // 消息处理
            }catch (Exception ex){
                LOGGER.error("订阅消息处理异常！",ex);
            }
        };
    }
    
    // 发布消息
    public void push(String channel,String message){
        redisTemplate.convertAndSend(channel,message);
    }
```