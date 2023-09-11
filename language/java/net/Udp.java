package language.java.net;

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
