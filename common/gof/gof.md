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
* **创建型**：**抽象了对象实例化**的过程，用来**帮助创建**对象的实例(**5种**)
  * 单例、工厂、抽象工厂、建造者、原型。
* **结构型**：描述如何**组合类和对象**，以获得更大的结构(**7种**)
  * 适配器、桥接、装饰、组合、外观、享元、代理。
* **行为**：描述算法和**对象间职责**的分配(**11种**)
  * 模板方法、命令、迭代器、观察者、中介、备忘录、解释器、状态、策略、职责、访问者。

# 创建型
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

## 工厂
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

### 简单工厂模式
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
作用：**复制对象**，分深浅拷贝
* 浅拷贝：复制对象，但**引用类型**的属性**引用同一个对象**
* 深拷贝：复制对象，**引用类型**的属性**引用不同对象**
  * 拷贝构造器
  * 重写clone方法
  * 序列化
```java
/**
 * 默认Cloneable实现的是浅拷贝
 * 若想实现深拷贝，则需要重写clone方法，或自定义深拷贝方法
 */
class Dongle implements Cloneable{
    ArrayList<String> list = new ArrayList<>(); 

    @Override
    public Dongle clone() throws CloneNotSupportedException {
        // return (Dongle) super.clone(); // 默认super.clone为浅克隆 list引用同一个对象
        Dongle clone = (Dongle) super.clone();
        clone.list = new ArrayList<>(this.list); // 创建新集合
        return clone;
    }
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

