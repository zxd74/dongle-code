# 并发编程
## 多线程&并发
1. 方式：thread，runnable,callable
2. 线程状态：new,runnable,running,blocked,waiting,timed_waiting
3. 线程同步：synchronized,lock
4. 线程通信：wait,notify,notifyAll
5. 线程池：Executor,ExecutorService,ThreadPoolExecutor
6. 线程安全：synchronized,lock,volatile,final
7. 线程调度：schedule,scheduleAtFixedRate,scheduleWithFixedDelay
8. 线程中断：interrupt,interrupted
9. 线程上下文：ThreadLocal

### Thread
    无返回值，以对象形式存在的线程任务
```java
new Thread(){
    @Override
    public void run() {
        System.out.println("Thread one");
    }
}.start();
```
### Runnable
    无返回值的线程任务
```java
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable one");
            }
        }).start(); // new Thread(()-> "Runnable one").start()
```
### Callable
    带返回值的线程任务
```java
String result = new Callable<String>(){
    @Override
    public String call() throws Exception {
        return "callable one";
    }
}.call(); // ((Callable<String>)() -> "callable one").call()
System.out.println(result);
```
### Future
```java
// 创建线程池
ExecutorService executorService = Executors.newFixedThreadPool(1);
// 创建任务
Callable<String> c = new Callable<String>(){
    @Override
    public String call() throws Exception {
        return "callable one";
    }
};
// 绑定任务
Future<String> future = executorService.submit(c);
// 等待结果；阻塞式
String result = future.get();
System.out.println(result);
```

### FutureTask&Callable
```java
    FutureTask<String> task = new FutureTask<>(()->Integer.valueof("123"));
    new Thread(task).start(); // 必需或run()，开启任务，否则就单纯是个任务，不会执行
    if (task.isDone()){
        ctx.writeAndFlush(task.get());
    }
```

### CountDownLatch
    多线程同步计数器，适用于指定数量的任务，比如商品秒杀
```java
public static void main(String[] args) {
    final int count = 3;
    final CountDownLatch latch = new CountDownLatch(count);

    for (int i = 0; i < count; i++) {
        new Thread(() -> {
            // 线程执行任务
            System.out.println(Thread.currentThread().getName() + " 执行任务...");
            // 任务执行完毕，计数器减1
            latch.countDown();
        }).start();
    }

    // 等待所有任务执行完毕
    latch.await();
    System.out.println("所有任务执行完毕...");
}
```

## 线程池
### Executors创建线程池
1. 可缓存无界限：newCachedThreadPool
2. 指定大小，可控最高并发：newFixedThreadPool
3. 指定大小、核心线程数、定时周期：newScheduledThreadPool
4. 单线程化：newSingleThreadExecutor

### 自定义线程池
参考Executors.newFixedThreadPool
```java

// int corePoolSize  核心线程数
// int maximumPoolSize  最大线程数
// long keepAliveTime 活跃时间，超过这个时间不活跃会自动关闭线程
// TimeUnit unit    活跃时间单位，与keepAliveTime关联
// BlockingQueue<Runnable> workQueue 线程队列
// faThreadFactory threadFactory 线程工厂，指定线程属性
// RejectedExecutionHandler handler 线程数量到达边界时的处理程序


    public static void main(String[] args) {
        int corePoolSize = 10;
        int maximumPoolSize = 60;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.DAYS;
        BlockingQueue workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r,"dongle thread");
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        };
        RejectedExecutionHandler handler = (r, executor) -> {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        };

        ExecutorService dongle1 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        // 绑定factory
        ExecutorService dongle2 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
        // 绑定超出处理程序
        ExecutorService dongle3 = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,handler);
    }
```

