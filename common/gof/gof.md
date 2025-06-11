设计模式(**Design Patterns**)由`Gang of Four`团队提出，故简称为**GOF**。

# OOP七原则
* SRP：单一职责原则，Single Responsibility Principle
* OCP：开闭原则，Open Closed Principle
* LSP：里氏替换原则，Liskov Substitution Principle
* ISP：接口隔离原则，Interface Segregation Principle
* DIP：依赖倒置原则，Dependency Inversion Principle
* LoD：迪米特法则，Law of Demeter
* CARP: 合成复用原则，Composite And Reuse Principle

# 分类
* **创建型**：抽象了对象实例化的过程，用来帮助创建对象的实例(**5种**)
  * 单例、工厂、抽象工厂、建造者、原型。
* **结构型**：描述如何组合类和对象，以获得更大的结构(**7种**)
  * 适配器、桥接、装饰、组合、外观、享元、代理。
* **行为**：描述算法和对象间职责的分配(**11种**)
  * 模板方法、命令、迭代器、观察者、中介、备忘录、解释器、状态、策略、职责、访问者。

# 创建型
| 设计模式          | 主要用途                              | 关键特点                                      | 适用场景                              |
|------------------|-------------------------------------|--------------------------------------------|-------------------------------------|
| **单例模式**      | 确保一个类只有一个实例                | 全局访问点，私有构造函数，延迟初始化           | 配置管理、日志系统、线程池等            |
| **工厂方法模式**  | 由子类决定创建的对象类型              | 定义接口，子类实现具体创建逻辑                 | 需要扩展产品类型时（如不同数据库连接）     |
| **抽象工厂模式**  | 创建相关或依赖的对象家族              | 提供一组接口，隐藏具体实现类                   | 跨平台UI组件、不同风格的产品族（如家具）  |
| **建造者模式**    | 分步构建复杂对象                     | 分离构造过程与表示，支持链式调用                | 构造多参数对象（如SQL查询、HTML生成器）   |
| **原型模式**      | 通过克隆现有对象创建新对象            | 实现 `Cloneable` 接口，避免重复初始化开销       | 创建成本高的对象（如游戏角色复制）        |
## 单例
* 隐藏构造函数，通过静态方法获取实例
* 实现：饿汉式和懒汉式
  * 懒汉式存在并发问题，需要加锁
```java
    // 单例模式
    static class SingleObject{
        private SingleObject(){}
        private static final SingleObject obj = new SingleObject(); // 饿汉式
        public static SingleObject getObj(){
            return obj;
        }
    
        private static SingleObject obj1; // 懒汉式
        public static SingleObject getObj1(){ // 为加锁，线程不安全
            if(obj1==null) obj1= new SingleObject();
            return obj1;
        }
        public static synchronized SingleObject getObj2(){ // 方法加锁
            if(obj1==null) obj1= new SingleObject();
            return obj1;
        }
        public static SingleObject getObj3(){
            if(obj1==null){
                synchronized(SingleObject.class){ // 代码块加锁
                    if(obj1==null) obj1= new SingleObject();
                    return obj1;
                }
            }
            return obj1;
        }
    }

    public static void main(String[] args) {
        SingleObject normalObject = getObj();
        SingleObject normalObject1 = getObj1();
        SingleObject normalObject2 = getObj2();
        SingleObject normalObject3 = getObj3();
    }
```

## 工厂方法
* 适用于提供**不同类型**产品
* **一个工厂生产一种产品,不同产品不同工厂**
  * 工厂负责具体的产品
```java
    static interface Product {}
    static class ProductA implements Product {}
    static class ProductB implements Product {}

    interface ProductFactory {
        Product getProduct();
    }
    class ProductAFactory implements ProductFactory { // 具体产品工厂生产具体产品
        @Override
        public Product getProduct() {return new ProductA();}
    }
    class ProductBFactory implements ProductFactory { // 具体产品工厂生产具体产品
        @Override
        public Product getProduct() {return new ProductB();}
    }

```

