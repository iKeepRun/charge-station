package com.zhuritec.netty.util;

import com.zhuritec.netty.handler.MySimpleClientProtoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClientUtil {

    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;


    public void connect() {
        //1.创建
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new
                Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline
                                //固定分隔符，解决粘包拆包问题
//                        .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$_$".getBytes(StandardCharsets.UTF_8))))
                                //固定发送消息的大小，解决粘包拆包问题
//                        .addLast(new FixedLengthFrameDecoder(11))
//                        .addLast(new StringEncoder())
//                        .addLast(new MySimpleClientPkgHandler())


                                //解决protobuf类型的粘包和拆包问题内置处理器(通过在数据包的包头添加消息的长度)
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                //将bytebuf转换成protobuf
                                .addLast(new ProtobufEncoder())
                                .addLast(new MySimpleClientProtoHandler());

                    }
                });


        doConnect(bootstrap);

    }

    public  void doConnect(  Bootstrap bootstrap) {
        //2.启动
        ChannelFuture future = null;

        try {

            future = bootstrap.connect("localhost", 8888).sync();
            /**
             * 注册监听器，只能监听客户端启动的时候是否连接上服务端
             * 如果是运行过程中的断开，需要自实现消息处理器，重写断开连接的方法
             */

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("客户端连接成功");
                    }
                    else {
                        log.info("客户端连接失败,尝试重连.....");
//                        future.cause().printStackTrace();
                        //这里应该重开一个线程，一直尝试重新连接
                        eventLoopGroup.schedule(() -> doConnect(bootstrap), 5, TimeUnit.SECONDS);
                    }
                }
            });

            // 保持线程处于wait 监听阶段
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("客户端关闭");
            if (eventLoopGroup != null) eventLoopGroup.shutdownGracefully();
            if (channel != null) channel.closeFuture();
        }
    }
}
