package com.zhuritec.netty;

import com.zhuritec.netty.handler.MySimpleServerPkgHandler;
import com.zhuritec.netty.handler.MySimpleServerProtoHandler;
import com.zhuritec.protobuf.UserProtobuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.string.StringDecoder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@Order(1)
public class NettyServer implements CommandLineRunner {
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
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
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new
                ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //配置服务端channel的缓冲器大小
//              .option(ChannelOption.SO_RCVBUF,3)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) {

                        ChannelPipeline pipeline = ch.pipeline();

//                        pipeline.addLast(new MySimpleServerHandler());
//                        pipeline.addLast(new MyAdapterServerHandler());
                        //添加固定的分隔符来解决粘包拆包的问题
//                        pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$_$".getBytes(StandardCharsets.UTF_8))));
//                        pipeline.addLast(new FixedLengthFrameDecoder(11));
//                        pipeline.addLast(new StringDecoder());
//                        pipeline.addLast(new MySimpleServerPkgHandler());
                        //指定需要解码的protobuf对象类型
                        pipeline.addLast(new ProtobufDecoder(UserProtobuf.User.getDefaultInstance()));
                        pipeline.addLast(new MySimpleServerProtoHandler());
                    }
                });

        //2.启动
        ChannelFuture future = null;
        try {
            future = bootstrap.bind(8888).sync();
            if (future.isSuccess()) log.info("netty server start success");
            /**
             * closeFuture().sync()阻塞等待，服务端线程变成wait状态，直到服务端监听端口关闭
             */
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            destroy();
        }


    }

    /**
     * netty关闭
     * 注解 @PreDestroy  在spring容器销毁之前执行
     */
    @PreDestroy
    public void destroy() {
        try {
            if (bossGroup != null) bossGroup.shutdownGracefully().sync();
            if (workerGroup != null) workerGroup.shutdownGracefully().sync();
            if (channel != null) channel.closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 异步启动netty server 避免阻塞springboot启动
     *
     * @param args
     * @throws Exception
     */
    @Async
    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
