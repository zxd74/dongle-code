# ADX
创建一个adx广告系统，要求支持以下功能：
1. 支持多种广告类型，如banner、插屏、开屏、视频等；
2. 支持多种广告位，如首页、详情页、分类页等；
3. 支持多种广告投放策略，如按CPM、CPC、CPA等计费方式；
4. 支持多种广告位竞价策略，如固价，第一竞价、第二竞价、第三竞价等；
5. 支持多种广告投放平台，如Facebook、Google、Twitter等；
6. 支持多种广告投放形式，如原生广告、横幅广告、插屏广告等；
7. 支持多种广告投放渠道，如移动应用、网页、桌面应用等；
8. 支持多种广告投放场景，如首页、详情页、分类页等；
9. 支持多种广告投放时间，如实时投放、定时投放等；
10. 支持多种广告投放地域，如全国、省份、城市等；
11. 支持多种广告投放人群，如年龄、性别、兴趣等；
12. 支持多种广告投放设备，如手机、平板、电脑等；
13. 支持多种广告投放网络，如2G、3G、4G、5G等；
14. 支持多种广告投放语言，如中文、英文、日文等；
15. 支持多种广告投放货币，如人民币、美元、日元等；
16. 支持多种广告投放尺寸，如320x50、300x250、728x90等；
17. 支持多种广告投放样式，如图片、文字、视频等；
18. 支持多种广告投放效果，如点击率、转化率、曝光率等；
19. 支持多种广告投放统计，如曝光量、点击量、转化量等；
20. 支持多种广告投放优化，如自动出价、智能投放等；
21. 支持多种广告投放管理，如广告审核、广告投放、广告暂停等。
22. 支持多种广告投放效果评估，如ROI、CVR、CTR等；
23. 支持多种广告投放预算管理，如预算设置、预算调整、预算监控等；
24. 支持多种广告投放数据分析，如数据报表、数据可视化等；
25. 支持多种广告投放广告主管理，如广告主注册、广告主审核、广告主管理等；
26. 支持多种广告投放广告位管理，如广告位注册、广告位审核、广告位管理等；
27. 支持多种广告投放广告素材管理，如广告素材上传、广告素材审核、广告素材管理等；
28. 支持多种广告投放广告投放管理，如广告投放策略设置、广告投放效果监控、广告投放暂停等；
29. 支持多种广告投放广告主服务，如广告主服务注册、广告主服务审核、广告主服务管理等；
30. 支持多种广告投放广告位服务，如广告位服务注册、广告位服务审核、广告位服务管理等；
31. 支持多种广告投放广告素材服务，如广告素材服务注册、广告素材服务审核、广告素材服务管理等；
32. 支持多种广告投放广告投放服务，如广告投放服务注册、广告投放服务审核、广告投放服务管理等；
33. 支持多种广告投放广告主服务管理，如广告主服务注册、广告主服务审核、广告主服务管理等；
34. 支持多种广告投放广告位服务管理，如广告位服务注册、广告位服务审核、广告位服务管理等；
35. 支持多种广告投放广告素材服务管理，如广告素材服务注册、广告素材服务审核、广告素材服务管理等；
36. 支持多种广告投放广告投放服务管理，如广告投放服务注册、广告投放服务审核、广告投放服务管理等；
37. 支持多种广告投放广告主服务统计，如广告主服务曝光量、广告主服务点击量、广告主服务转化量等；
38. 支持多种广告投放广告位服务统计，如广告位服务曝光量、广告位服务点击量、广告位服务转化量等；
39. 支持多种广告投放广告素材服务统计，如广告素材服务曝光量、广告素材服务点击量、广告素材服务转化量等；

额外要求提供数据库设计，包括表结构、字段类型、字段含义、索引等。

