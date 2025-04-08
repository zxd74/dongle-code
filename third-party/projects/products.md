# 数据库设计
```sql
CREATE TABLE `products` (
  `product_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_code` VARCHAR(64) NOT NULL COMMENT '商品编码',
  `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
  `category_id` INT UNSIGNED NOT NULL COMMENT '分类ID',
  `brand_id` INT UNSIGNED DEFAULT NULL COMMENT '品牌ID',
  `short_description` VARCHAR(500) DEFAULT NULL COMMENT '商品简介',
  `full_description` TEXT COMMENT '商品详情',
  `price` DECIMAL(15,2) NOT NULL COMMENT '销售价',
  `cost_price` DECIMAL(15,2) COMMENT '成本价',
  `market_price` DECIMAL(15,2) COMMENT '市场价',
  `weight` DECIMAL(10,3) DEFAULT 0 COMMENT '重量(kg)',
  `length` DECIMAL(10,2) COMMENT '长度(cm)',
  `width` DECIMAL(10,2) COMMENT '宽度(cm)',
  `height` DECIMAL(10,2) COMMENT '高度(cm)',
  `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `min_stock_quantity` INT DEFAULT 0 COMMENT '最小库存阈值',
  `sales_count` INT DEFAULT 0 COMMENT '销售数量',
  `view_count` INT DEFAULT 0 COMMENT '浏览数量',
  `is_published` TINYINT(1) DEFAULT 0 COMMENT '是否上架(1是0否)',
  `is_featured` TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
  `is_bestseller` TINYINT(1) DEFAULT 0 COMMENT '是否热销',
  `is_new` TINYINT(1) DEFAULT 1 COMMENT '是否新品',
  `seo_title` VARCHAR(255) DEFAULT NULL COMMENT 'SEO标题',
  `seo_keywords` VARCHAR(255) DEFAULT NULL COMMENT 'SEO关键词',
  `seo_description` VARCHAR(500) DEFAULT NULL COMMENT 'SEO描述',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `idx_product_code` (`product_code`),
  KEY `idx_category` (`category_id`),
  KEY `idx_brand` (`brand_id`),
  KEY `idx_published` (`is_published`),
  KEY `idx_featured` (`is_featured`),
  KEY `idx_sales` (`sales_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品基本信息表';

