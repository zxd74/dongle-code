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

# 注意事项
1. 若通信数据非字节，则需要进行数据加解密操作，即将非字节数据转为字节数据
2. ChannelOutboundHandler要在ChannelInboundHandlerAdapter之前配置，否则无效
3. Encoder加密属于ChannelOutboundHandler操作
4. Decoder解密属于ChannelInboundHandlerAdapter