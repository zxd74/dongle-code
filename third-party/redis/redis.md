# Jedis(java)
```gradle
    redis.clients:jedis:3.3.0
```
## 配置
```java
private static final JedisPool JEDIS_POOL;
static{
        String host = "db.dongle.com" ; //properties.getProperty("redis.host");
        String port =  "6379" ; //properties.getProperty("redis.port");
        String pass =  "123456" ; //properties.getProperty("redis.pass");
        String timeout =  "3000" ; //properties.getProperty("redis.timeout");
        String maxIdle =  "" ; //properties.getProperty("redis.maxIdle");
        String maxTotal =  "" ; //properties.getProperty("redis.maxTotal");
        String maxWaitMillis =  "" ; //properties.getProperty("redis.maxWaitMillis");
        String testOnBorrow =  "" ; //properties.getProperty("redis.testOnBorrow");
        String db = "0"; // properties.getProperty("redis.db"); //默认0

        JedisPoolConfig config = new JedisPoolConfig();
        ////控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(maxTotal));
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(maxIdle));
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(Boolean.valueOf(testOnBorrow));

        JEDIS_POOL = new JedisPool(config, host, Integer.parseInt(port), Integer.parseInt(timeout), pass);
}

```
## 发布订阅
1. 发布：jedis.publish(channel, message);
2. 订阅：jedis.subscribe(new StringJedisPubSub(),channel);
  a. 分为JedisPubSub订阅和BinaryJedisPubSub二进制订阅
```java
public static class StringJedisPubSub extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        //super.onMessage(channel, message);
        System.out.println("channel:" + channel + ",message:" + message);
    }
}

public static void main(String[] args) {
	Jedis jedis = JedisUtils.getJedis();
	if (jedis == null){
		return;
	}
	new Thread(()->{
            // 客户端消息订阅
	    jedis.subscribe(new StringJedisPubSub(),"channel");
	}).start();
        // 生产者发布消息 channel要保证匹配，或者模式匹配，或者精确匹配
        jedis.publish("channel", "message");
}

```
# spring 支持
见spring-redis.