CREATE TABLE `categories` (
  `category_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` INT UNSIGNED DEFAULT 0 COMMENT '父分类ID',
  `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `category_path` VARCHAR(255) DEFAULT NULL COMMENT '分类路径(如1/2/3)',
  `level` TINYINT UNSIGNED DEFAULT 1 COMMENT '分类层级',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
  `seo_title` VARCHAR(255) DEFAULT NULL COMMENT 'SEO标题',
  `seo_keywords` VARCHAR(255) DEFAULT NULL COMMENT 'SEO关键词',
  `seo_description` VARCHAR(500) DEFAULT NULL COMMENT 'SEO描述',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_path` (`category_path`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

CREATE TABLE `brands` (
  `brand_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `brand_name` VARCHAR(100) NOT NULL COMMENT '品牌名称',
  `brand_logo` VARCHAR(255) DEFAULT NULL COMMENT '品牌Logo',
  `website` VARCHAR(255) DEFAULT NULL COMMENT '品牌官网',
  `description` TEXT COMMENT '品牌描述',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  `is_featured` TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
  `seo_title` VARCHAR(255) DEFAULT NULL COMMENT 'SEO标题',
  `seo_keywords` VARCHAR(255) DEFAULT NULL COMMENT 'SEO关键词',
  `seo_description` VARCHAR(500) DEFAULT NULL COMMENT 'SEO描述',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`brand_id`),
  KEY `idx_name` (`brand_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌表';

CREATE TABLE `product_images` (
  `image_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `thumbnail_url` VARCHAR(255) DEFAULT NULL COMMENT '缩略图URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  `is_main` TINYINT(1) DEFAULT 0 COMMENT '是否主图',
  `alt_text` VARCHAR(255) DEFAULT NULL COMMENT '替代文本',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`image_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

CREATE TABLE `product_attributes` (
  `attribute_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `attribute_name` VARCHAR(100) NOT NULL COMMENT '属性名称',
  `attribute_code` VARCHAR(64) NOT NULL COMMENT '属性代码',
  `attribute_type` ENUM('text','select','multiselect','boolean','decimal','integer') NOT NULL DEFAULT 'text' COMMENT '属性类型',
  `is_required` TINYINT(1) DEFAULT 0 COMMENT '是否必填',
  `is_filterable` TINYINT(1) DEFAULT 0 COMMENT '是否可筛选',
  `is_visible` TINYINT(1) DEFAULT 1 COMMENT '是否可见',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  `note` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`attribute_id`),
  UNIQUE KEY `idx_code` (`attribute_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品属性表';

CREATE TABLE `product_attribute_values` (
  `value_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '值ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `attribute_id` INT UNSIGNED NOT NULL COMMENT '属性ID',
  `text_value` TEXT COMMENT '文本值',
  `boolean_value` TINYINT(1) DEFAULT NULL COMMENT '布尔值',
  `integer_value` INT DEFAULT NULL COMMENT '整数值',
  `decimal_value` DECIMAL(12,4) DEFAULT NULL COMMENT '小数值',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`value_id`),
  UNIQUE KEY `idx_product_attribute` (`product_id`,`attribute_id`),
  KEY `idx_attribute` (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品属性值表';

-- SKU (Stock Keeping Unit，库存量单位) 是零售和电商领域最重要的商品管理概念之一，指商家为区分商品具体属性组合而设定的最小库存管理单元。
CREATE TABLE `product_skus` (
  `sku_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `sku_code` VARCHAR(64) NOT NULL COMMENT 'SKU编码',
  `barcode` VARCHAR(64) DEFAULT NULL COMMENT '条形码',
  `price` DECIMAL(15,2) NOT NULL COMMENT '销售价',
  `cost_price` DECIMAL(15,2) DEFAULT NULL COMMENT '成本价',
  `market_price` DECIMAL(15,2) DEFAULT NULL COMMENT '市场价',
  `weight` DECIMAL(10,3) DEFAULT NULL COMMENT '重量(kg)',
  `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `low_stock_threshold` INT DEFAULT 5 COMMENT '低库存阈值',
  `image_id` BIGINT UNSIGNED DEFAULT NULL COMMENT 'SKU图片',
  `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否默认SKU',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sku_id`),
  UNIQUE KEY `idx_sku_code` (`sku_code`),
  UNIQUE KEY `idx_barcode` (`barcode`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SKU表';

CREATE TABLE `sku_combinations` (
  `combination_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组合ID',
  `sku_id` BIGINT UNSIGNED NOT NULL COMMENT 'SKU ID',
  `attribute_id` INT UNSIGNED NOT NULL COMMENT '属性ID',
  `attribute_value_id` INT UNSIGNED DEFAULT NULL COMMENT '属性值ID(可选)',
  `value_text` VARCHAR(255) DEFAULT NULL COMMENT '属性值文本',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`combination_id`),
  UNIQUE KEY `idx_sku_attribute` (`sku_id`,`attribute_id`),
  KEY `idx_attribute` (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU属性组合表';

CREATE TABLE `product_category_relations` (
  `relation_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `category_id` INT UNSIGNED NOT NULL COMMENT '分类ID',
  `is_main_category` TINYINT(1) DEFAULT 0 COMMENT '是否主分类',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `idx_product_category` (`product_id`,`category_id`),
  KEY `idx_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类关联表';

CREATE TABLE `product_tags` (
  `tag_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `tag_code` VARCHAR(50) NOT NULL COMMENT '标签代码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '标签描述',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `idx_tag_code` (`tag_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品标签表';

CREATE TABLE `product_tag_relations` (
  `relation_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `tag_id` INT UNSIGNED NOT NULL COMMENT '标签ID',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `idx_product_tag` (`product_id`,`tag_id`),
  KEY `idx_tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品标签关联表';

CREATE TABLE `product_reviews` (
  `review_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `customer_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '客户ID',
  `rating` TINYINT UNSIGNED NOT NULL COMMENT '评分(1-5)',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '评价标题',
  `content` TEXT COMMENT '评价内容',
  `is_approved` TINYINT(1) DEFAULT 0 COMMENT '是否审核通过',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`review_id`),
  KEY `idx_product` (`product_id`),
  KEY `idx_customer` (`customer_id`),
  KEY `idx_approved` (`is_approved`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品评价表';
```
* 索引优化
  * 高频查询字段(组)建立索引
  * 对商品名称，描述等文档可建立全文索引
    ```sql
    ALTER TABLE products ADD FULLTEXT INDEX `ft_idx_name_desc` (`product_name`, `short_description`);
    ```
* 扩展建议
  * 分区策略：根据商品ID进行分区，每个分区存储一定范围内的商品数据，提高查询性能。
    ```sql
    ALTER TABLE products PARTITION BY RANGE (category_id) (
        PARTITION p0 VALUES LESS THAN (100),
        PARTITION p1 VALUES LESS THAN (200),
        PARTITION p2 VALUES LESS THAN MAXVALUE
    );
    ```
  * 归档策略：对下架时间超过1年的商品可迁移到归档表
  * 缓存层
