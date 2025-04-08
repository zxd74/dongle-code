# 数据库设计
## 基础模块设计
```sql
CREATE TABLE `organizations` (
  `org_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `parent_id` INT UNSIGNED DEFAULT NULL COMMENT '父组织ID',
  `org_code` VARCHAR(50) NOT NULL COMMENT '组织编码',
  `org_name` VARCHAR(100) NOT NULL COMMENT '组织名称',
  `org_type` ENUM('company','department','team') NOT NULL COMMENT '组织类型',
  `org_level` TINYINT UNSIGNED DEFAULT 1 COMMENT '组织层级',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态(1启用0禁用)',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`org_id`),
  UNIQUE KEY `idx_org_code` (`org_code`),
  KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

CREATE TABLE `employees` (
  `emp_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `emp_code` VARCHAR(50) NOT NULL COMMENT '员工编号',
  `org_id` INT UNSIGNED NOT NULL COMMENT '所属组织',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` ENUM('M','F','O') DEFAULT NULL COMMENT '性别',
  `position` VARCHAR(100) DEFAULT NULL COMMENT '职位',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机',
  `hire_date` DATE DEFAULT NULL COMMENT '入职日期',
  `leave_date` DATE DEFAULT NULL COMMENT '离职日期',
  `status` ENUM('active','on_leave','terminated') DEFAULT 'active' COMMENT '状态',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`emp_id`),
  UNIQUE KEY `idx_emp_code` (`emp_code`),
  KEY `idx_org` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

