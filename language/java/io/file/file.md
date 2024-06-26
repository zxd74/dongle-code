# File工具代码库

## XML代码库

### Java版

#### 依赖

dom4j实现：org.dom4j:dom4j:2.1.3

## Excel

### 依赖

poi实现(xls)：org.apache.poi:poi:5.0.0

poi实现(xlsx)：org.apache.poi:poi-ooxml:5.0.0

easy-poi实现：cn.afterturn:easypoi-base(easypoi-web[可无]、easypoi-annotation\):4.4.0

easyexcel实现: com.alibaba:easyexcel:3.0.5

#### 说明

1. csv文件可使用java自带的Reader类处理(java7+)
2. poi对于每行最后一列或者几列若数据为空值就会导致读取列与实际列不一致问题，请谨慎！
2. easy-poi可通过注解列明直接转对象数据
3. easyexcel亦可绑定列