# Net 网络编程
## IP 获取
默认网卡IP地址获取
```java
InetAddress.getLocalHost().getHostAddress();
```
多网卡IP地址获取
```java
    Enumeration allNetInterfaces=NetworkInterface.getNetworkInterfaces();
    InetAddress ip=null;
    while(allNetInterfaces.hasMoreElements()){
        NetworkInterface netInterface=(NetworkInterface) allNetInterfaces.nextElement();
        //System.out.println(netInterface.getName());
        Enumeration addresses=netInterface.getInetAddresses();
        while(addresses.hasMoreElements()){
            // 当前网卡IP信息
            InetAddress ip=(InetAddress) addresses.nextElement();
        }
    }
```
## TCP
```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Tcp{
    public static class MyTcp{
        private BufferedReader reader;
        private ServerSocket serverSocket;
        private Socket socket;

        void getServer(){
            try {
                // 若不指定监听端口，系统会自动分配
                serverSocket = new ServerSocket(9000);
                System.out.println("等待请求连接.......");
                // 循环等待客户端请求连接
                while (true){
                    // 接收请求连接
                    socket = serverSocket.accept();
                    // 接收请求信息
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 处理请求
                    getClientMessage();
                    // 若以上步骤不发生错误，会继续下一次等待，除非系统或整个程序异常才会停止
                    System.out.println("等待下个请求连接.......");
                }
            }catch (Exception ignore){}
        }

        void getClientMessage(){
            try {
                while (true){
                    // 套接字是连接状态时可获取客户端信息，即处理请求信息
                    String request = reader.readLine();
                    System.out.println("服务端接收：" + request);
                    String resp = "返回信息：" + Math.random();
                    // 返回响应信息
                    PrintWriter writer =  new PrintWriter(socket.getOutputStream(),true);
                    writer.println(resp);
                    System.out.println("服务端响应：" + resp);
                }
            }catch (Exception ignore){}
            // 需要关闭本次请求连接socket？多请求同时连接时，以下步骤可能会造成异常
            try {
                if (reader != null){
                    reader.close();
                }
                if (socket != null){
                    socket.close();
                }
            }catch (Exception ignore){}
        }

        public static void main(String[] args) {
            MyTcp tcp = new MyTcp();
            tcp.getServer();
        }
    }

    public static class Client{
        public static void main(String[] args) {
            PrintWriter writer;
            BufferedReader reader;
            try {
                // 绑定地址和端口
                Socket socket = new Socket("127.0.0.1",9000);
                // 发送请求信息
                String body = "请求连接:" + Math.random();
                writer = new PrintWriter(socket.getOutputStream(),true);
                writer.println(body);
                System.out.println("客户端发送：" + body);
                // 接收响应信息，如果有
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("客户端接收：" + reader.readLine());
            }catch (Exception ignore){}

        }
    }
}
```
## UDP
```java
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Udp {
    public static class MyUdp{
        InetAddress inetAddress;
        MulticastSocket socket;
        int port = 9000;
        MyUdp(){
            try {
                // 实例化InetAddress，指定地址
                inetAddress = InetAddress.getByName("224.255.10.0");
                // 创建DatagramSocket实例
                // MulticastSocket为多点广告套接字
                socket = new MulticastSocket(port);
                // 指定发送范围为本地网络
                socket.setTimeToLive(1);
                // 加入广播组
                socket.joinGroup(inetAddress);
            }catch (Exception ignore){}
        }
        void sendPacket(String info){
            while (true){
                String resp = info + Math.random();
                DatagramPacket packet = null;
                byte[] data = resp.getBytes(StandardCharsets.UTF_8);
                // 将数据打包
                packet = new DatagramPacket(data,data.length,inetAddress,port);
                System.out.println(resp);
                try {
                    socket.send(packet);
                    Thread.sleep(3000);
                }catch (Exception ignore){}
            }
        }

        public static void main(String[] args) {
            MyUdp udp = new MyUdp();
            udp.sendPacket("号外号外：河里有只乌龟，请收听！！！");
        }
    }

    public static class Client{
        public static void main(String[] args) {
            MulticastSocket socket;
            InetAddress inetAddress;
            int port = 9000;
            try {
                inetAddress = InetAddress.getByName("224.255.10.0");
                socket = new MulticastSocket(port);
                socket.joinGroup(inetAddress);
                System.out.println("正在接收的内容：");
                while (true){
                    // 定义byte数组
                    byte[] data = new byte[1024];
                    // 待接收的数据包
                    DatagramPacket packet = new DatagramPacket(data,data.length,inetAddress,port);
                    // 接收数据包
                    socket.receive(packet);
                    // 获取数据包内容
                    String message = new String(packet.getData(),0,packet.getLength());
                    System.out.println(message);
                }
            }catch (Exception ignore){}
        }
    }
}

```
## IO模型
### 阻塞IO(BIO)
* `ServerSocke`t 服务端socket，用于监听客户端连接
* `Socket` 客户端socket，用于与服务端通信
* `InputStream` 输入流，用于读取数据
* `OutputStream` 输出流，用于写入数据
```java
    public void server(){
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept(); // 等待客户端接收
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[32];
        int len;
        while(true){
            // 读取数据
            while((len=in.read(buf)) != -1){
                // 循环接收全部数据
            }
            
            buf = "hello".getBytes();
            len = buf.length;
            // 输出数据
            out.write(buf, 0, len);
        }
    }

    public void client(){
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(port));
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[32];
        int len;
        while(true){ // 输入输出与server类似
            buf = "hello".getBytes();
            // 写入数据
            out.write(send);

            buf = new byte[32];
            // 读取数据
            while((len=in.read(buf)) != -1){
                // 循环接收全部数据
            }
        }
    }
```
### 非阻塞IO(NIO)
* Buffer 数据缓冲区，用于发送或接收数据
* Selector 通道选择器，用于监听通道事件
  * `SelectorKey` 通道事件Enum