## 数据库设计
```sql
CREATE TABLE advertisers (
    advertiser_id INT AUTO_INCREMENT PRIMARY KEY,
    advertiser_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status ENUM('active', 'inactive') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_advertiser_name (advertiser_name)
); -- 广告主表
CREATE TABLE ad_slots (
    ad_slot_id INT AUTO_INCREMENT PRIMARY KEY,
    slot_name VARCHAR(255) NOT NULL,
    page_type ENUM('homepage', 'detailpage', 'categorypage') NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_slot_name (slot_name)
); -- 广告位表
CREATE TABLE ad_materials (
    ad_material_id INT AUTO_INCREMENT PRIMARY KEY,
    advertiser_id INT NOT NULL,
    ad_type ENUM('banner', 'interstitial', 'splash', 'video') NOT NULL,
    ad_style ENUM('image', 'text', 'video') NOT NULL,
    ad_size VARCHAR(20) NOT NULL,
    ad_language VARCHAR(10) NOT NULL,
    ad_content TEXT NOT NULL,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (advertiser_id) REFERENCES advertisers(advertiser_id),
    INDEX idx_ad_type (ad_type)
); -- 广告素材表
CREATE TABLE ad_strategies (
    ad_strategy_id INT AUTO_INCREMENT PRIMARY KEY,
    advertiser_id INT NOT NULL,
    ad_slot_id INT NOT NULL,
    pricing_model ENUM('CPM', 'CPC', 'CPA') NOT NULL,
    bidding_strategy ENUM('fixed_price', 'first_price', 'second_price', 'third_price') NOT NULL,
    budget DECIMAL(10, 2) NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    targeting_age VARCHAR(50),
    targeting_gender ENUM('male', 'female', 'both'),
    targeting_interest VARCHAR(255),
    targeting_region VARCHAR(255),
    targeting_device ENUM('mobile', 'tablet', 'desktop'),
    targeting_network ENUM('2G', '3G', '4G', '5G'),
    status ENUM('active', 'paused', 'completed') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (advertiser_id) REFERENCES advertisers(advertiser_id),
    FOREIGN KEY (ad_slot_id) REFERENCES ad_slots(ad_slot_id),
    INDEX idx_pricing_model (pricing_model)
);  -- 广告策略表
CREATE TABLE ad_impressions (
    impression_id INT AUTO_INCREMENT PRIMARY KEY,
    ad_strategy_id INT NOT NULL,
    ad_material_id INT NOT NULL,
    ad_slot_id INT NOT NULL,
    platform ENUM('Facebook', 'Google', 'Twitter') NOT NULL,
    channel ENUM('mobile_app', 'web', 'desktop_app') NOT NULL,
    impression_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_clicked BOOLEAN DEFAULT FALSE,
    conversion_type VARCHAR(50),
    conversion_time TIMESTAMP,
    currency VARCHAR(10) NOT NULL,
    cost DECIMAL(10, 2),
    FOREIGN KEY (ad_strategy_id) REFERENCES ad_strategies(ad_strategy_id),
    FOREIGN KEY (ad_material_id) REFERENCES ad_materials(ad_material_id),
    FOREIGN KEY (ad_slot_id) REFERENCES ad_slots(ad_slot_id),
    INDEX idx_impression_time (impression_time)
); -- 广告曝光表
CREATE TABLE ad_reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    ad_strategy_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    impressions INT DEFAULT 0,
    clicks INT DEFAULT 0,
    conversions INT DEFAULT 0,
    ctr DECIMAL(5, 2) DEFAULT 0.00,
    cvr DECIMAL(5, 2) DEFAULT 0.00,
    roi DECIMAL(5, 2) DEFAULT 0.00,
    total_cost DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (ad_strategy_id) REFERENCES ad_strategies(ad_strategy_id),
    INDEX idx_start_date (start_date)
); -- 广告报告表
```

# 业务设计
## 竞价模块
1. 分层架构
```txt
graph TD
    A[API层] --> B[业务逻辑层]
    B --> C[竞价引擎]
    C --> D[规则引擎]
    B --> E[缓存层]
    E --> F[数据库]
```
2. 核心实现
   1. 竞价策略接口设计（策略模式）
    ```java
    // 竞价策略接口
    public interface BiddingStrategy {
        /**
         * @param bidRequest 竞价请求
         * @param context 竞价上下文（包含市场数据等）
         * @return 竞价结果（包含出价和元数据）
         */
        BidResult evaluate(BidRequest bidRequest, BiddingContext context);
    }

    // 示例策略1：CPM竞价
    public class CPMStrategy implements BiddingStrategy {
        @Override
        public BidResult evaluate(BidRequest request, BiddingContext context) {
            double basePrice = request.getImpressionPrice();
            double adjustedPrice = basePrice * context.getMarketCompetitionFactor();
            return new BidResult(request.getAdId(), adjustedPrice);
        }
    }

    // 示例策略2：CPC竞价（带质量控制）
    public class CPCStrategy implements BiddingStrategy {
        @Override
        public BidResult evaluate(BidRequest request, BiddingContext context) {
            double qualityScore = calculateQualityScore(request.getAdCreative());
            double bidPrice = request.getClickPrice() * qualityScore;
            return new BidResult(request.getAdId(), bidPrice);
        }
        
        private double calculateQualityScore(AdCreative creative) {
            // 质量评估逻辑
        }
    }
    ```
   2.  竞价引擎核心（工厂模式 + 责任链）
    ```java
    public class BiddingEngine {
        private final Map<BidType, BiddingStrategy> strategies;
        private final BidRuleEngine ruleEngine;

        // 通过依赖注入初始化
        public BiddingEngine(
            Map<BidType, BiddingStrategy> strategies,
            BidRuleEngine ruleEngine
        ) {
            this.strategies = strategies;
            this.ruleEngine = ruleEngine;
        }

        public BidResponse process(BidRequest request) {
            // 1. 基础验证
            if (!validateRequest(request)) {
                return BidResponse.error("Invalid request");
            }

            // 2. 并行获取候选广告（高性能关键点）
            List<AdCandidate> candidates = fetchCandidatesAsync(request);

            // 3. 规则过滤
            List<AdCandidate> filtered = ruleEngine.applyRules(candidates);

            // 4. 策略竞价
            BidResult winner = filtered.parallelStream()
                .map(ad -> {
                    BiddingStrategy strategy = strategies.get(ad.getBidType());
                    return strategy.evaluate(request, ad);
                })
                .max(Comparator.comparingDouble(BidResult::getPrice))
                .orElse(null);

            // 5. 构建响应
            return buildResponse(winner);
        }
    }
    ```
    3. 高性能优化关键代码
    ```java
    // 使用异步非阻塞IO获取广告候选
    public List<AdCandidate> fetchCandidatesAsync(BidRequest request) {
        CompletableFuture<List<AdCandidate>> dbFuture = CompletableFuture.supplyAsync(
            () -> adRepository.findByTargeting(request.getTargeting()),
            dbExecutor); // 专用线程池

        CompletableFuture<List<AdCandidate>> cacheFuture = CompletableFuture.supplyAsync(
            () -> cacheService.getHotAds(request.getUserSegment()),
            cacheExecutor);

        return Stream.of(dbFuture, cacheFuture)
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    // 基于Caffeine的本地缓存
    public class AdCache {
        private final Cache<String, List<AdCandidate>> cache;

        public AdCache() {
            this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
        }

        public List<AdCandidate> get(String key) {
            return cache.get(key, k -> loadFromDB(k));
        }
    }
    ```