### 简单工厂
* **不属于GOF范式**，不符合**开放封闭原则**，会**修改工厂代码**
```java
    static class SampleProductFactory{
        public  ProductA getProductA(){return new ProductA();}
        public  ProductB getProductB(){return new ProductB();}
        // 每次增加或去除产品时，都要手动修改SampleProductFactory，不符合开闭原则
        
        public Product getProduct(String type){
            switch(type){ // 亦可使用if-else
                case "A": return new ProductA();
                case "B": return new ProductB(); 
                default: return null;
            }
        }
    }
```

## 抽象工厂
* 适用于提供**同系列不同类型**产品
* **系列工厂创建不同类型产品**
  * 系列工厂创建系列产品
* 与工厂模式区别：
  * 工厂模式工厂对应的单一产品
  * 抽象工厂模式工厂对应的是系列产品
```java
    static class TbProductA extends ProductA {}
    static class TbProductB extends ProductB {}
    static class JdProductA extends ProductA {}
    static class JdProductB extends ProductB {}

    static interface XProductFactory {
        ProductA getProductA();
        ProductB getProductB();
    }
    static class TbProductFactory implements XProductFactory {
        @Override
        public ProductA getProductA() {return new TbProductA();}
        @Override
        public ProductB getProductB() {return new TbProductB();}
    }
    static class JdProductFactory implements XProductFactory {
        @Override
        public ProductA getProductA() {return new JdProductA();}
        @Override
        public ProductB getProductB() {return new JdProductB();}
    }
```
## 原型
即克隆复制操作，关于深浅克隆不在这里多叙述，转至各语言的克隆实现
* 自定义提供克隆方法
* 或由语言内在提供，如java提供`Cloneable`接口，实现`clone()`方法(**默认浅克隆**)
```java
abstract class Prototype{
	// 关键点：clone自身的方法
	abstract Prototype clone(); // 提供自定义克隆方法
}
class OnePrototype extends Prototype{
	Prototype clone(){
		return (Prototype)this.clone();
	}
}
// class OnePrototype extends Prototype implements Cloneable // 或实现Cloneable接口，重写clone方法
```

## 建造者
* 适用于**复杂对象**，将对象的创建过程与对象本身分离，通过**链式调用**创建对象
  * 优点：创建过程与对象本身分离，可以复用
  * 缺点：代码冗余，需要创建Builder类
```java
class ProductA{
    // ProductA创建属性列表和方法列表
}
class ProductABuilder{
    // ProductA创建属性列表
    // 提供方法支持属性绑定
    // 结合属性生成ProductA
    public ProductA build(){
        return new ProductA();
    }
}
class Director{
    // 调用ProductABuilder的属性绑定方法
    // 调用ProductABuilder的生成方法
    public ProductA construct(ProductABuilder builder){
        return builder.build();
    }
}
```

# 结构型
| 设计模式              | 主要用途                              | 关键特点                                      | 适用场景                              |
|----------------------|-------------------------------------|--------------------------------------------|-------------------------------------|
| **适配器模式**       | 接口转换                            | 包装不兼容接口，使其协同工作                    | 整合旧系统、第三方库兼容                |
| **桥接模式**         | 分离抽象与实现                      | 用组合代替继承，独立变化维度                     | 跨平台应用、多维度扩展系统               |
| **组合模式**         | 统一处理树形结构                    | 以一致方式处理单个对象和组合对象                 | 文件系统、UI组件树、组织结构管理          |
| **装饰器模式**       | 动态添加职责                        | 透明扩展功能，避免子类爆炸                      | I/O流增强、权限校验链                  |
| **外观模式**         | 简化复杂系统接口                    | 提供统一高层接口，隐藏子系统细节                  | 框架封装、API网关设计                  |
| **享元模式**         | 共享细粒度对象                      | 缓存可复用对象，减少内存消耗                     | 文字编辑器字符对象、游戏粒子系统           |
| **代理模式**         | 控制对象访问                        | 添加间接层，实现延迟加载/访问控制                 | 远程调用、图片懒加载、AOP拦截            |
## 代理
* 代理模式核心：代理者实际控制被代理者
  * 持有被代理者(**必需**)
  * 继承相同类和实现相同接口(**显式**)
  * 有相同的接口方法(**隐式**)
