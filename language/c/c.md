# 结构体函数实现
```c
typedef struct List{
    int* tab;
    int size;
    void (*put)(List* list,int val); // 首参绑定自身
    int (*get)(List* list,int idx);
    void (*del)(List* list,int idx);
}List;

static void put(List* list,int val){
    //...
}
static int get(List* list,int idx){
    //...
}
static void del(List* list,int idx){
    //...
}

void init(List* list){ // 定义初始化方法
    if(!list) exit(-1);
    list->size = 0;
    list->tab = NULL;
    list->put = put; // 绑定struct指针函数成员
    list->get = get;
    list->del = del;
}
```

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

# 简易TCP
* Server
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#define PORT 8080
#define BUF_SIZE 1024

// MinGW 必须链接 ws2_32
#pragma comment(lib, "ws2_32.lib")

int main() {
    WSADATA wsa;
    SOCKET server_fd, new_socket;
    struct sockaddr_in address;
    int addrlen = sizeof(address);
    char buffer[BUF_SIZE] = {0};
    const char *reply = "服务端已收到你的消息";

    // 1. 初始化 Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("Winsock 初始化失败，错误码：%d\n", WSAGetLastError());
        return 1;
    }

    // 2. 创建 socket
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("socket 创建失败：%d\n", WSAGetLastError());
        return 1;
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // 3. 绑定
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) == SOCKET_ERROR) {
        printf("绑定失败：%d\n", WSAGetLastError());
        return 1;
    }

    // 4. 监听
    if (listen(server_fd, 3) == SOCKET_ERROR) {
        printf("监听失败：%d\n", WSAGetLastError());
        return 1;
    }

    printf("服务端已启动，端口 %d，等待客户端连接...\n", PORT);

    // 5. 接受连接
    if ((new_socket = accept(server_fd, (struct sockaddr *)&address, &addrlen)) == INVALID_SOCKET) {
        printf("接受连接失败：%d\n", WSAGetLastError());
        return 1;
    }
    printf("客户端已连接\n");

    // 6. 接收数据
    recv(new_socket, buffer, BUF_SIZE, 0);
    printf("客户端消息：%s\n", buffer);

    // 7. 回复
    send(new_socket, reply, strlen(reply), 0);
    printf("已回复客户端\n");

    // 8. 关闭
    closesocket(new_socket);
    closesocket(server_fd);
    WSACleanup();

    return 0;
}
```
* Client
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#define PORT 8080
#define BUF_SIZE 1024

#pragma comment(lib, "ws2_32.lib")

int main() {
    WSADATA wsa;
    SOCKET sock;
    struct sockaddr_in serv_addr;
    char buffer[BUF_SIZE] = {0};
    const char *message = "你好，MinGW Socket 服务端！";

    // 1. 初始化 Winsock
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("Winsock 初始化失败：%d\n", WSAGetLastError());
        return 1;
    }

    // 2. 创建 socket
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("创建 socket 失败：%d\n", WSAGetLastError());
        return 1;
    }

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    
    if (serv_addr.sin_addr.s_addr == INADDR_NONE) {
        printf("IP 无效\n");
        return 1;
    }

    // 3. 连接服务端
    if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        printf("连接失败\n");
        return 1;
    }

    // 4. 发送消息
    send(sock, message, strlen(message), 0);
    printf("已发送：%s\n", message);

    // 5. 接收回复
    recv(sock, buffer, BUF_SIZE, 0);
    printf("服务端回复：%s\n", buffer);

    // 6. 关闭
    closesocket(sock);
    WSACleanup();

    return 0;
}
```