CREATE TABLE `users` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `emp_id` INT UNSIGNED DEFAULT NULL COMMENT '关联员工ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机',
  `last_login` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `login_count` INT DEFAULT 0 COMMENT '登录次数',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态(1启用0禁用)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`),
  UNIQUE KEY `idx_phone` (`phone`),
  KEY `idx_emp` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
```
## 采购管理模块
```sql
CREATE TABLE `suppliers` (
  `supplier_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `supplier_code` VARCHAR(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` VARCHAR(100) NOT NULL COMMENT '供应商名称',
  `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
  `tax_id` VARCHAR(50) DEFAULT NULL COMMENT '税号',
  `bank_account` VARCHAR(50) DEFAULT NULL COMMENT '银行账号',
  `bank_name` VARCHAR(100) DEFAULT NULL COMMENT '开户行',
  `credit_rating` ENUM('A','B','C','D') DEFAULT 'B' COMMENT '信用等级',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态(1启用0禁用)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `idx_supplier_code` (`supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

CREATE TABLE `purchase_orders` (
  `po_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `po_code` VARCHAR(50) NOT NULL COMMENT '采购单号',
  `supplier_id` INT UNSIGNED NOT NULL COMMENT '供应商ID',
  `order_date` DATE NOT NULL COMMENT '订单日期',
  `expected_delivery_date` DATE DEFAULT NULL COMMENT '预计交货日期',
  `actual_delivery_date` DATE DEFAULT NULL COMMENT '实际交货日期',
  `payment_terms` VARCHAR(255) DEFAULT NULL COMMENT '付款条款',
  `shipping_terms` VARCHAR(255) DEFAULT NULL COMMENT '运输条款',
  `total_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '总金额',
  `tax_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '税额',
  `discount_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '折扣金额',
  `final_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '最终金额',
  `status` ENUM('draft','approved','shipped','received','completed','cancelled') DEFAULT 'draft' COMMENT '状态',
  `created_by` INT UNSIGNED NOT NULL COMMENT '创建人',
  `approved_by` INT UNSIGNED DEFAULT NULL COMMENT '审批人',
  `approved_at` DATETIME DEFAULT NULL COMMENT '审批时间',
  `remarks` TEXT DEFAULT NULL COMMENT '备注',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`po_id`),
  UNIQUE KEY `idx_po_code` (`po_code`),
  KEY `idx_supplier` (`supplier_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

CREATE TABLE `purchase_order_items` (
  `item_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `po_id` INT UNSIGNED NOT NULL COMMENT '采购订单ID',
  `product_id` INT UNSIGNED NOT NULL COMMENT '产品ID',
  `quantity` DECIMAL(12,3) NOT NULL COMMENT '数量',
  `unit_price` DECIMAL(15,2) NOT NULL COMMENT '单价',
  `tax_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '税率',
  `discount_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '折扣率',
  `line_total` DECIMAL(15,2) DEFAULT 0 COMMENT '行总计',
  `received_quantity` DECIMAL(12,3) DEFAULT 0 COMMENT '已接收数量',
  `expected_delivery_date` DATE DEFAULT NULL COMMENT '预计交货日期',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`item_id`),
  KEY `idx_po` (`po_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';
```
## 库存管理模块
```sql
CREATE TABLE `warehouses` (
  `warehouse_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `warehouse_code` VARCHAR(50) NOT NULL COMMENT '仓库编码',
  `warehouse_name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
  `location` VARCHAR(255) DEFAULT NULL COMMENT '位置',
  `manager_id` INT UNSIGNED DEFAULT NULL COMMENT '负责人',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `warehouse_type` ENUM('central','regional','temporary') DEFAULT 'regional' COMMENT '仓库类型',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态(1启用0禁用)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `idx_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

CREATE TABLE `inventory` (
  `inventory_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `warehouse_id` INT UNSIGNED NOT NULL COMMENT '仓库ID',
  `product_id` INT UNSIGNED NOT NULL COMMENT '产品ID',
  `quantity_on_hand` DECIMAL(12,3) NOT NULL DEFAULT 0 COMMENT '现有数量',
  `quantity_allocated` DECIMAL(12,3) DEFAULT 0 COMMENT '已分配数量',
  `quantity_on_order` DECIMAL(12,3) DEFAULT 0 COMMENT '在途数量',
  `safety_stock` DECIMAL(12,3) DEFAULT 0 COMMENT '安全库存',
  `reorder_point` DECIMAL(12,3) DEFAULT 0 COMMENT '再订货点',
  `last_stocktake_date` DATE DEFAULT NULL COMMENT '最后盘点日期',
  `last_receipt_date` DATE DEFAULT NULL COMMENT '最后收货日期',
  `last_issue_date` DATE DEFAULT NULL COMMENT '最后发货日期',
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `idx_warehouse_product` (`warehouse_id`,`product_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

CREATE TABLE `inventory_transactions` (
  `transaction_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `transaction_type` ENUM('purchase','sale','transfer','adjustment','return') NOT NULL COMMENT '交易类型',
  `warehouse_id` INT UNSIGNED NOT NULL COMMENT '仓库ID',
  `product_id` INT UNSIGNED NOT NULL COMMENT '产品ID',
  `quantity` DECIMAL(12,3) NOT NULL COMMENT '数量',
  `uom` VARCHAR(20) DEFAULT NULL COMMENT '单位',
  `reference_id` INT UNSIGNED DEFAULT NULL COMMENT '关联单据ID',
  `reference_code` VARCHAR(50) DEFAULT NULL COMMENT '关联单据号',
  `transaction_date` DATETIME NOT NULL COMMENT '交易时间',
  `operator_id` INT UNSIGNED NOT NULL COMMENT '操作人',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`),
  KEY `idx_warehouse_product` (`warehouse_id`,`product_id`),
  KEY `idx_reference` (`reference_id`,`reference_code`),
  KEY `idx_date` (`transaction_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存交易表';
```
## 销售管理模块
```sql
CREATE TABLE `customers` (
  `customer_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_code` VARCHAR(50) NOT NULL COMMENT '客户编码',
  `customer_name` VARCHAR(100) NOT NULL COMMENT '客户名称',
  `customer_type` ENUM('individual','company','government') DEFAULT 'company' COMMENT '客户类型',
  `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
  `tax_id` VARCHAR(50) DEFAULT NULL COMMENT '税号',
  `credit_limit` DECIMAL(15,2) DEFAULT 0 COMMENT '信用额度',
  `payment_terms` INT DEFAULT 30 COMMENT '付款期限(天)',
  `sales_rep_id` INT UNSIGNED DEFAULT NULL COMMENT '销售代表',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态(1启用0禁用)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `idx_customer_code` (`customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

CREATE TABLE `sales_orders` (
  `so_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `so_code` VARCHAR(50) NOT NULL COMMENT '销售单号',
  `customer_id` INT UNSIGNED NOT NULL COMMENT '客户ID',
  `order_date` DATE NOT NULL COMMENT '订单日期',
  `expected_ship_date` DATE DEFAULT NULL COMMENT '预计发货日期',
  `actual_ship_date` DATE DEFAULT NULL COMMENT '实际发货日期',
  `payment_terms` VARCHAR(255) DEFAULT NULL COMMENT '付款条款',
  `shipping_method` VARCHAR(100) DEFAULT NULL COMMENT '运输方式',
  `warehouse_id` INT UNSIGNED DEFAULT NULL COMMENT '发货仓库',
  `total_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '总金额',
  `tax_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '税额',
  `discount_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '折扣金额',
  `final_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '最终金额',
  `status` ENUM('draft','confirmed','processing','shipped','completed','cancelled') DEFAULT 'draft' COMMENT '状态',
  `created_by` INT UNSIGNED NOT NULL COMMENT '创建人',
  `approved_by` INT UNSIGNED DEFAULT NULL COMMENT '审批人',
  `approved_at` DATETIME DEFAULT NULL COMMENT '审批时间',
  `remarks` TEXT DEFAULT NULL COMMENT '备注',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`so_id`),
  UNIQUE KEY `idx_so_code` (`so_code`),
  KEY `idx_customer` (`customer_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

CREATE TABLE `sales_order_items` (
  `item_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `so_id` INT UNSIGNED NOT NULL COMMENT '销售订单ID',
  `product_id` INT UNSIGNED NOT NULL COMMENT '产品ID',
  `quantity` DECIMAL(12,3) NOT NULL COMMENT '数量',
  `unit_price` DECIMAL(15,2) NOT NULL COMMENT '单价',
  `tax_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '税率',
  `discount_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '折扣率',
  `line_total` DECIMAL(15,2) DEFAULT 0 COMMENT '行总计',
  `shipped_quantity` DECIMAL(12,3) DEFAULT 0 COMMENT '已发货数量',
  `warehouse_id` INT UNSIGNED DEFAULT NULL COMMENT '发货仓库',
  `expected_ship_date` DATE DEFAULT NULL COMMENT '预计发货日期',
  `remarks` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`item_id`),
  KEY `idx_so` (`so_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单明细表';
```
## 财务管理模块
```sql
CREATE TABLE `accounting_accounts` (
  `account_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_code` VARCHAR(50) NOT NULL COMMENT '科目编码',
  `account_name` VARCHAR(100) NOT NULL COMMENT '科目名称',
  `account_type` ENUM('asset','liability','equity','revenue','expense') NOT NULL COMMENT '科目类型',
  `parent_id` INT UNSIGNED DEFAULT NULL COMMENT '父科目ID',
  `level` TINYINT UNSIGNED DEFAULT 1 COMMENT '科目层级',
  `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
  `normal_balance` ENUM('debit','credit') NOT NULL COMMENT '正常余额方向',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `idx_account_code` (`account_code`),
  KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会计科目表';

CREATE TABLE `general_ledger` (
  `entry_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `entry_date` DATE NOT NULL COMMENT '记账日期',
  `account_id` INT UNSIGNED NOT NULL COMMENT '科目ID',
  `debit_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '借方金额',
  `credit_amount` DECIMAL(15,2) DEFAULT 0 COMMENT '贷方金额',
  `currency` CHAR(3) DEFAULT 'CNY' COMMENT '币种',
  `exchange_rate` DECIMAL(10,4) DEFAULT 1 COMMENT '汇率',
  `reference_type` VARCHAR(50) DEFAULT NULL COMMENT '关联单据类型',
  `reference_id` INT UNSIGNED DEFAULT NULL COMMENT '关联单据ID',
  `reference_code` VARCHAR(50) DEFAULT NULL COMMENT '关联单据号',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `period_id` INT UNSIGNED DEFAULT NULL COMMENT '会计期间',
  `posted_by` INT UNSIGNED NOT NULL COMMENT '记账人',
  `posted_at` DATETIME NOT NULL COMMENT '记账时间',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`entry_id`),
  KEY `idx_account_date` (`account_id`,`entry_date`),
  KEY `idx_reference` (`reference_type`,`reference_id`),
  KEY `idx_period` (`period_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='总账表';

CREATE TABLE `receivables_payables` (
  `rp_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rp_type` ENUM('receivable','payable') NOT NULL COMMENT '类型',
  `business_partner_id` INT UNSIGNED NOT NULL COMMENT '业务伙伴ID',
  `business_partner_type` ENUM('customer','supplier') NOT NULL COMMENT '伙伴类型',
  `document_type` VARCHAR(50) NOT NULL COMMENT '单据类型',
  `document_id` INT UNSIGNED NOT NULL COMMENT '单据ID',
  `document_code` VARCHAR(50) NOT NULL COMMENT '单据编号',
  `document_date` DATE NOT NULL COMMENT '单据日期',
  `due_date` DATE NOT NULL COMMENT '到期日',
  `original_amount` DECIMAL(15,2) NOT NULL COMMENT '原始金额',
  `outstanding_amount` DECIMAL(15,2) NOT NULL COMMENT '未结金额',
  `currency` CHAR(3) DEFAULT 'CNY' COMMENT '币种',
  `status` ENUM('open','partial','closed','cancelled') DEFAULT 'open' COMMENT '状态',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rp_id`),
  KEY `idx_partner` (`business_partner_id`,`business_partner_type`),
  KEY `idx_document` (`document_type`,`document_id`),
  KEY `idx_due_date` (`due_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收应付表';
```
# 扩展建议
* 多语言支持：
  * 为所有名称/描述字段添加多语言版本表
  * 实现动态语言切换功能
* 多币种支持：
  * 在财务相关表中增加币种和汇率字段
  * 实现自动汇率转换功能
* 审计跟踪：
  * 添加系统操作日志表
  * 记录关键数据的变更历史
* API集成：
  * 设计API访问控制表
  * 实现OAuth2.0认证机制
* 数据分区策略：
  * 按时间范围对交易表进行分区
  * 提高大数据量查询性能
