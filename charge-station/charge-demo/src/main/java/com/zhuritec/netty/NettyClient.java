package com.zhuritec.netty;

import com.zhuritec.netty.handler.MySimpleClientHandler;
import com.zhuritec.netty.handler.MySimpleClientPkgHandler;
import com.zhuritec.netty.handler.MySimpleClientProtoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Order(2)
public class NettyClient implements CommandLineRunner {

    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;

    /**
     * netty核心组件
     * 1.nioEventLoop  网络指挥官
     * 2.channel       快递小哥
     * 3.channel pipeline  工序流水线
     * 4.channel handler   流水线上的工人
     * 5.bytebuf   数据容器
     */

    public void start() {

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

    /**
     * 封装了客户端连接的方法
     *
     * @param bootstrap
     */
    public  void doConnect(Bootstrap bootstrap) {
        //2.启动
        ChannelFuture future = null;
        try {
            future = bootstrap.connect("localhost", 8888).sync();
            //注册监听器
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


    /**
     * netty关闭
     * 注解@PreDestroy 在容器销毁前执行
     */
    @PreDestroy
    public void destroy() {
        try {
            if (eventLoopGroup != null) eventLoopGroup.shutdownGracefully().sync();
            if (channel != null) channel.closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void run(String... args) throws Exception {
        start();
    }
}
