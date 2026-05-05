# 自定义命名空间
* 方式一：通过固定前缀方式`math_xxx`
* 方式二：`static` 做私有成员（**static成员不对外开放**）
* 方式三：通过宏定义方式，避免重复定义
  * 宏定义命名空间
    ```h
    // mynamespace.h
    #ifndef MY_NAMESPACE_H
    #define MY_NAMESPACE_H

    // 模拟命名空间：定义一个结构体，存放函数指针
    typedef struct MyNamespace {
        // 命名空间内的函数声明
        void (*hello)(void);
        int (*add)(int a, int b);
    } MyNamespace;
    // 对外暴露的命名空间实例（全局唯一，像命名空间本身）
    extern const struct MyNamespace myns;
    #endif // MY_NAMESPACE_H
    ```
  * 具体实现及绑定
    ```c
    // mynamespace.c
    #include <stdio.h>
    #include "mynamespace.h"
    static void hello_impl(void) {
        printf("Hello from myns namespace!\n");
    }

    static int add_impl(int a, int b) {
        return a + b;
    }

    // 创建命名空间实例(实例化，定义位于头文件) 
    const MyNamespace myns = {
        .hello = hello_impl,   // 绑定函数指针
        .add   = add_impl
    };
    ```
  * 主函数调用
    ```c
    #include <stdio.h>
    #include "my_namespace.h" // 自动引入 外部实例 myns

    int main() {
        myns.hello(); // 使用方式：命名空间.函数()
        int res = myns.add(10, 20);
        printf("10 + 20 = %d\n", res);
        return 0;
    }
    ```