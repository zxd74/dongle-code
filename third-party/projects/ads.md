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
