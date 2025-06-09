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
## 单例
* 隐藏构造函数，通过静态方法获取实例
* 实现：饿汉式和懒汉式
  * 懒汉式存在并发问题，需要加锁
```java
    // 单例模式
    static class NormalObject{
        private NormalObject(){} // 隐藏构造函数

        private static final NormalObject nobj = new NormalObject(); // 饿汉式，不会存在并发问题
        public static NormalObject getNormalObject(){
            return nobj;
        }
        private static NormalObject nobj1;// 懒汉式存在并发问题
        public static NormalObject getNormalObject1(){
            if (nobj1 == null) {
                nobj1= new NormalObject(); // 多线程时容易
            }
            return nobj1;
        }

        private static NormalObject instance; // 并发安全的懒汉式
        public static synchronized NormalObject getInstanceByMethSych(){ // 方法同步锁实现并发安全
            if(instance == null) instance = new NormalObject();
            return instance;
        }
        
        public static NormalObject getInstanceByDubbleCheckAndSych(){ // 通过双重校验+同步锁实现并发安全
            if(instance == null){
                synchronized(this){
                    if(instance == null) instance =new NormalObject();
                }
            }
            return instance;
        }
    }

    public static void main(String[] args) {
        NormalObject normalObject = getNormalObject();
        NormalObject normalObject1 = getNormalObject1();
        NormalObject normalObject2 = normalObject.getInstanceByMethSych();
        NormalObject normalObject3 = normalObject.getInstanceByDubbleCheckAndSych();
    }
```

## 工厂
* 适用于提供**不同类型**产品
* **一个工厂生产一种产品,不同产品不同工厂**
  * 工厂负责具体的产品
```java
    static abstract class Product{ // 定义抽象产品
        void method();
    }
    static class ProductThree extends Product{
        public void method(){System.out.println("Method of ProductThree");}
    }
    static class ProductFour extends Product{
        public void method(){System.out.println("Method of ProductFour");}
    }
    static interface ProductFactory{ // 定义同类型产品接口
        Product getProduct(); // 定义产品都为同一类型
    }
    static class ProductThreeFactory implements ProductFactory{
        @Override
        public Product getProduct() {
            return new ProductThree();
        }
    }
    static class ProductFourFactory implements ProductFactory{
        @Override
        public Product getProduct() {
            return new ProductFour();
        }
    }
```

### 简单工厂模式
* 不属于GOF范式，因不符合**开放封闭原则**，会修改工厂代码
```java
    // 工厂模式：适用于不同系列产品
    // 简单工厂
    static class ProductOne{
        public void method(){System.out.println("Method of ProductOne");}
    }
    static class ProductTwo{
        public void method(){System.out.println("Method of ProductTwo");}
    }
    static class ProductFactory{
        // 有多少产品要对外提供多少方法，代码冗余
        public ProductOne productOne(){return new ProductOne();}
        public ProductTwo productTwo(){return new ProductTwo();}

        // 强化版，通过一个方法，传入参数，返回对应产品
        public Object product(String productType){ // 此方法的方法只能是产品同一上层，如父级及以上或接口
            if(productType.equals("one")) return new ProductOne();
            if(productType.equals("two")) return new ProductTwo();
            return null;
            // 此方法为强化版，虽然减少了方法数量，但增加了判断逻辑，代码仍有冗余
        }
    }

    public static void main(String[] args) {
        ProductFactory productFactory = new ProductFactory();
        productFactory.productOne().method();
        productFactory.productTwo().method();
        // 强化版使用
        ((ProductOne) productFactory.product("one")).method();
        ((ProductTwo) productFactory.product("two")).method();
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
    static interface ProductA{ // 定义A产品接口或抽象类
        void a();
    }
    static interface ProductB{ // 定义B产品接口或抽象类
        void b();
    }
    static class ProductOneA implements ProductA{ // ProductOne系列A产品
        public void a(){System.out.println("Method of ProductOneA");}
    }
    static class ProductOneB implements ProductB{ // ProductOne系列B产品
        public void b(){System.out.println("Method of ProductOneA");}
    }
    static class ProductTwoA implements ProductA{ // ProductTwo系列A产品
        public void a(){System.out.println("Method of ProductTwoA");}
    }
    static class ProductTwoB implements ProductB{ // ProductTwo系列B产品
        public void b(){System.out.println("Method of ProductTwoB");}
    }
    
    static interface ProductFactory{ // 定义同系列产品工厂接口
        ProductA productA();
        ProductB productB();
    }
    static class ProductOneFactory implements ProductFactory{ // ProductOne系列工厂
        @Override
        public ProductA productA() {
            return new ProductOneA();
        }
        @Override
        public ProductB productB() {
            return new ProductOneB();
        }
    }
    static class ProductTwoFactory implements ProductFactory{ // ProductTwo系列工厂
        @Override
        public ProductA productA() {
            return new ProductTwoA();
        }
        @Override
        public ProductB productB() {
            return new ProductTwoB();
        }
    }

    public static void main(String[] args) {
        ProductOneFactory oneFactory = new ProductOneFactory();
        oneFactory.productA().a();
        oneFactory.productB().b();

        ProductTwoFactory twoFactory = new ProductTwoFactory();
        twoFactory.productA().a();
        twoFactory.productB().b();
    }
```

# 结构型
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

