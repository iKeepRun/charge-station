package com.zhuritec.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义服务器端消息（入站）处理器， 一般不直接实现ChannelInboundHandler接口，而是继承SimpleChannelInboundHandler
 * 1.继承SimpleChannelInboundHandler   带泛型的类，无需类型转换
 * 2.继承ChannelInboundHandlerAdapter，需要手动处理消息转换
 *
 */

@Slf4j
public class MySimpleClientPkgHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("客户端收到消息: "+msg);
        //向下传递
        ctx.fireChannelRead(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端channel已经就绪");
//        log.info("客户端的id:",ctx.channel().id().asLongText());

        String msg="hello world$_$";
        for (int i = 1; i <=100; i++) {

            log.info("客户端第{}次发送消息:{}",i,msg);
            ctx.writeAndFlush(msg);
        }
//        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes());
    }
}