* Channel 通道，用于发送或接收数据，并且注册监听事件
* 客户端`SocketChannel`
  * 使用connect连接服务端
  * 注册`SelectionKey.OP_CONNECT`状态
* 服务端`ServerSocketChannel`
  * 使用`bind`绑定端口
  * 注册`SelectionKey.OP_ACCEPT`状态
* `channel.configureBlocking`: 设置是否阻塞，默认阻塞
* 占用**主线程持续等待**连接和事件
```java
    // 监听端口
    int port;
    /*标识数字*/ 
    private  int flag = 0; 
    private  int BLOCK = 4096; 
    private  ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);  
    private  ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);  

    public  void server() throws IOException {
        // 区别一：打开服务器套接字通道 
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();   
        serverSocketChannel.configureBlocking(false);  // 服务器配置为非阻塞 
        ServerSocket serverSocket = serverSocketChannel.socket();  
        // 区别二： 绑定端口
        serverSocket.bind(new InetSocketAddress(port));  
        Selector selector = Selector.open();  // 通过open()方法找到Selector 
        // 区别三： 注册到selector，等待连接 
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  

        listen(selector);
    }

    public  void client() throws IOException {
        // 区别一：打开socket通道 
        SocketChannel socketChannel = SocketChannel.open();  
        socketChannel.configureBlocking(false); // 设置为非阻塞方式 
        Selector selector = Selector.open();  
        // 区别二：注册连接服务端socket动作 
        socketChannel.register(selector, SelectionKey.OP_CONNECT);  
        // 区别三：连接server
        InetSocketAddress server = new InetSocketAddress("host", port); 
        socketChannel.connect(server);  
        
        listen(selector);
    }

    // 监听连接
    public listent(Selector selector) throws IOException {
        // 监听连接 
        while (true) {   // true无限循环 监听
            // 选择一组键，并且相应的通道已经打开 
            selector.select();  
            // 返回此选择器的已选择键集。 
            Set<SelectionKey> selectionKeys = selector.selectedKeys();  
            Iterator<SelectionKey> iterator = selectionKeys.iterator();  
            while (iterator.hasNext()) {          
                SelectionKey selectionKey = iterator.next();  
                iterator.remove();  
                handleKey(selectionKey);  
            }  
        } 
    }

    // 处理请求 
    private void handleKey(SelectionKey selectionKey) throws IOException {  
        // 接受请求 
        ServerSocketChannel server = null;  
        SocketChannel client = null;  
        String receiveText,sendText;  int count=0;  
        // 测试此键的通道是否已准备好接受新的套接字连接。 
        if (selectionKey.isAcceptable()) {  
            // ... 服务端状态，可连接
            server = (ServerSocketChannel) selectionKey.channel();  
            client = server.accept();  // 接收客户端请求
            
            client.register(selector, SelectionKey.OP_READ);  // 连接之后注册可读状态
        } else if (selectionKey.isConnectable()) {  
            // ... 客户端状态，已连接
            
            client.register(selector, SelectionKey.OP_READ);  // 连接之后注册可读状态
        } else if (selectionKey.isReadable()) {  
            // 返回为之创建此键的通道。 
            client = (SocketChannel) selectionKey.channel();  
            //将缓冲区清空以备下次读取 
            receivebuffer.clear();  
            //读取服发送来的数据到缓冲区中 
            count = client.read(receivebuffer);   
            if (count > 0) {  
                receiveText = new String( receivebuffer.array(),0,count);  
                // // 注册为可写状态
                client.register(selector, SelectionKey.OP_WRITE);  
            }  
        } else if (selectionKey.isWritable()) {  
            //将缓冲区清空以备下次写入 
            sendbuffer.clear();  // set position=0，mark=-1
            // 返回为之创建此键的通道。 
            client = (SocketChannel) selectionKey.channel();  
            sendText="message from server--" + flag++;  
            //向缓冲区中输入数据 
            sendbuffer.put(sendText.getBytes());  
            //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位 
            sendbuffer.flip();  // set position=0
            //输出到通道 
            client.write(sendbuffer);  
            // 注册为可读状态
            client.register(selector, SelectionKey.OP_READ);  
        }  
     }  
```

