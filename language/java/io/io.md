
| 对比维度                | BIO（阻塞式IO）| NIO（非阻塞IO/多路复用IO）| AIO（异步IO）|
|-------------------------|---------------------------------------------|---------------------------------------------|---------------------------------------------|
| **核心模型**            | 流模型（InputStream/OutputStream）| 通道+缓冲区模型（Channel+Buffer）| 异步通道+回调/Future模型（AsynchronousChannel）|
| **阻塞特性**            | 1. 读写阻塞：线程等待数据就绪/写入完成<br>2. 一个连接对应一个线程 | 1. 通道非阻塞：读写无数据时返回，不阻塞<br>2. 多路复用：Selector单线程处理多通道 | 1. 完全异步：发起读写后立即返回，不阻塞<br>2. 完成后通过回调/Future通知结果 |
| **线程模型**            | 单连接单线程（线程池优化后为多线程）| 单线程处理多连接（Selector多路复用）| 无连接绑定线程，由系统回调触发处理线程 |
| **大文件处理核心**      | 分块+缓冲流（Buffered），控制内存占用       | 分块+直接缓冲区（allocateDirect），减少拷贝 | 异步分块回调，全流程无阻塞                  |
| **内存占用**            | 固定缓冲区大小（如32KB），无额外开销        | 直接缓冲区（堆外内存），减少JVM内存拷贝     | 同NIO，缓冲区大小可控                       |
| **编程复杂度**          | 低（线性逻辑，易调试）| 中（需处理Selector事件、缓冲区切换）| 中高（回调递归/异步同步问题）|
| **效率/性能**           | 低（阻塞等待，线程开销大）| 高（多路复用，CPU利用率高）| 极高（IO密集场景，无阻塞等待）|
| **适用场景**            | 1. 中小文件读写（<1GB）<br>2. 低并发网络通信（<1000 QPS）<br>3. 简单场景、开发效率优先 | 1. 大文件读写（1GB~10GB）<br>2. 中高并发网络通信（1000~10万 QPS）<br>3. 服务器端核心场景 | 1. 超大文件读写（>10GB）<br>2. 超高并发IO密集场景（>10万 QPS）<br>3. 异步下载/日志写入等 |
| **系统依赖**            | 无，JVM层面实现阻塞                         | 依赖操作系统多路复用（epoll/kqueue）| 依赖操作系统异步IO支持（Windows IOCP/Linux AIO）|
| **关键优化点**          | 1. 缓冲流减少磁盘IO<br>2. 线程池控制线程数   | 1. Selector优化（避免空轮询）<br>2. 直接缓冲区提升效率 | 1. 回调递归避免内存泄漏<br>2. CountDownLatch同步异步任务 |
| **典型应用**            | 简单文件拷贝、低并发Socket通信               | Nginx底层、Netty框架、大文件拷贝            | 大文件异步下载、高吞吐日志收集、分布式存储 |

