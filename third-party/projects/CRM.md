# 系统描述
CRM系统主要涉及客户信息、互动记录、销售机会、服务管理等模块。

# 数据库设计
```sql
CREATE TABLE customer (
  customer_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_name VARCHAR(100) NOT NULL, -- 公司名称/个人姓名
  customer_type ENUM('enterprise', 'individual') NOT NULL,
  industry VARCHAR(50), -- 所属行业
  source ENUM('website', 'referral', 'event', 'cold_call'), -- 客户来源
  credit_rating VARCHAR(20), -- 信用评级
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
); -- 客户信息表

CREATE TABLE contact (
  contact_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  position VARCHAR(50), -- 职位
  phone VARCHAR(20),
  email VARCHAR(100),
  is_primary BOOLEAN DEFAULT FALSE, -- 主要联系人
  notes TEXT,
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
); -- 联系人信息表

CREATE TABLE opportunity (
  opportunity_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  title VARCHAR(200) NOT NULL,
  stage ENUM('prospecting', 'qualification', 'proposal', 'negotiation', 'closed'),
  expected_value DECIMAL(12,2),
  probability TINYINT CHECK (probability BETWEEN 0 AND 100),
  close_date DATE,
  owner_id INT NOT NULL, -- 负责人
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
); -- 销售机会表

CREATE TABLE interaction (
  interaction_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  contact_id INT,
  type ENUM('call', 'email', 'meeting', 'demo'),
  content TEXT NOT NULL,
  outcome TEXT,
  next_step VARCHAR(200),
  created_by INT NOT NULL, -- 记录人
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
  FOREIGN KEY (contact_id) REFERENCES contact(contact_id)
); -- 客户互动记录表

CREATE TABLE service_case (
  case_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  contact_id INT,
  priority ENUM('low', 'medium', 'high', 'urgent'),
  status ENUM('open', 'in_progress', 'resolved', 'closed'),
  description TEXT,
  resolution TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  closed_at TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
); -- 服务案例表

CREATE TABLE product_service (
  item_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  type ENUM('product', 'service'),
  unit_price DECIMAL(12,2),
  description TEXT,
  is_active BOOLEAN DEFAULT TRUE
); -- 产品/服务信息表

CREATE TABLE team_member (
  member_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT UNIQUE, -- 关联系统用户表
  department VARCHAR(50),
  role ENUM('sales', 'support', 'manager', 'executive'),
  phone_ext VARCHAR(10),
  joined_date DATE
); -- 团队成员信息表

CREATE TABLE customer_tag (
  tag_id INT PRIMARY KEY AUTO_INCREMENT,
  tag_name VARCHAR(50) UNIQUE,
  color_code VARCHAR(7) -- HEX颜色代码
);

-- 客户标签关联表
CREATE TABLE customer_tag_relation (
  customer_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (customer_id, tag_id),
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
  FOREIGN KEY (tag_id) REFERENCES customer_tag(tag_id)
); -- 客户与标签的关联关系表
```
* 关键索引建议
```sql
-- 客户表
CREATE INDEX idx_customer_name ON customer(customer_name);
CREATE INDEX idx_customer_source ON customer(source);

-- 销售机会
CREATE INDEX idx_opp_stage ON opportunity(stage);
CREATE INDEX idx_opp_close_date ON opportunity(close_date);

-- 互动记录
CREATE INDEX idx_interaction_date ON interaction(created_at);
CREATE INDEX idx_interaction_type ON interaction(type);
```

# 优化方案
* 数据分区：对互动记录表按时间进行分区
* 全文检索：为互动记录的content字段添加全文索引
* 敏感数据加密：对联系人的手机、邮箱进行加密存储
* 审计日志：单独建立变更日志表记录关键数据修改
* 数据归档：将已关闭3年以上的服务案例移至历史库

# 扩展功能建议
* 邮件集成表：记录与客户的邮件往来
* 任务管理表：跟踪客户跟进任务
* 合同管理表：存储客户合同信息
* 客户满意度表：记录调研反馈结果
* 报表分析表：预生成的统计报表数据
