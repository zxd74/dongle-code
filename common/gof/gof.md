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
### 静态内部类单例
静态内部类单例**延迟加载**，**适用于初始化成本高**的情况

特性	|直接静态字段初始化	|静态内部类Holder模式
--	|----	|--
初始化时机	|类加载时立即初始化	|第一次调用`getInstance()`时初始化
内存占用	|可能更早占用内存	|真正按需加载
线程安全性	|由类加载机制保证	|由类加载机制保证
实现复杂度	|更简单直接	|需要额外内部类
适用场景	|确定会使用的轻量级单例	|不确定是否使用或初始化成本高的单例

```java
public class Singleton {
    private Singleton() {}
    
    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
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
* 或由语言内在提供，如java提供`Cloneable`接口，实现` Object.clone()`方法(**默认浅克隆**)
```java
abstract class Prototype{
	// 关键点：clone自身的方法
	public abstract Prototype clone(); // 提供自定义克隆方法
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
## 适配器
* 适配者理论与适配目标是**等同**的，但相同操作方法不同，需要适配器对是适配者进行转换
* 和解释器有点像
  * 适配器将适配者包装成适配者，然后执行相同方法
  * 解释器都是仅仅是执行相同方法
```java
static abstract class Target{ // 需要适配的目标
    public abstract void request();
}
static class Adaptee{
    public void specificRequest(){
        // 适配类动作
    }
}
static class Target1 extends Target{
    @Override
    public void request() {
        // 适配目标动作
    }
}
static class Target2 extends Target{
    @Override
    public void request() {
        // 适配目标动作
    }
}
static class Adapter extends Target{
    private Adaptee adaptee = new Adaptee();
    @Override
    public void request() {
        adaptee.specificRequest(); // 通知适配类执行适配动作
    }
}

public static void main(String[] args){
    Target target = new Target1();
    target.request(); // 适配目标1

    target = new Target2(); // 适配目标2
    target.request();

    target = new Adapter(); // 适配目标3，内部相当代表适配者
    target.request();
}
```

# 行为型
| 设计模式              | 主要用途                              | 关键特点                                      | 适用场景                              |
|----------------------|-------------------------------------|--------------------------------------------|-------------------------------------|
| **策略模式**         | 动态切换算法或行为                   | 定义算法族，封装每个算法，使它们可互换          | 支付方式选择、排序算法切换等            |
| **观察者模式**       | 一对多的依赖关系                     | 主题通知所有观察者，解耦发布者和订阅者           | 事件处理系统、消息通知机制              |
| **命令模式**         | 将请求封装为对象                     | 解耦请求发送者和接收者，支持撤销/重做            | 菜单操作、事务管理、任务队列             |
| **状态模式**         | 根据状态改变行为                     | 将状态转移逻辑分散到不同状态类中                | 订单状态流转、游戏角色状态切换           |
| **职责链模式**       | 链式处理请求                         | 多个处理器按顺序尝试处理请求                    | 审批流程、异常处理链、过滤器链           |
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

## 命令
* 逻辑
  * 有命令调用方, 用于通知命令执行
  * 有具体执行逻辑，但具体执行人不确定
  * 有具体命令执行者，根据命令执行
* 改进：
  * 在调用方绑定多个命令组成命令集合，并进行排序，亦可以取消命令(将命令从命令集合中移除)
```java
class Receiver{} // 具体命令执行者
abstract class Command{ 
    private Receiver Receiver; // 命令绑定执行者
    public Command(Receiver Receiver){
        this.Receiver = Receiver;
    }
    public abstract void execute(); //执行具体命令
}
class CommandOne extends Receiver{ // 命令1的逻辑
    public CommandOne(Executor Receiver){
        super(Receiver);
    }
    public void execute(){
        System.out.println("Receiver execute CommandOne");
    }
}
class CommandTwo extends Command{ // 命令2的逻辑
    public CommandTwo(Receiver Receiver){
        super(Receiver);
    }
    public void execute(){
        System.out.println("Receiver execute CommandTwo");
    }
}
class Invoker{ // 命令调用者
    private Command command;
    // private List<Command> commands = new ArrayList<>(); // 命令集合
    public void setCommand(Command command){
        this.command = command;
    }
    public void action(){
        command.execute();
    }
}
public static void main(String[] args) {
    Receiver Receiver = new Receiver();
    Waiter waiter = new Waiter();
    waiter.setCommand(new CommandOne(executor));
    waiter.action();
    waiter.setCommand(new CommandTwo(executor)); //每设置一个新命令，上一个命令丢失
    waiter.action();
}
```

## 模板方法
* 由父类定义**一系列抽象方法**，由子类具体实现
* 由父类**决定**抽象方法**处理流程**(及调用抽象方法的流程)
* 适用于**流程一致，细节不一致**的场景
```java
static  abstract class Template{
    public abstract void templateOperationOne(); // 具体步骤实现由子类完成
    public abstract void templateOperationTwo();
    // ...more template abstract methods		

    public final void template(){  // 抽象类提供统一模板处理步骤处理流程
        templateOperationOne();
        templateOperationTwo();
        // invoker more template methods
    }
}

public static class TemplateA extends Template{
    @Override
    public void templateOperationOne(){
        System.out.println("TemplateA templateOperationOne");
    }
    @Override
    public void templateOperationTwo(){
        System.out.println("TemplateA templateOperationTwo");
    }
}
public static class TemplateB extends Template{
    @Override
    public void templateOperationOne(){
        System.out.println("TemplateB templateOperationOne");
    }
    @Override
    public void templateOperationTwo(){
        System.out.println("TemplateB templateOperationTwo");
    }
}
```

## 状态
* 定义**上下文**(任意有状态的都可以)，**关联状态对象**
  * 定义一个方法，用于执行状态处理
* 抽象状态，定义状态处理(绑定上下文)
  * 状态实现处理中切换下一个状态
* 与**职责链**区别：
  * 职责链是**顺序执行**，状态是**循环执行**
  * 职责链是**内部关联自身**，状态是**外部关联状态**
  * 职责链只需要自身，状态模式需要对象是有状态的
```java
static class Context{
    private State state;
    public void setState(State state){
        this.state = state;
    }

    public void request(){
        state.handle(this); // 处理请求，并内部设置下一个状态
    }
}

static abstract class State{
    public abstract void handle(Context context);
}
static class StateOne extends State{
    @Override
    public void handle(Context context){
        context.setState(new StateTwo ());
    }
}

static class StateTwo extends State{
    @Override
    public void handle(Context context){
        context.setState(new StateOne());
    }
}

public static void main(String[] args){
    Context context = new Context();
    context.setState(new StateOne());
    // 每请求一次更换一次状态
    context.request(); // 转换成StateTwo
    context.request(); // 转换成StateOne
    context.request(); // 转换成StateTwo
}
```

## 访问者
* 定义
  * **访问者 Visitor**，定义访问行为
  * **元素 Element**，定义接受访问者
  * **具体访问者**，实现访问行为
  * **具体元素**，实现接受访问者
* **特点**
  * 元素类和访问者类分离
  * 元素类绑定访问类，由访问类调用元素类的方法
  * 元素类与访问类是**多对多**关系
* **适用场景**
  * **数据结构稳定**但操作频繁变化的场景
  * 需要对复杂结构统一执行多种操作的场景
  * 避免污染原有类结构的场景
  * 操作需要访问对象的私有状态的场景
  * 双分派（Double Dispatch）需求的场景
```java
interface Visitor{
    void visit(ConcreteElementA element);
    void visit(ConcreteElementB element);
}
interface Element{
    void accept(Visitor visitor);
}
class ConcreteVisitorA implements Visitor{
    @Override
    public void visit(ConcreteElementA element){
        element.operationA();
    }
}
class ConcreteVisitorB implements Visitor{
    @Override
    public void visit(ConcreteElementB element){
        element.operationB();
    }
}
class ConcreteElementA implements Element{
    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
    public void operationA(){}
}
class ConcreteElementB implements Element{
    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
    public void operationB(){}
}

class ObjectStructure{  // 可以枚举元素类
    private List<Element> elements = new ArrayList();
    public void addElement(Element element){
        elements.add(element);
    }
    public void accept(Visitor visitor){
        for (Element element : elements){
            element.accept(visitor);
        }
    }
}

public static void main(String[] args){
    ObjectStructure objectStructure = new ObjectStructure();
    objectStructure.addElement(new ConcreteElementA());
    objectStructure.addElement(new ConcreteElementB());
    objectStructure.accept(new ConcreteVisitorA());
    objectStructure.accept(new ConcreteVisitorB());
}
```

## 解释器
* 不同解释器对同一内容进行解读(**强调行为**)
```java
static abstract class Interpreter{
    public abstract void interpretor(Context context);
}
static class ConcreteInterpreter extends Interpreter{
    @Override
    public void interpretor(Context context) {
        // TODO Auto-generated method stub
    }
}
static class Context{
    // 内容
}
static class Client{
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Interpreter interpreter = new ConcreteInterpreter();
        Context context = new Context();
        interpreter.interpretor(context);
    }
}
```
## 迭代器
* 对一种数据结构提供集合操作，而不暴露其内部表示
```java
static interface Iterator{
    public Object first();
    // 其它方法：last,next等等
}
static abstract class Aggregate{
    public abstract Iterator createIterator();
}
static class IteratorOne implements Iterator{
    private AggregateOne  one;
    public IteratorOne (AggregateOne  one){this.one = one;}
    public Object first(){
        return one.get(0);
    }
}
static class AggregateOne extends Aggregate{
    private List<Object> list= new ArrayList<>();
    public Iterator createIterator(){
        return new IteratorOne(this);
    }
    public Object get(int index){
        return list.get(index);
    }
}
```
## 备忘录
* **将对象内部属性存到另外一个对象中**，恢复时，直接从另外对象中将属性赋值给对象
* 需要一个管理备忘录的对象(**没有也算是备忘录模式**)
```java
static class Memento{
    private String state;
    public Memento(String state){this.state=state;}
}
static class Originator{
    private String state;
    public void setState(String state){this.state = state;}
    public String getState(){return state;}
    public Memento createMemento(){
        return new Memento(state);
    }
    public void setMemento(Memento memento){
        state = memento.state;
    }
    public void show(){
        System.out.println(state);
    }
}
static class Caretaker{
    private Memento memento;
    public void setMemento(Memento memento){
        this.memento = memento;
    }
    public Memento getMemento(){
        return memento;
    }
}

public static void main(String[] args){
    Originator o = new Originator();
    o.setState("Dongle");
    o.show();
    Caretaker c = new Caretaker();
    Memento m = new Memento(o.getState());
    c.setMemento(m);

    o.setState("Kevin"); // 改变了Originator 的state
    o.show();

    o.setMemento(c.getMemento()); // 恢复Originator  的state 为"Dongle"
    o.show();
}
```

## 中介者
* 在中介对象中根据传入参数对象不同，将消息转发给不同的对象
```java
static abstract class Mediator{
    public abstract void send(String msg,Colleague c);
}
static class MediatorOne extends Mediator{
    private ColleagueOne c1;
    private ColleagueTwo c2;

    public void send(String msg,Colleague c){
        if(c == c1) c2.recv(msg);
        if(c==c2) c1.recv(msg);
    }
}
static abstract class Colleague{
    protected Mediator m;
    public Colleague(Mediator m ){this.m =m ;}
}
static class ColleagueOne extends Colleague{
    public ColleagueOne(Mediator m ){super(m);}
    public void send(String msg){
        m.send(msg,this);
    }
    public void recv(String msg){
        System.out.println("ColleagueOne recive msg:" + msg);
    }
}
static class ColleagueTwo extends Colleague{
    public ColleagueTwo (Mediator m ){super(m);}
    public void send(String msg){
        m.send(msg,this);
    }
    public void recv(String msg){
        System.out.println("ColleagueTwo recive msg:" + msg);
    }
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
