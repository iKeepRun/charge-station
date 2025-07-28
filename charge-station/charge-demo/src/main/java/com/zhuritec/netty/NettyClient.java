package com.zhuritec.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(2)
public class NettyClient implements CommandLineRunner {

    private NioEventLoopGroup eventLoopGroup ;
    private Channel channel;
    /**
     * netty核心组件
     * 1.nioEventLoop  网络指挥官
     * 2.channel       快递小哥
     * 3.channel pipeline  工序流水线
     * 4.channel handler   流水线上的工人
     * 5.bytebuf   数据容器
     */

    public void start(){

        //1.创建
        eventLoopGroup=new NioEventLoopGroup();


        Bootstrap bootstrap=new
                Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast();


                    }
                });

        //2.启动
        ChannelFuture future = null;
        try {
            future = bootstrap.connect("netty",8888).sync();
            if(future.isSuccess()) log.info("client start success");
            // 保持线程处于wait 监听阶段
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if(eventLoopGroup!=null) eventLoopGroup.shutdownGracefully();
            if (channel != null) channel.closeFuture();
        }



    }

    /**
     * netty关闭
     */
    @PreDestroy
    public void destroy(){
        try {
            if(eventLoopGroup!=null) eventLoopGroup.shutdownGracefully().sync();
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
