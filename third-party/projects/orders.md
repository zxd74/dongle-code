# 数据库设计
```sql
CREATE TABLE user (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL, -- 加密存储
  email VARCHAR(100) UNIQUE NOT NULL,
  phone VARCHAR(20),
  address TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP
); -- 用户表 (user)

CREATE TABLE product_category (
  category_id INT PRIMARY KEY AUTO_INCREMENT,
  category_name VARCHAR(50) NOT NULL,
  parent_category_id INT, -- 支持多级分类
  description TEXT
); -- 商品分类表 (product_category)

CREATE TABLE product (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  product_name VARCHAR(200) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  stock INT DEFAULT 0,
  category_id INT NOT NULL,
  is_available BOOLEAN DEFAULT TRUE, -- 上下架状态
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES product_category(category_id)
); -- 商品表 (product)

CREATE TABLE `order` ( -- 需转义保留字
  order_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  order_no VARCHAR(50) UNIQUE NOT NULL, -- 唯一订单号（如：20230801-123456）
  total_amount DECIMAL(10,2) NOT NULL,
  order_status ENUM('pending', 'paid', 'shipped', 'completed', 'cancelled') DEFAULT 'pending',
  payment_method VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id)
); -- 订单表 (order)

CREATE TABLE order_item (
  order_item_id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL, -- 下单时的价格快照
  FOREIGN KEY (order_id) REFERENCES `order`(order_id),
  FOREIGN KEY (product_id) REFERENCES product(product_id)
); -- 订单项表 (order_item)

CREATE TABLE payment (
  payment_id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  transaction_no VARCHAR(100), -- 第三方支付流水号
  payment_status ENUM('pending', 'success', 'failed') DEFAULT 'pending',
  payment_time TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES `order`(order_id)
); -- 支付表 (payment)

CREATE TABLE shipping (
  shipping_id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  tracking_no VARCHAR(50),
  carrier VARCHAR(50), -- 快递公司
  shipping_status ENUM('preparing', 'shipped', 'in_transit', 'delivered'),
  estimated_delivery DATE,
  actual_delivery TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES `order`(order_id)
); -- 物流表 (shipping)
```
* 关键索引建议
```sql
-- 用户表
CREATE INDEX idx_user_email ON user(email);
CREATE INDEX idx_user_phone ON user(phone);

-- 商品表
CREATE INDEX idx_product_name ON product(product_name);
CREATE INDEX idx_product_price ON product(price);

-- 订单表
CREATE INDEX idx_order_user ON `order`(user_id);
CREATE INDEX idx_order_status ON `order`(order_status);
CREATE INDEX idx_order_create_time ON `order`(created_at);

-- 支付表
CREATE INDEX idx_payment_order ON payment(order_id);
```
* 优化建议
  * 分表分库：当订单量较大时，可按时间范围进行水平分表（如按年分表）
  * 数据归档：定期将已完成订单迁移到历史表
  * 缓存机制：使用Redis缓存高频访问数据（如商品信息、用户基础信息）
  * 库存控制：通过乐观锁（版本号字段）或数据库事务保证库存准确性
  * 敏感数据加密：对手机号、地址等字段进行加密存储
* 扩展功能表建议
  * 购物车表 (cart)
  * 优惠券表 (coupon)
  * 评论表 (product_review)
  * 退货表 (order_return)


# 秒杀活动
```java
/** 活动结果 */
public static class SeckillResult{
    private final String activityId;
    private final String goodsName;
    private final int userId;
    private boolean isSuccess;
    private String msg;
    
    public SeckillResult(String activityId, String goodsName, int userId) {
        this.activityId = activityId;
        this.goodsName = goodsName;
        this.userId = userId;
    }
    // getter/setter
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
/** 单个秒杀活动实体 */
public static class SeckillActivity {
    // 活动唯一标识、商品名
    private final String activityId;
    private final String goodsName;
    // 剩余库存
    private final AtomicInteger stock;
    // 活动启停标记
    private volatile boolean isStart = false;

    public SeckillActivity(String activityId, String goodsName, int initStock) {
        this.activityId = activityId;
        this.goodsName = goodsName;
        this.stock = new AtomicInteger(initStock);
    }

    /** 后台开启秒杀 */
    public void openSeckill() {
        isStart = true;
        System.out.printf("\n>>> 活动[%s]-%s 正式开启秒杀\n", activityId, goodsName);
    }

    /** 用户发起抢购，未开启直接失败，不阻塞 */
    public SeckillResult userBuy(int userId) {
        SeckillResult result = new SeckillResult(activityId,goodsName,userId);
        
        if (!isStart) {// 未开始直接返回失败
            System.out.printf("用户%d：%s秒杀尚未开始，抢购失败\n", userId, goodsName);
            result.setSuccess(false);
            result.setMsg("未开始");
            return result;
        }
        while (true) {
            int current = stock.get(); // 判断当前库存
            if (current <= 0) {
                System.out.printf("用户%d → %s：已售罄\n", userId, goodsName);
                result.setSuccess(false);
                result.setMsg("已售完");
                return result;
            }
            
            if (stock.compareAndSet(current, current - 1)) { // CAS 原子扣减
                System.out.printf("用户%d → %s：成功！库存剩余：%d\n", userId, goodsName, current - 1);
                result.setSuccess(true);
                return result;
            }
            // 并发冲突，递归重试（也可以用自旋）
        }
    }
}
// 后台创建并返回秒杀活动
public static SeckillActivity createSeckillActivity(String goodsName, int stock) {
  System.out.printf("【后台】创建商品：%s，库存：%d\n", goodsName, stock);
  return new SeckillActivity(goodsName, stock);
}
// 用户手动参与秒杀（外部随时调用）
public static void userJoinSeckill(SeckillActivity activity, int userId) {
  new Thread(() -> {
  SeckillResult result = activity.userBuy(userId);// 根据抢购结果处理
  }, "用户-" + userId).start();
}
public static void main(String[] args) throws InterruptedException {
    // 创建多个独立秒杀活动
    SeckillActivity phone = createSeckillActivity("小米14", 5);
    SeckillActivity watch = createSeckillActivity("华为手表", 3);

    System.out.println("此时两个活动均未开启，用户无法参与抢购");
    userJoinSeckill(phoneActivity, 100);
    userJoinSeckill(watchActivity, 101);
    Thread.sleep(1500);

    // 后台分时段开启不同秒杀
    phone.openSeckill();
    watch.openSeckill();
    Thread.sleep(1500);
    System.out.println("两个活动均已开启，后续用户可参与抢购");

    // 模拟批量用户，随机参与不同商品抢购(可参与抢购)
    for (int i = 1; i <= 7; i++) {
        userJoinSeckill(phoneActivity, i);
    }
}
```
## 优化
1. 算法
  - 安全+CAS自旋：存在间隙，明明存在订单但无法抢到(CAS无锁并发最小代价)
  - 安全+Lock：绝对公平，但性能下降，高并发阻塞，秒杀雪崩
