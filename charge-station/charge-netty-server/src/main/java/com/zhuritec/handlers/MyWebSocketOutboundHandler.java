package com.zhuritec.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class MyWebSocketOutboundHandler extends ChannelOutboundHandlerAdapter {
    private static final Timer timer=new Timer();
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info(">>>>>>>>>有新的客户端连接了：", channel.id().asLongText());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.info(">>>>>>>>>定时任务执行了：", channel.id().asLongText());
                //TODO 从iotDB中查询数据
                String msg ="这是一条来自后端的数据消息";
                channel.writeAndFlush(new TextWebSocketFrame(msg));
                log.info(">>>>>>>>>往前端发送数据：", msg);
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info(">>>>>>>>>客户端断开了连接,取消定时任务");
        timer.cancel();
    }

}
