package language.java.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NioDemo {

    /**
     * 直接使用SocketChannel访问
     */
    public void client(){
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost",9999));
            // 不能设置阻塞，否则read方法会一直等待读取内容
            channel.configureBlocking(false);
            System.out.println("请输入：");
            Scanner scanner = new Scanner(System.in);
            String message;
            while (scanner.hasNextLine()){
                message = scanner.next();
                System.out.println("发送消息：" + message);
                ByteBuffer bf = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
                channel.write(bf);

                // 接收响应
                StringBuilder sb = new StringBuilder();
                bf = ByteBuffer.allocate(1024);
                int len;
                // TODO 非阻塞模式第一次请求不会接收响应，第二次开始接收的是上一次请求响应
                // 解决方案见下发注释
                while ((len = channel.read(bf)) > 0 ){
                    bf.flip();
                    sb.append(new String(bf.array(),0,len,StandardCharsets.UTF_8));
                    bf.clear();
                }
                if (sb.length()>0){
                    System.out.println("接收消息：" + sb);
                }
            }

            //// 可将接收响应信息设置循环，直到可以得到响应
                //while (true){
                //    StringBuilder sb = new StringBuilder();
                //    bf = ByteBuffer.allocate(1024);
                //    int len;
                //    while ((len = channel.read(bf)) > 0 ){
                //        bf.flip();
                //        sb.append(new String(bf.array(),0,len,StandardCharsets.UTF_8));
                //        bf.clear();
                //    }
                //    if (sb.length()>0){
                //        System.out.println("接收消息：" + sb);
                //        break;
                //    }
                //}
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 直接使用ServerSocketChannel实现服务端
     */
    public void server(){
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(9999));
            // 设置阻塞true，accept会停顿直到有新链接及socketchannel才会继续
            // 设置非阻塞false，accept不能停顿，可能获取的socketchannel为空
            channel.configureBlocking(true);

            while (true){
                SocketChannel socketChannel = channel.accept();
                if (socketChannel == null){
                    continue;
                }
                // 实际请求通道不能设置阻塞，否则，read方法会一直阻塞读取数据
                socketChannel.configureBlocking(false);
                if (socketChannel.isConnected()){
                    System.out.println("已有客户端建立链接");
                }else{
                    System.out.println("客户端已断开链接");
                    continue;
                }

                System.out.println("准备接收数据");
                try {
                    while(true){
                        int len = 0;
                        StringBuilder sb = new StringBuilder();
                        ByteBuffer bf = ByteBuffer.allocate(1024);
                        len = socketChannel.read(bf);
                        while (len > 0 ){
                            bf.flip();
                            sb.append(new String(bf.array(),0,len,StandardCharsets.UTF_8));
                            bf.clear();
                            len = socketChannel.read(bf);
                        }
                        if(sb.length() > 0 ){
                            System.out.println("接收消息：" + sb);
                            // 返回客户端
                            String message = "已收到消息" + sb;
                            socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                            System.out.println("响应消息：" + message);
                        }
                    }
                }catch (Exception e){
                    socketChannel.close();
                    System.out.println("链接已强制关闭");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void nioClientUp(){
        try {
            SocketChannel channel = SocketChannel.open();
            // 不能设置阻塞，否则read方法会一直等待读取内容
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);
            channel.connect(new InetSocketAddress("localhost",9999));
            while (true){
                if (channel.isOpen()){
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        // 移除Key，避免重复操作处理
                        iterator.remove();
                        if (key.isConnectable()){
                            // 确保链接结束
                            while (!channel.finishConnect()){
                                System.out.println("链接中...");
                            }
                            System.out.println("客户端已连接服务器");
                            // 唤醒客户端请求的写入操作
                            channel.register(selector, SelectionKey.OP_WRITE);
                        }
                        if (key.isWritable()){
                            System.out.println("请输入：");
                            Scanner scanner = new Scanner(System.in);
                            String message;
                            if (scanner.hasNextLine()){
                                message = scanner.nextLine();
                                channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                                // 注册可读状态，服务端可以读取请求信息
                                channel.register(selector, SelectionKey.OP_READ);
                                System.out.println("发送消息：" + message);
                            }
                            //while (scanner.hasNextLine()){
                            //    message = scanner.nextLine();
                            //    channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                            //    // 注册可读状态，服务端可以读取请求信息
                            //    //channel.register(selector, SelectionKey.OP_READ);
                            //    System.out.println("发送消息：" + message);
                            //}
                        }
                        if (key.isReadable()){
                            StringBuilder sb = new StringBuilder();
                            ByteBuffer bf = ByteBuffer.allocate(1024);
                            int len;
                            try {
                                while ((len = channel.read(bf)) != 0 ){
                                    bf.flip();
                                    sb.append(new String(bf.array(),0,len,StandardCharsets.UTF_8));
                                    bf.clear();
                                }
                                channel.register(selector, SelectionKey.OP_WRITE);
                            }catch (Exception ex){
                                System.out.println("接收服务端消息异常");
                                key.cancel();
                                channel.close();
                            }finally {
                                // 可终结本次链接，也可继续本次链接通信，但需要继续注册OP_WRITE 或 OP_READ事件
                                //key.cancel();
                                //channel.close();
                            }
                            if (sb.length()>0){
                                System.out.println("接收消息：" + sb);
                            }
                        }
                    }
                }
            }
        }catch (Exception ex){
            System.out.println("客户端异常，请重启");
            ex.printStackTrace();
        }
    }

    /**
     * NIO Server优化版：利用Selector优化
     */
    public void nioServerUp(){
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(9999));
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            // 注册OP_ACCEPT事件（即监听该事件，如果有客户端发来连接请求，则该键在select()后被选中）
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务已启动，等待客户端链接");
            while (true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 移除Key，避免重复操作处理
                    iterator.remove();
                    if (key.isAcceptable()){
                        SocketChannel clientChannel = channel.accept();
                        clientChannel.configureBlocking(false);
                        // 服务端链接后要进入读取操作，读取客户端请求内容
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        String message = "这是第" + (selector.keys().size() -1) + "个请求链接";
                        System.out.println(message);
                    }
                    if (key.isWritable()){
                        SocketChannel clientChannel = (SocketChannel)key.channel();
                        String message = "已收到消息! ";
                        try {
                            clientChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                            System.out.println("响应消息：" + message);
                            // 写操作之后一定要表示下移动作为读取，否则会陷入循环写操作输出
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        }catch (Exception ex){
                            System.out.println("客户端异常关闭链接,无法发送响应信息");
                            clientChannel.close();
                        }
                    }
                    if (key.isReadable()){
                        SocketChannel clientChannel = (SocketChannel)key.channel();
                        StringBuilder sb = new StringBuilder();
                        ByteBuffer bf = ByteBuffer.allocate(1024);
                        int len;
                        try {
                            while ((len = clientChannel.read(bf)) != 0 ){
                                bf.flip();
                                sb.append(new String(bf.array(),0,len,StandardCharsets.UTF_8));
                                bf.clear();
                            }
                            // 读取之后可以更改未写入操作，方便下发广告响应内容
                            clientChannel.register(selector, SelectionKey.OP_WRITE);
                        }catch (Exception ex){
                            System.out.println("客户端异常关闭链接,无法读取请求信息");
                            clientChannel.close();
                        }
                        if(sb.length() > 0 ){
                            System.out.println("接收消息：" + sb);
                        }
                    }
                }
            }
        }catch (Exception ex){
            System.out.println("服务器异常，服务终止");
            ex.printStackTrace();
        }
    }
}
