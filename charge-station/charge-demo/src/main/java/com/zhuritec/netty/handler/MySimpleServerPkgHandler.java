package com.zhuritec.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 自定义服务器端消息（入站）处理器， 一般不直接实现ChannelInboundHandler接口，而是继承SimpleChannelInboundHandler
 * 1.继承SimpleChannelInboundHandler   带泛型的类，无需类型转换
 * 2.继承ChannelInboundHandlerAdapter，需要手动处理消息转换
 *
 */

@Slf4j
public class MySimpleServerPkgHandler extends SimpleChannelInboundHandler<String> {
    int i=0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>MySimpleServerPkgHandler接收到客户端的第{}条消息 :{}<<<: ",i+1,msg);
        i++;
        //向下传递
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("====mySimpleServerHandler 有新的客户端连接===={}",ctx.channel().id().asLongText());
        super.handlerAdded(ctx);
    }
}