# BIO 同步阻塞
基于`Buffer`实现数据读取
* 使用`Socket(ServerSocket,Socket,DatagramSocket)`进行通信
* 使用`InputStream/OutputStream/DatagramPacket`进行数据传输
## File
* 使用 `BufferedInputStream/BufferedOutputStream`（8KB 默认缓冲区，可自定义更大缓冲区）；
* 固定大小字节数组分块读写，控制内存占用；
* 关闭自动刷新，批量写入减少磁盘 IO；
* 适配 GB 级文件，内存占用仅为缓冲区大小。
```java
import java.io.*;

/**
 * BIO 大文件读写 Demo（分块读写，避免内存溢出）
 */
public class BioBigFileDemo {
    // 分块缓冲区大小（建议16KB-64KB，BIO中过大的缓冲区提升有限）
    private static final int BUFFER_SIZE = 32 * 1024; // 32KB
    // 测试大文件路径（请替换为本地真实大文件路径）
    private static final String SOURCE_FILE = "D:/big_source_file.dat";
    private static final String TARGET_FILE = "D:/big_target_file.dat";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 大文件拷贝核心方法
            copyBigFile(SOURCE_FILE, TARGET_FILE);
            
            long endTime = System.currentTimeMillis();
            File sourceFile = new File(SOURCE_FILE);
            System.out.println("BIO 大文件拷贝完成！");
            System.out.println("文件大小：" + formatFileSize(sourceFile.length()));
            System.out.println("耗时：" + (endTime - startTime) + " ms");
        } catch (IOException e) {
            System.err.println("BIO 大文件读写失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * BIO 分块拷贝大文件（核心方法）
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @throws IOException 读写异常
     */
    public static void copyBigFile(String sourcePath, String targetPath) throws IOException {
        // 声明流（try-with-resources 自动关闭，避免资源泄漏）
        try (InputStream in = new BufferedInputStream(new FileInputStream(sourcePath), BUFFER_SIZE);
             OutputStream out = new BufferedOutputStream(new FileOutputStream(targetPath), BUFFER_SIZE)) {

            // 分块读写的字节数组（内存占用仅BUFFER_SIZE）
            byte[] buffer = new byte[BUFFER_SIZE];
            int readLen; // 每次实际读取的字节数

            // 循环读取，直到文件末尾（read返回-1）
            while ((readLen = in.read(buffer)) != -1) {
                // 写入对应长度的字节（避免写入空字节）
                out.write(buffer, 0, readLen);
            }

            // 强制刷盘（确保缓冲区数据写入磁盘，大文件避免数据丢失）
            out.flush();
        }
    }

    /**
     * 格式化文件大小（字节→KB/MB/GB）
     * @param size 文件字节数
     * @return 格式化后的大小字符串
     */
    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        else if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        else if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        else return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
```
## TCP
* 服务端
  * 创建`ServerSocket`对象，绑定端口
  * 调用`accept()`方法，阻塞等待客户端连接
  * 获取Socket对象，进行通信(数据传输)
  * **关闭Socket对象**
```java
public class BIOServer extends Thread {
    private ServerSocket serverSocket;
    private int port;

    public BIOServer(int port) {this.port = port;}

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                // 阻塞等待客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // 处理客户端请求
                handleClient(clientSocket);
                // 多线程版
                // new Thread(() -> handleClient(clientSocket)).start();
            } 
        }catch (IOException e) {
            System.out.println("Error Socket: " + e.getMessage());
        }
    }
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // 读取数据
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                // 简单回显
                out.println("Echo: " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
}
```
* 客户端
  * 创建Socket连接
  * 进行通信c
```java
public class BIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.println("Hello Server");
        System.out.println("Server response: " + in.readLine());
        
        socket.close();
    }
}
```
## UDP
* 服务端
  * 创建`DatagramSocket`对象，绑定端口
  * 创建`DatagramPacket`对象，绑定数据和地址
  * 使用socket进行通信(数据传输)
