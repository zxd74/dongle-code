# 简介
注解从JDK1.5 开始支持。又称Java标注，一种注释机制，可通过反射获取注解内容，在编译器生成类文件时，标注可以被嵌入到字节码中。Java 虚拟机可以保留标注内容，在运行时可以获取到标注内容 。

# JAVA 注解列表
## JDK 1.8
1. @FunctionalInterface 方法接口注解
2. @Repeatable  标识某注解可以在同一个声明上使用多次

## JDK 1.7 
1. @SafeVarargs 忽略任何使用参数为泛型变量的方法或构造函数调用产生的警告

## JDK 1.5
1. @Documented  文档注解,允许将注解包含在用户文档中
2. @Retention   标识注解保存方式
3. @Target      作用目标
4. @Inherited   标记注解可继承，允许子注解继承，但不作用于接口实现
5. @Override    方法重写，继承或实现的子类需要该注解
6. @Deprecated  标记过时，目标可使用，但是会报警告
7. @SuppressWarnings    指示编译器忽略注解中声明的警告

# 元注解
## @Retention
1. SOURCE 源代码
2. CLASS  class文件
3. RUNTIME  运行时

## @Target
1. Type 类/接口/注解/枚举
2. FIELD 属性
3. METHOD 普通方法
4. PARAMETER 参数
5. CONSTRUCTOR 构造方法
6. LOCAL_VARIABLE 局部变量
7. ANNOTATION_TYPE 注解
8. PACKAGE 包
9. TYPE_PARAMETER 类型参数 ：表示该注解能写在类型参数的声明语句中，JDK1.8+
    ```java
        @Target(ElementType.TYPE_PARAMETER)
        @interface TyptParam {}
    
        public <@TyptParam E> void test( String a) {}
    ```
10. TYPE_USE 类型使用：表示注解可以再任何用到类型的地方使用，JDK1.8+
    ```java
    @Target(ElementType.TYPE_USE)
    @interface NotNull {}

    private @NotNull int a = 10;

    public static void main(@NotNull String[] args) {}
    ``` 


# 自定义注解
```java
    // 定义注解@interface，标明作用范围，作用期
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
    @interface Dongle{
        String value();
    }
```
```java
    @Dongle("kevin")
    static class Kevin{
        @Dongle("name")
        private String name;

        @Dongle("method")
        public void test(){

        }
    }

    public static void main(String[] args) {
        try {
            Class cls = Class.forName(Kevin.class.getName());

            // 读取类上的注解
            Dongle typeDongle = (Dongle) cls.getAnnotation(Dongle.class);
            System.out.println(typeDongle.value());

            // 读取属性上注解
            Field name = cls.getDeclaredField("name");
            Dongle fieldDongle = name.getAnnotation(Dongle.class);
            System.out.println(fieldDongle.value());

            // 读取方法上的注解
            Method test = cls.getMethod("test");
            Dongle methodDongle = test.getAnnotation(Dongle.class);
            System.out.println(methodDongle.value());

        }catch (Exception ignore){}
    }

```