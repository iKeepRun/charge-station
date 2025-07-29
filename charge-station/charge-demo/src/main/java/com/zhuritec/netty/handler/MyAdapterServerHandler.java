package com.zhuritec.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MyAdapterServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;

        log.info("===MyAdapterServerHandler接收到客户端消息===：{}", buf.toString(StandardCharsets.UTF_8));

        /**
         * 作为最后一个处理器，不应该调用fireChannelRead方法向下传递消息
         * 并且应该手动将消息容器释放，因为父类没有释放。
         * 如果继承的是SimpleChannelInboundHandler父类，则无需手动释放，因为父类已经封装了释放方法
         * 因为从Netty 4 开始采用引用计数来管理对象的生命周期，而不是使用垃圾回收器
         * 特别是ByteBuf对象 使用引用计数，来提高内存的分配和释放 来提升性能
         *
         * ByteBuf释放方法：
         *   1. buf.release();
         *   2。ReferenceCountUtil.release(buf);
         */
//        ctx.fireChannelRead(buf);
          buf.release();
//          ReferenceCountUtil.release(buf);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("===MyAdapterServerHandler 有新的客户端连接===：{}", ctx.channel().id());
        super.handlerAdded(ctx);
    }
}
