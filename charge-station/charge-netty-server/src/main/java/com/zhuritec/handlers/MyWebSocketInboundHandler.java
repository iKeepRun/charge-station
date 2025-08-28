package com.zhuritec.handlers;

import com.zhuritec.push.WebsoketChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyWebSocketInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
//    private final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<String, Channel>();



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info(">>>>>>>>>有新的客户端连接了：", channel.id().asLongText());
//        concurrentHashMap.put(channel.id().asLongText(), channel);
        WebsoketChannelManager.addChannel(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        concurrentHashMap.remove(channel.id().asLongText());
        WebsoketChannelManager.removeChannel(channel);
        log.info(">>>>>>>>>客户端断开了连接：", channel.id().asLongText());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        //判断数据帧的内容是否文本类型
        if (webSocketFrame instanceof TextWebSocketFrame) {
            String msg = ((TextWebSocketFrame) webSocketFrame).text();
            log.info(">>>>>>>>>自定义入站处理器收到消息：{}", msg);
        }
    }
}
