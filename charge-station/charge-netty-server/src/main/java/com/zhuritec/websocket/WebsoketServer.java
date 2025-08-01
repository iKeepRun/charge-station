package com.zhuritec.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Order(1)
@Component
public class WebsoketServer implements CommandLineRunner {
    private NioEventLoopGroup bossGroup ;
    private   NioEventLoopGroup workerGroup ;
    private Channel channel;
    
    private final MyChannelInit myChannelInit;
    
    public WebsoketServer(MyChannelInit myChannelInit) {
        this.myChannelInit = myChannelInit;
    }
    
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
        bossGroup=new NioEventLoopGroup();
        workerGroup=new NioEventLoopGroup();

        ServerBootstrap bootstrap=new
                ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(myChannelInit);

        //2.启动
        ChannelFuture future = null;
        try {
            future = bootstrap.bind(8989).sync();
            if(future.isSuccess()) log.info("netty server start success");
            /**
             * closeFuture().sync()阻塞等待，服务端线程变成wait状态，直到服务端监听端口关闭
             */
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            destroy();
        }



    }

    /**
     * netty关闭
     * 注解 @PreDestroy  在spring容器销毁之前执行
     *
     */
    @PreDestroy
    public void destroy(){
        try {
            if(bossGroup!=null) bossGroup.shutdownGracefully().sync();
            if(workerGroup!=null) workerGroup.shutdownGracefully().sync();
            if (channel != null) channel.closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 异步启动netty server 避免阻塞springboot启动
     * @param args
     * @throws Exception
     */
    @Async
    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