* 实现方式：
  * 静态代理：简单，但需要代理者同步被代理者方法，并且提前编写代理类，代码冗余
    * **使用场景**：临时测试;严格一对一转发
  * 动态代理：复杂，但体现动态，不需要编写代理类，减少代码冗余
    * JDK动态代理：通过反射实现，只能代理接口
    * CGLIB动态代理：通过继承实现，可以代理类
```java
    // 代理模式（java静态代理）
    static abstract Product{
        public abstract void method();
        public abstract void method1();
    }
    // 以下方代码示例，即使不继承Product，也算代理模式(隐式)
    static class ProductOne extends Product{ // 被代理者
        public void method(){System.out.println("Method of ProductOne");}
        public void method1(){System.out.println("Method1 of ProductOne");}
    }
    static class ProductOneProxy extends Product{ // 代理者
        private ProductOne product; // 持有被代理者
        public ProductOneProxy(Product product){this.product = product;}
        public void method(){product.method();} 
        public void method1(){product.method1();}
        // ... 同步产品的所有方法，并内部分别调用对应产品方法
    }

    public static void main(String[] args) {
        ProductOneProxy proxy = new ProductOneProxy(new ProductOne());
        // 通过代理者调用被代理者方法
        proxy.method();
        proxy.method1();
    }
```

# 行为型
| 设计模式              | 主要用途                              | 关键特点                                      | 适用场景                              |
|----------------------|-------------------------------------|--------------------------------------------|-------------------------------------|
| **策略模式**         | 动态切换算法或行为                   | 定义算法族，封装每个算法，使它们可互换          | 支付方式选择、排序算法切换等            |
| **观察者模式**       | 一对多的依赖关系                     | 主题通知所有观察者，解耦发布者和订阅者           | 事件处理系统、消息通知机制              |
| **命令模式**         | 将请求封装为对象                     | 解耦请求发送者和接收者，支持撤销/重做            | 菜单操作、事务管理、任务队列             |
| **状态模式**         | 根据状态改变行为                     | 将状态转移逻辑分散到不同状态类中                | 订单状态流转、游戏角色状态切换           |
| **责任链模式**       | 链式处理请求                         | 多个处理器按顺序尝试处理请求                    | 审批流程、异常处理链、过滤器链           |
| **模板方法模式**     | 定义算法骨架                         | 父类定义步骤，子类实现具体细节                   | 框架设计、标准化流程（如饮料制作）        |
| **访问者模式**       | 分离数据结构与操作                   | 在不修改类的前提下添加新操作                    | 编译器AST处理、复杂对象结构操作          |
| **中介者模式**       | 集中控制对象交互                     | 通过中介者减少对象间直接耦合                    | 聊天室、GUI组件协调                   |
| **备忘录模式**       | 保存和恢复对象状态                   | 外部化对象状态，支持撤销功能                    | 游戏存档、事务回滚                     |
| **迭代器模式**       | 统一遍历集合元素                     | 提供一致的遍历接口，隐藏集合内部结构              | 集合类遍历（如List、Tree等）           |
| **解释器模式**       | 解释特定语法或表达式                 | 定义语法规则，构建语法树解释执行                 | 正则表达式、SQL解析、数学公式计算        |
## 策略
* 定义一个**抽象公共类**
* 定义具体实现类
* 与职责链区别：所有实现类都是同等级的，没有链式职责
* 由其它类绑定抽象公共类，在用户实例化其他类时，绑定具体策略类
```java
abstract class Strategy{
    // 公共内容
}
class FirstStrategy extends Strategy{}
class SecondStrategy extends Strategy{}
class Context {
    Strategy strategy; // 绑定公共类
    public Context(Strategy strategy){ //在用户实例化其他类时，绑定具体策略类
        this.strategy = strategy;
    }
}
```
## 职责链
* 定义一个**抽象公共类**
  * 定义下一个**公共类属性**
  * 定义下一个函数，用于**绑定**下一个公共类
  * 定义一个抽象方法，用于**处理职责**
* 定义多个公共类，并实现其职责逻辑
  * 在职责完成后判断是否还有下一个公共类，有继续执行，没有终止