1. 扩展性设计方案
   1. 动态规则引擎（Drools实现示例）
    ```java
    // 规则配置化示例
    rule "HighValueUserRule"
        when
            $request : BidRequest(user.valueTier > 8)
            $ad : AdCandidate(budget > 10000)
        then
            insert(new BidBoost($ad.getId(), 1.2)); // 高价用户提升20%出价
    end

    // 规则引擎执行
    public class BidRuleEngine {
        private KieContainer kieContainer;

        public List<AdCandidate> applyRules(List<AdCandidate> candidates) {
            KieSession session = kieContainer.newKieSession();
            candidates.forEach(session::insert);
            session.fireAllRules();
            
            return candidates.stream()
                .filter(ad -> !ad.isFiltered())
                .collect(Collectors.toList());
        }
    }
    ```
   2. 策略动态加载（SPI机制）
    ```txt
    # resources/META-INF/services/com.xxx.BiddingStrategy
    com.xxx.CPMStrategy
    com.xxx.CPCStrategy
    com.xxx.CPAStrategy
    ```
    ```java
    // 策略自动发现
    ServiceLoader<BiddingStrategy> loader = ServiceLoader.load(BiddingStrategy.class);
    Map<BidType, BiddingStrategy> strategies = loader.stream()
        .collect(Collectors.toMap(
            p -> p.get().getSupportedType(),
            ServiceLoader.Provider::get
        ));
    ```
4. 优化手段
   1. 缓存分层：
      * 本地缓存（Caffeine）：存储高频广告
      * Redis集群：存储实时竞价数据
      * 数据库分片：广告主数据按ID哈希分片
   2. 异步处理：
    ```java
    // 使用Disruptor实现高并发事件处理
    public class BidEventProcessor {
        private final Disruptor<BidEvent> disruptor;
        
        public void onEvent(BidEvent event) {
            // 异步记录竞价日志
            logQueue.add(event);
            // 实时统计
            statsAggregator.record(event);
        }
    }
    ```
   3. 实时监控：
    ```java
    // Micrometer指标监控
    public class BiddingMetrics {
        private final Counter requestCounter;
        private final Timer processingTimer;
        
        public void recordSuccess(long latency) {
            requestCounter.increment();
            processingTimer.record(latency, TimeUnit.MILLISECONDS);
        }
    }
    ```
5. 数据库设计建议：
```sql
-- 广告主表（分库键）
CREATE TABLE advertiser (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    budget DECIMAL(20,2),
    balance DECIMAL(20,2)
) PARTITION BY HASH(id) PARTITIONS 16;

-- 广告计划表（按时间分区）
CREATE TABLE ad_campaign (
    id BIGINT PRIMARY KEY,
    advertiser_id BIGINT,
    bid_type ENUM('CPM','CPC','CPA'),
    start_time DATETIME,
    end_time DATETIME,
    INDEX idx_time (start_time, end_time)
) PARTITION BY RANGE (TO_DAYS(start_time)) (
    PARTITION p202301 VALUES LESS THAN (TO_DAYS('2023-02-01')),
    PARTITION p202302 VALUES LESS THAN (TO_DAYS('2023-03-01'))
);
```
6. 关键设计思想
   * 策略模式：支持灵活扩展新竞价类型
   * 规则引擎：实现业务逻辑与代码解耦
   * 异步并行：最大化利用多核CPU
   * 分层缓存：减少数据库访问压力
   * 动态加载：支持不停机更新策略