- 真实秒杀逻辑
  - 不能直接请求数据库
  - 流量必须层层过滤
  - 超卖绝对不允许
  - 公平性可牺牲，速度和稳定性第一
  - 库存只在内存算，不扣数据库
- 企业级流程
  1. 预热：将库存加载到缓存(redis)
  2. 前端拦截
    - 按钮倒计时
    - 按钮点击限流
    - 人机验证
  3. redis抢资格：redis+lua原子性，无并发问题
    ```lua
    -- Lua 脚本：原子判断+扣减
    local stock = tonumber(redis.call("get", KEYS[1]))
    if stock <= 0 then
        return 0  -- 售罄
    end
    redis.call("decr", KEYS[1])
    return 1      -- 抢到资格
    ```
  4. 抢到资格 → 生成订单（异步）
     1. 记录用户已抢，防重复
     2. 异步发送消息队列
     3. 后端消费者慢慢写数据库
     4. 前端直接提示"抢购成功"
  5. 兜底：数据库最终一致性，**定时任务核对** Redis 库存和数据库库存，确保一致。
- 真实秒杀技术栈（企业标准）
  - 库存：Redis + Lua
  - 限流：Redis + 漏斗 / 令牌桶
  - 异步：MQ 异步下单
  - 防刷：用户限频、黑名单
  - 隔离：秒杀独立服务、独立数据库、独立缓存
  - 兜底：降级、熔断、排队

### Spring Boot + Redis + Lua
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
* `seckill.lua`
```lua
-- Redis Lua 原子秒杀脚本（企业真实使用）
-- KEYS[1]: 库存key
-- KEYS[2]: 已购买用户key
-- ARGV[1]: 用户ID

-- 1. 判断库存
local stock = tonumber(redis.call("get", KEYS[1]))
if stock <= 0 then
    return 0  -- 库存不足
end

-- 2. 判断用户是否重复抢购
local userExists = redis.call("sismember", KEYS[2], ARGV[1])
if userExists == 1 then
    return -1 -- 重复抢购
end

-- 3. 原子扣库存 + 记录用户
redis.call("decr", KEYS[1])
redis.call("sadd", KEYS[2], ARGV[1])

return 1 -- 抢购成功
```
```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    // 加载Lua脚本
    @Bean
    public DefaultRedisScript<Long> seckillScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("seckill.lua")); // 绑定lua脚本
        script.setResultType(Long.class);
        return script;
    }
}

@Service
public class SeckillService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Long> seckillScript;

    // 预热秒杀活动（后台创建）
    public void initSeckill(String goodsId, int stock) {
        redisTemplate.opsForValue().set("seckill:stock:" + goodsId, String.valueOf(stock));
        redisTemplate.delete("seckill:user:" + goodsId);
        System.out.println("秒杀活动已预热，商品：" + goodsId + "，库存：" + stock);
    }

    // 真实秒杀接口
    public String doSeckill(String goodsId, String userId) {
        List<String> keys = Arrays.asList(
            "seckill:stock:" + goodsId,  // 库存key
            "seckill:user:" + goodsId    // 已购用户key
        );

        // 执行Lua原子脚本
        Long result = redisTemplate.execute(seckillScript, keys, userId);

        if (result == 1) {
            System.out.println("用户" + userId + "抢购成功！商品：" + goodsId);
            // 发送MQ异步生成订单（不阻塞秒杀）
            return "success";
        } else if (result == 0) {
            return "sold_out"; // 售罄
        } else if (result == -1) {
            return "repeat";   // 重复抢购
        } else {
            return "fail";
        }
    }
}

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    // 后台预热秒杀
    @GetMapping("/init")
    public String init(String goodsId, Integer stock) {
        seckillService.initSeckill(goodsId, stock);
        return "秒杀活动创建成功";
    }

    // 用户抢购
    @GetMapping("/do")
    public String seckill(String goodsId, String userId) {
        return seckillService.doSeckill(goodsId, userId);
    }
}
```