```java
abstract class Handler{ // 定义一个抽象公共类
    protected Handler processor; // 定义下一个公共类属性
    public void setNextProcessor(Handler processor){ // 绑定下一个公共类
        this.processor = processor;
    }
    abstract void handler(); // 定义一个抽象方法，用于处理职责
}
class FirstHandler extends Handler{
    @Override
    public void handler(){
        /** 自身职责处理 */
        // 自身职责处理完，执行下一个职责
        if(processor !=null){processor.handler();}
    }
}
class SecondHander extends Handler{
    @Override
    public void handler(){
        if(processor !=null){processor.handler();}
    }
}
public static void main(String[] args) {
    FirstHandler first = new FirstHandler();
    SecondHander second = new SecondHander();
    first.setNextProcessor(second);
    first.handler(); // 执行自身职责,当First职责处理完会继续Second职责
}
```
## 观察者
* 被观察者绑定**观察者列表**
  * 并定义一个用于**通知**观察者更新的方法
* 观察者定义**更新方法**
* 被观察者更新时，遍历观察者列表，调用观察者更新方法
```java
abstract class Observer{abstract void update();}
class ObserverA extends Observer{public void update(){System.out.println("ObserverA update");}}
class ObserverB extends Observer{public void update(){System.out.println("ObserverB update");}}
class Subject{
    List<Observer> observers = new ArrayList<>(); // 绑定观察者列表
    public void addObserver(Observer observer){observers.add(observer);}
    public void removeObserver(Observer observer){observers.remove(observer);}
    public void update(){ // 被观察者更新时，遍历观察者列表，调用观察者更新方法
        for(Observer observer:observers){
            observer.update();
        }
    }
}

public static void main(String[] args) {
    Subject subject = new Subject();
    subject.addObserver(new ObserverA());
    subject.addObserver(new ObserverB());
    subject.update(); // 被观察者更新，调用所有观察者更新方法
}
```

# 实践
## 集合创建型模式
* `TbProductABuilder,TbProductBBuilder` **建造者**
* `TbProductAFactory,TbProductBFactory` **工厂方法**
* `TbProductFactory` **抽象工厂**
* `TbProductFactory` **单例**
* `ProductB` **原型**
```java
abstract class Product {}

abstract class ProductA extends Product {}
abstract class ProductB extends Product implements Cloneable{
    @Override
    public ProductB clone() throws CloneNotSupportedException { //支持克隆，即原型模式
        return (TbProductB)super.clone();
    }
}

class TbProductA extends ProductA {}
class TbProductB extends ProductB {}

class TbProductABuilder{ // 建造者模式
    // ProductA创建属性列表
    // 提供方法支持属性绑定
    // 结合属性生成ProductA
    public TbProductA build(){return new TbProductA();}
}
class TbProductBBuilder{
    // ProductB创建属性列表
    // 提供方法支持属性绑定
    // 结合属性生成ProductB
    public TbProductB build(){return new TbProductB();}
}

interface ProductFactory { // 工厂方法模式
    Product createProduct();
}

class TbProductAFactory implements ProductFactory {
    public Product createProduct() {
        TbProductABuilder builder = new TbProductABuilder();
        // 设置属性
        return builder.build();
    }
}

class TbProductBFactory implements ProductFactory {
    public Product createProduct() {
        TbProductBBuilder builder = new TbProductBBuilder();
        // 设置属性
        return builder.build();
    }
    public ProductB createProductB(ProductB product) {
        return product.clone(); // 使用原型模式创建对象
    }
}


interface XProductFactory { // 抽象工厂模式
    ProductA createProductA();

    ProductB createProductB();
    ProductB createProductB(ProductB product);
}

class TbProductFactory implements XProductFactory { 
    private TbProductAFactory factoryA = new TbProductAFactory();
    private TbProductBFactory factoryB = new TbProductBFactory();

    private TbProductFactory (){}
    private static TbProductFactory instance = new TbProductFactory();
    public static TbProductFactory getInstance(){ // 单例模式
        return instance;
    }

    public ProductA createProductA() {
        return (ProductA)factoryA.createProduct();
    }

    public ProductB createProductB() {
        return (ProductB)factoryB.createProduct();
    }
    public ProductB createProductB(ProductB product) {
        TbProductBFactory factory = new TbProductBFactory();
        return factoryB.createProductB(product);
    }
}
```
