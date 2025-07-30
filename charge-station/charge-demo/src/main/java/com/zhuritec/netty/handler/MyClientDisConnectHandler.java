package com.zhuritec.netty.handler;

import com.zhuritec.netty.util.NettyClientUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 自定义服务器端消息（入站）处理器， 一般不直接实现ChannelInboundHandler接口，而是继承SimpleChannelInboundHandler
 * 1.继承SimpleChannelInboundHandler   带泛型的类，无需类型转换
 * 2.继承ChannelInboundHandlerAdapter，需要手动处理消息转换
 *
 */

@Slf4j
public class MyClientDisConnectHandler extends ChannelInboundHandlerAdapter {
    private NettyClientUtil clientUtil=new NettyClientUtil();
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端断开连接时，会触发该方法
        log.info(">>>>>Netty 客户端和服务端断开了连接, 重连中...");

        //另起线程重新连接
        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(()->clientUtil.connect(),10, TimeUnit.SECONDS);
    }
}