```java
public class UDPServer extends Thread {
    private int port;
    public UDPServer(int port) {this.port = port;}

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket); // 阻塞等待数据包
                String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + sentence);

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
* 客户端
  * 创建DatagramSocket对象
  * 创建DatagramPacket对象，绑定数据和地址
  * 使用socket进行通信(数据传输)
```java
public class UDPClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        String sentence = "Hello Server";
        byte[] sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8080);
        clientSocket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("FROM SERVER:" + modifiedSentence);

        clientSocket.close();
    }
}
```
### 多播 (Multicast)
* 接收端:`MulticastSocket`
  * 加入多播组：`multicastSocket.joinGroup(group)`
```java
public class MulticastReceiver {
    public static void main(String[] args) {
        String multicastGroup = "230.0.0.0";
        int port = 8888;
        
        try (MulticastSocket multicastSocket = new MulticastSocket(port)) {
            InetAddress group = InetAddress.getByName(multicastGroup);
            multicastSocket.joinGroup(group);
            
            System.out.println("Joined multicast group " + multicastGroup);
            
            byte[] buffer = new byte[1024];
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                
                String received = new String(
                    packet.getData(), 
                    0, 
                    packet.getLength());
                System.out.println("Multicast message: " + received);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
* 发送端:`MulticastSocket`
  * 数据包绑定多播组
```java
public class MulticastSender {
    public static void main(String[] args) {
        String multicastGroup = "230.0.0.0";
        int port = 8888;
        
        try (MulticastSocket multicastSocket = new MulticastSocket();
             Scanner scanner = new Scanner(System.in)) {
            
            InetAddress group = InetAddress.getByName(multicastGroup);
            
            while (true) {
                System.out.print("Enter multicast message (or 'quit' to exit): ");
                String message = scanner.nextLine();
                
                if ("quit".equalsIgnoreCase(message)) {
                    break;
                }
                
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(
                    buffer, 
                    buffer.length, 
                    group, 
                    port);
                multicastSocket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
###  广播 (Broadcast) 
* **等同于多播**，只不过组地址是广播地址`255.255.255.255`
* 发送端
  * socket设置为广播模式`socket.setBroadcast(true)`
```java
public class BroadcastSender {
    public static void main(String[] args) {
        int port = 9999;
        
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {
            
            socket.setBroadcast(true); // 广播模式
            
            while (true) {
                System.out.print("Enter broadcast message (or 'quit' to exit): ");
                String message = scanner.nextLine();
                
                if ("quit".equalsIgnoreCase(message)) {
                    break;
                }
                
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(
                    buffer, 
                    buffer.length, 
                    InetAddress.getByName("255.255.255.255"), 
                    port);
                socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

# NIO 同步非阻塞
基于`Channel+Selector+Buffer`实现数据读取
## File
* 基于 `FileChannel` 分块读写，适配大文件
  * 固定大小缓冲区（4MB），避免一次性加载文件到内存；
  * 非阻塞模式（结合 Selector 可选，此处优先保证可读性）
```java
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO 大文件读写 Demo（适配GB级文件，分块读写）
 */
public class NioBigFileDemo {
    // 缓冲区大小（4MB，可根据机器性能调整：2MB/8MB）
    private static final int BUFFER_SIZE = 4 * 1024 * 1024;
    // 测试大文件路径（自行替换）
    private static final String SOURCE_FILE = "D:/big_source_file.dat";
    private static final String TARGET_FILE = "D:/big_target_file.dat";

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        
        // 1. 大文件读取 + 写入（分块处理）
        copyBigFile(SOURCE_FILE, TARGET_FILE);
        
        long endTime = System.currentTimeMillis();
        File sourceFile = new File(SOURCE_FILE);
        System.out.println("NIO 大文件拷贝完成！");
        System.out.println("文件大小：" + formatFileSize(sourceFile.length()));
        System.out.println("耗时：" + (endTime - startTime) + " ms");
    }

    /**
     * NIO 分块拷贝大文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void copyBigFile(String sourcePath, String targetPath) throws Exception {
        // 声明通道（try-with-resources 自动关闭）
        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileChannel readChannel = fis.getChannel();
             FileOutputStream fos = new FileOutputStream(targetPath);
             FileChannel writeChannel = fos.getChannel()) {

            // 分配直接缓冲区（堆外内存，减少JVM内存拷贝，提升大文件效率）
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            
            // 分块读取文件，直到读取完毕（read返回-1）
            while (readChannel.read(buffer) != -1) {
                buffer.flip(); // 切换为读模式（准备写入）
                // 确保缓冲区数据全部写入（避免部分写入）
                while (buffer.hasRemaining()) {
                    writeChannel.write(buffer);
                }
                buffer.clear(); // 重置缓冲区（准备下一次读取）
            }
            
            // 强制刷盘（确保数据写入磁盘，避免缓存丢失）
            writeChannel.force(true);
        }
    }

    /**
     * 格式化文件大小（字节→MB/GB）
     */
    private static String formatFileSize(long size) {
        if (size < 1024 * 1024) return size / 1024 + " KB";
        else if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        else return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
```
## TCP
* 服务端
```java
public class NIOServer extends Thread {
    private int port;

    public NIOServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(8080));

        serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 注册accept事件
        while(true){
            selector.select(); // 阻塞等待就绪的Channel
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for(SelectionKey key : selectedKeys){
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ); // 注册read事件
                }else if(key.isReadable()){ // 在Read事件中处理数据
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = client.read(buffer);
                    if(len > 0){
                        System.out.println("接收到客户端的数据：" + new String(buffer.array(), 0, len));
                    }else{
                        client.close();
                    }
                }
                // 可以再Read事件中Write写入数据，实现数据的双向通信
            }
        }
    }
}
```
* 客户端
```java
public class NIOClient extends Thread {
    @Override
    public void run() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            // 发送数据
            socketChannel.write(ByteBuffer.wrap("Hello Server".getBytes()));
            // 接收数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = socketChannel.read(buffer);
            if (len > 0) {
                System.out.println("接收到服务器的数据：" + new String(buffer.array(), 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
## UDP
* 服务端
```java
public class NIOUDPServer extends Thread {
    private int port;

    public NIOUDPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.configureBlocking(false); // 设置非阻塞
            datagramChannel.socket().bind(new InetSocketAddress(port));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                datagramChannel.receive(buffer); // 阻塞等待数据
                buffer.flip();
                System.out.println("接收到客户端的数据：" + new String(buffer.array(), 0, buffer.limit()));
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
* 客户端
```java
public class NIOUDPClient {
    public static void main(String[] args) throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String sentence = "Hello Server";
        buffer.put(sentence.getBytes());
        buffer.flip();
        datagramChannel.send(buffer, new InetSocketAddress("localhost", 8080)); // 发送数据
        datagramChannel.close();
    }
}
```
# AIO 异步非阻塞
基于`Asynchronous{XXX}Channel + CompletionHandler`实现数据读取
## File
* 基于 `AsynchronousFileChannel + CompletionHandler` 实现全异步
```java
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

/**
 * AIO 大文件读写 Demo（异步回调，分块处理）
 */
public class AioBigFileDemo {
    // 缓冲区大小（4MB）
    private static final int BUFFER_SIZE = 4 * 1024 * 1024;
    // 测试大文件路径（自行替换）
    private static final String SOURCE_FILE = "D:/big_source_file.dat";
    private static final String TARGET_FILE = "D:/big_target_file.dat";
    // 计数器（等待异步任务完成）
    private static CountDownLatch latch;
    // 文件总大小
    private static long fileSize;
    // 当前读写位置
    private static long currentPosition = 0;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        
        // 初始化计数器
        latch = new CountDownLatch(1);
        // 获取源文件大小
        fileSize = Paths.get(SOURCE_FILE).toFile().length();
        
        // 1. 打开异步通道
        AsynchronousFileChannel readChannel = AsynchronousFileChannel.open(
                Paths.get(SOURCE_FILE), StandardOpenOption.READ);
        AsynchronousFileChannel writeChannel = AsynchronousFileChannel.open(
                Paths.get(TARGET_FILE), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        
        // 2. 启动异步分块读取
        readNextBlock(readChannel, writeChannel);
        
        // 3. 等待所有异步任务完成
        latch.await();
        
        // 4. 关闭通道 + 强制刷盘
        writeChannel.force(true);
        readChannel.close();
        writeChannel.close();
        
        long endTime = System.currentTimeMillis();
        System.out.println("AIO 大文件拷贝完成！");
        System.out.println("文件大小：" + formatFileSize(fileSize));
        System.out.println("耗时：" + (endTime - startTime) + " ms");
    }

    /**
     * 异步读取下一块数据（递归回调，直到文件读取完毕）
     */
    private static void readNextBlock(AsynchronousFileChannel readChannel, AsynchronousFileChannel writeChannel) {
        // 分配直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        
        // 异步读取指定位置的数据
        readChannel.read(buffer, currentPosition, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer readLen, ByteBuffer buffer) {
                if (readLen == -1) {
                    // 读取完成，计数器减1
                    latch.countDown();
                    return;
                }
                
                try {
                    // 切换为读模式
                    buffer.flip();
                    // 异步写入目标文件
                    writeChannel.write(buffer, currentPosition, null, new CompletionHandler<Integer, Void>() {
                        @Override
                        public void completed(Integer writeLen, Void attachment) {
                            // 更新当前位置
                            currentPosition += readLen;
                            // 清理缓冲区
                            buffer.clear();
                            
                            // 判断是否读取完毕
                            if (currentPosition < fileSize) {
                                // 继续读取下一块
                                readNextBlock(readChannel, writeChannel);
                            } else {
                                // 读取完成
                                latch.countDown();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Void attachment) {
                            System.err.println("AIO 写入失败：" + exc.getMessage());
                            latch.countDown();
                        }
                    });
                } catch (Exception e) {
                    System.err.println("AIO 处理失败：" + e.getMessage());
                    latch.countDown();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                System.err.println("AIO 读取失败：" + exc.getMessage());
                latch.countDown();
            }
        });
    }

    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long size) {
        if (size < 1024 * 1024) {
            return size / 1024 + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
}
```
## Net
* 服务端
```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO 异步服务器（核心：AsynchronousServerSocketChannel + 回调）
 */
public class AioServer {
    public static void main(String[] args) throws IOException {
        // 1. 打开异步服务器通道
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(9999));
        System.out.println("AIO 服务器启动，监听端口9999...");

        // 2. 异步接受连接（核心：CompletionHandler回调）
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                // 接受新连接后，立即再次注册接受事件（处理下一个连接）
                serverChannel.accept(null, this);

                try {
                    System.out.println("客户端连接：" + clientChannel.getRemoteAddress());
                    // 准备缓冲区
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 异步读取客户端消息（回调处理）
                    clientChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer readLen, ByteBuffer buffer) {
                            if (readLen > 0) {
                                buffer.flip();
                                String msg = new String(buffer.array(), 0, readLen);
                                System.out.println("收到客户端消息：" + msg);
                                // 异步响应客户端
                                ByteBuffer responseBuffer = ByteBuffer.wrap(("AIO服务器响应：" + msg).getBytes());
                                clientChannel.write(responseBuffer, null, new CompletionHandler<Integer, Void>() {
                                    @Override
                                    public void completed(Integer writeLen, Void attachment) {
                                        System.out.println("服务器响应完成，写入字节数：" + writeLen);
                                    }

                                    @Override
                                    public void failed(Throwable exc, Void attachment) {
                                        System.out.println("服务器响应失败：" + exc.getMessage());
                                    }
                                });
                            }
                            try {
                                clientChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer buffer) {
                            System.out.println("读取客户端消息失败：" + exc.getMessage());
                            try {
                                clientChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("接受客户端连接失败：" + exc.getMessage());
            }
        });

        // 主线程保持存活（AIO是异步回调，主线程退出则服务器停止）
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
* Client
```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AIO 客户端
 */
public class AioClient {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // 1. 打开异步客户端通道
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();

        // 2. 异步连接服务器（Future方式）
        Future<Void> connectFuture = clientChannel.connect(new InetSocketAddress("localhost", 9999));
        connectFuture.get(); // 等待连接完成
        System.out.println("AIO 客户端连接服务器成功");

        // 3. 异步发送消息（回调方式）
        String msg = "Hello AIO Server!";
        ByteBuffer sendBuffer = ByteBuffer.wrap(msg.getBytes());
        clientChannel.write(sendBuffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer writeLen, Void attachment) {
                System.out.println("客户端发送消息完成，写入字节数：" + writeLen);
                // 发送完成后，异步读取响应
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                clientChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer readLen, ByteBuffer buffer) {
                        if (readLen > 0) {
                            buffer.flip();
                            String response = new String(buffer.array(), 0, readLen);
                            System.out.println("客户端收到响应：" + response);
                        }
                        try {
                            clientChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer buffer) {
                        System.out.println("客户端读取响应失败：" + exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("客户端发送消息失败：" + exc.getMessage());
            }
        });

        // 主线程等待回调执行完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```