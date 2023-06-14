package net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
public class NettyDemo {
    
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
}
