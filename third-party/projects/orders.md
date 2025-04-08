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
