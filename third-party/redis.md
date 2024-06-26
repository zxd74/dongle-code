* 采用字符串序列化方式
* 通过JSON格式化数据
* 通过TypeReference转换数据（支持复杂类型的处理，如`List<Map<Object,Object)>`）
* 可以获取到缓存数据，在使用位置自行通过`Class`方式转换

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

```java
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Dongle
 * @desc
 * @since 2023/9/9 009 12:55
 */
@Component
public class RedisClient {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int DEFAULT_EXPIRE = -1;

    public <T> void set(String key,T value){
        set(key,value,DEFAULT_EXPIRE);
    }
    public <T> void set(String key, T value, long expire, TimeUnit timeUnit){
        set(key,value,timeUnit.toSeconds(expire));
    }
    public <T> void set(String key, T value, long expire){
        setEx(key, convert2Json(value),expire);
    }
    public <T> void set(String key, Caller<T> caller){
        set(key,caller,DEFAULT_EXPIRE);
    }
    public <T> void set(String key, Caller<T> caller, long expire, TimeUnit timeUnit){
        set(key,caller,timeUnit.toSeconds(expire));
    }
    public <T> void set(String key, Caller<T> caller, long expire){
        set(key,caller.apply(),expire);
    }
    public void set(String key,String value){
        setEx(key,value,DEFAULT_EXPIRE);
    }
    public void set(String key,String value, long expire, TimeUnit timeUnit){
        setEx(key,value,timeUnit.toSeconds(expire));
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public <T> T get(String key, TypeReference<T> typeReference){
        return convert2Object(redisTemplate.opsForValue().get(key),typeReference);
    }
    public <T> T getAndSet(String key, Caller<T> caller){
        return getAndSet(key,DEFAULT_EXPIRE,caller);
    }
    public <T> T getAndSet(String key, long expire, TimeUnit timeUnit, Caller<T> caller){
        return getAndSet(key,timeUnit.toSeconds(expire),caller);
    }
    public <T> T getAndSet(String key, long expire, Caller<T> caller){
        String obj = redisTemplate.opsForValue().get(key);
        if (obj == null){
            set(key,caller,expire);
        }
        return convert2Object(obj,new TypeReference<T>(){});
    }
    public String getAndDelete(String key){
        return redisTemplate.opsForValue().getAndDelete(key);
    }
    public <T> T getAndDelete(String key, TypeReference<T> typeReference){
        return convert2Object(redisTemplate.opsForValue().getAndDelete(key),typeReference);
    }


    public <T> void hSet(String hKey,String key,T value){
        redisTemplate.opsForHash().put(hKey,key,value);
    }
    public Object hGet(String key,String hKey){
        return redisTemplate.opsForHash().get(key,hKey);
    }
    public <T> T hGet(String key,String hKey, TypeReference<T> typeReference){
        return convert2Object((String)hGet(key,hKey),typeReference);
    }
    public Map<Object, Object> hall(String key){
        return redisTemplate.opsForHash().entries(key);
    }
    public <T> T hall(String key,TypeReference<T> typeReference){
        return JSON.parseObject(JSON.toJSONString(redisTemplate.opsForHash().entries(key)),typeReference);
    }
    public <T> T hGetAndSet(String key,String hKey,T value){
        Object obj = hGet(key,hKey);
        if (obj == null){
            hSet(key,hKey,value);
            return value;
        }
        return convert2Object((String)obj,new TypeReference<T>(){});
    }
    public <T> T hGetAndSet(String key,String hKey,Caller<T> caller){
        Object obj = hGet(key,hKey);
        if (obj == null){
            T value = caller.apply();
            hSet(key,hKey,value);
            return value;
        }
        return convert2Object((String)obj,new TypeReference<T>(){});
    }


    public void setEx(String key,long expire){
        setEx(key,expire,TimeUnit.SECONDS);
    }
    public void setEx(String key,String value,long expire){
        redisTemplate.opsForValue().set(key,value,expire);
    }
    public void setEx(String key,long expire,TimeUnit timeUnit){
        redisTemplate.expire(key,expire,timeUnit);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    // 发现还是通过TypeReference方便转换数据，否则传递class，转换数组数据非常麻烦，不利于提取
    private <T> T convert2Object(String str, TypeReference<T> typeReference){
        return JSON.parseObject(str,typeReference);
    }

    private <T> String convert2Json(T value){
        return JSON.toJSONString(value);
    }
}
```