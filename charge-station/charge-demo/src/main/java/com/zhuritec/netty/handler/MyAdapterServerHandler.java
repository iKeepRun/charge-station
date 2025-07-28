package com.zhuritec.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MyAdapterServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;

        log.info("===MyAdapterServerHandler接收到客户端消息===：{}", buf.toString(StandardCharsets.UTF_8));
        ctx.fireChannelRead(buf);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("===MyAdapterServerHandler 有新的客户端连接===：{}", ctx.channel().id());
        super.handlerAdded(ctx);
    }
}
