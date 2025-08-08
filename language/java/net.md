# BIO
* 使用Socket(ServerSocket,Socket,DatagramSocket)进行通信
* 使用InputStream/OutputStream/DatagramPacket进行数据传输
## TCP
* 服务端
  * 创建ServerSocket对象，绑定端口
  * 调用accept()方法，阻塞等待客户端连接
  * 获取Socket对象，进行通信(数据传输)
  * 关闭Socket对象
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
  * 创建DatagramSocket对象，绑定端口
  * 创建DatagramPacket对象，绑定数据和地址
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
* 接收端:MulticastSocket
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
* 发送端:MulticastSocket
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

# NIO
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
