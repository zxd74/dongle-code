# 步骤
1. Server端
   1. 定义Boss和Work事件分组EventLoopGroup
   2. 定义ServerBootstrap服务端启动类
   3. 启动类配置
      1. 加入事件分组
      2. 绑定通道类型NioServerSocketChannel
      3. 配置操作ChannelOption.SO_BACKLOG
      4. 配置子通道操作ChannelOption.SO_KEEPALIVE
      5. 配置通道处理childHandler(有且又有一次配置)：处理请求链接通道的处理
         1. 消息处理 ChannelOutboundHandler
         2. 消息通信 ChannelInboundHandler
   4. 开启通道功能：启动服务端启动类并监听链接
      1. serverBootstrap绑定地址
      2. sync同步监听链接
   5. 关闭通道功能(若无异常，会程序会停在上一步)
2. Client端
   1. 定义事件分组EventLoopGroup(可忽略)
   2. 定义Bootstrap通信启动类
   3. 启动类配置
      1.  加入事件分组
      2.  绑定通道类型NioSocketChannel
      3.  配置操作ChannelOption.SO_KEEPALIVE
      4.  配置通道处理childHandler(有且又有一次配置)：处理请求链接通道的处理
          1.  消息转码：数据
          2.  消息通信 ChannelInboundHandlerAdapter
   4. 开启通道功能：启动客户端端启动类并监听链接
      1. Bootstrap链接服务端
      2. sync同步监听链接
   5. 关闭通道功能(若无异常，会程序会停在上一步)


```java
    public static class NettyClient{
        public static void main(String[] args) {
            try {
                EventLoopGroup workerGrouop = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGrouop);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 加解密一定要在ChannelInboundHandlerAdapter处理之前，否则适配器处理不会对消息加解密处理
                                .addLast(new DecodeHandler())
                                .addLast(new EncodeHandler())
                                // 消息通信一定要在最后添加，前面添加的应该都是数据处理
                                .addLast(new ClientHandler());
                    }
                });
                ChannelFuture future = bootstrap.connect("localhost",9999).sync();
                future.channel().closeFuture();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static class NettyServer{
        public static void main(String[] args) {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try{
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup,workGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                // 绑定业务通道流处理，允许添加多个,并且可以指定执行顺序(相对业务),addLast,addFirst,addBefore,addAfter
                                ch.pipeline()
                                        // 添加最后处理
                                        //.addLast(new InHandler())
                                        .addLast(new EncodeHandler())
                                        .addLast(new DecodeHandler())
                                        .addLast(new ServerHandler());
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG,128)
                        .childOption(ChannelOption.SO_KEEPALIVE,true);
                ChannelFuture future = serverBootstrap.bind(9999).sync();
                future.channel().closeFuture();
            }catch(Exception ex){
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        }
    }

    public static class ClientHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead");
            String message = (String) msg;
            if (message != null){
                System.out.println("接收请求：" + message);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("exceptionCaught");
            //super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static class ServerHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //super.channelActive(ctx);
            //final ByteBuf bf = ctx.alloc().buffer(1024);
            System.out.println("已收到链接请求！正在准备响应数据...");
            String content = "已收到链接请求，请接收响应！";
            ctx.write(content);
            ctx.flush();
            //bf.writeBytes("已收到链接请求！".getBytes(StandardCharsets.UTF_8));
            //final ChannelFuture f = ctx.write(content);
            //f.addListener(new ChannelFutureListener() {
            //    @Override
            //    public void operationComplete(ChannelFuture future) {
            //        assert f == future;
            //        ctx.close();
            //    }
            //});
            System.out.println("已发送响应信息:" + content);
            super.channelActive(ctx);
        }
    }

    /**
     * 数据加密，可指定类型
     */
    public static class EncodeHandler extends MessageToMessageEncoder<String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.out.println("消息进行编码");
            out.add(Unpooled.copiedBuffer(msg,StandardCharsets.UTF_8));
        }
    }

    /**
     * 数据解密，可指定类型
     */
    public static class DecodeHandler extends MessageToMessageDecoder<ByteBuf>{
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            out.add(msg.toString(StandardCharsets.UTF_8));
        }
    }
```

# 注意事项
1. 若通信数据非字节，则需要进行数据加解密操作，即将非字节数据转为字节数据
2. ChannelOutboundHandler要在ChannelInboundHandlerAdapter之前配置，否则无效
3. Encoder加密属于ChannelOutboundHandler操作
4. Decoder解密属于ChannelInboundHandlerAdapter

# 自定义协议
* 负责编码和解码自定义协议数据
* 主要涉及`MessageToByteEncoder`和`ByteToMessageDecoder`
  * 每个最好只有一个，但可以有多个，不过容易造成数据混乱(合包)
  * 如果并非合包的处理，解决办法:读取完数据后，将`ByteBuf.readIndex=.writerIndex`，或者使用`readBytes`读取读取为新数据
```java
class MyMessageHead{ // 消息头
    private int headData=0X76;//协议开始标志;
    private int length;//包的长度
    // .. 可以包含其它头消息体
}
class MyMessage{
    private MyMessageHead head;
    private byte[] body; // 内容体
}

class MyMessageEncode extends MessageToByteEncoder<MyMessage>{
    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessage msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getHead().getHeadData());// 写入开头的标志
        out.writeInt(msg.getBody().getLength()); // 写入包的的长度

        // 写入其它头消息体数据  out.writeBytes

        out.writeBytes(msg.getBody()); // 写入消息主体
    }
}

class MyMessageDecode extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 读取消息头
        int headData=in.readInt();
        int length=in.readInt();
        
        byte[] body=new byte[length];// 读取消息体
        in.readBytes(body);
        MyMessage msg=new MyMessage();// 封装成消息对象
        msg.setHead(new MyMessageHead());
        msg.setBody(body);
        out.add(msg);
    }
}
```