### 异步IO(AIO)
* `AsynchronousServerSocketChannel`: 异步服务端套接字通道
* Asynchr`onousSocketChannel: 异步客户端套接字通道
* `AsynchronousFileChannel`: 异步文件通道
* `CompletionHandler`: 异步操作的完成处理程序
* 异步IO是异步非阻塞的，它可以通过异步IO的方式实现网络编程中的异步非阻塞。
* 类似**NIO+额外线程**方式
```java
    int port;
    public void server(){
        //创建异步服务端通道
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        // 异步accept客户端异步处理，回调的时候会调用里面的方法
        server.accept(this,new AcceptHandler());
    }

    static class AcceptHandle implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>{
        //当有客户端请求进来，会自动回调这个方法
        @Override
        public void completed(AsynchronousSocketChannel socketChannel, AsynchronousServerSocketChannel serverSocketChannel) {
            ByteBuffer readBuffer,writeBuffer;
            // 读取操作(仅一次，多次需要重复read)
            socketChannel.read(readBuffer, channelInfo, new ReadHandle());
            // 写入操作：仅一次，多次需要重复write
            socketChannel.write(writeBuffer, channelInfo, new WriteHandle());
            // 接收下一个客户端请求
            serverSocketChannel.accept(serverSocketChannel, this);
        }

        @Override
        public void failed(Throwable throwable, AsynchronousServerSocketChannel serverSocketChannel) {
            throwable.printStackTrace();
        }
    }
    //read事件处理
    static class ReadHandle implements CompletionHandler<Integer, ChannelInfo>{}

    //write时间处理
    static class WriteHandle implements CompletionHandler<Integer, ChannelInfo>{}

    public void client(){
        //创建客户端通道
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        //异步连接，使用Future.get等待连接成功后返回
        //当然也可以使用方法回调，这里简单演示，就不用了
        socketChannel.connect(new InetSocketAddress("host", port)).get();
        //  分配缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(32);
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        while (true){
            //正常buffer写操作
            writeBuffer.put("hello".getBytes());
            writeBuffer.flip();
            //异步写，使用Future.get等待写成功
            socketChannel.write(writeBuffer).get();
            writeBuffer.clear();

            //异步读，使用Future.get等待读成功
            int len = socketChannel.read(readBuffer).get();
            byte[] buf = new byte[len];
            readBuffer.flip();
            readBuffer.get(buf);
            readBuffer.clear();
        }
    }
```

# JDBC 数据库编程
`Java DataBase Connectivity`,一种用于执行SQL语句的Java API，链接数据库和Java应用程序的纽带

## 步骤
1. 加载驱动Driver
2. 创建链接对象Connection
3. 发送SQL：Statement
```java
// 本实例采用mysql驱动，
Class.forName("com.mysql.jdbc.Driver");
// 数据源格式：  jdbc:odbc:数据源
Connection conn = DriverManager.getConnection("jdbc:mysql:数据源","user name","password");
Statement sql = conn.createStatement();

// 其他数据源仿照即可
```
## 组成
1. `DriverManager` 类：管理数据库中的所有驱动程序
2. `Connection` 接口：数据库连接接口
3. `Statement` 接口：用于在已建立连接的基础上向数据库发送SQL语句
    1. `Statement`：执行**不带参数**的SQL语句
    2. `PreparedStatement`：执行**带参数**的SQL语句
    3. `CallableStatement`：继承PreparedStatement，执行**存储过程**
4. `ResultSet` 接口：类似临时表，暂存sql执行结果(**查询**)

## 示例
* MYSQL驱动: `com.mysql.jdbc.Driver`
```java
package language.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 1. 加载驱动
 * 2. 创建连接
 * 3. 执行SQL
 * 4. 接收结果
 */
public class JdbcDemo {
    static Connection conn;
    public static Connection getConnection(){
        try {
            // 加载JDBC驱动
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test","test","123456");
        }catch (Exception ignore){}
        return conn;
    }
    
    public static ResultSet execute(){
        ResultSet resultSet = null;
        try {
            // 创建Statement，用于执行SQL语句
            Statement sql = conn.createStatement();
            // 接受执行结果
            resultSet = sql.executeQuery("select  * from users");
        }catch (Exception ignore){}
        return resultSet;
    }

    /**
     * 事务处理
     */
    public static void executeTransication(){
        try{
            conn.setAutoCommit(false); // 默认自动
            // 设置事务级别
            conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
            Statement sql = conn.createStatement();
            sql.executeUpdate("");
            conn.commit();
            conn.setAutoCommit(true);
        }catch(Exception ex){
            try{
                conn.rollback(); // 触发回滚
            }catch (Exception eex){
                eex.printStackTrace();
            }
        }
    }
}
```