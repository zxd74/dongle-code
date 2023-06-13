package base.java.net;

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