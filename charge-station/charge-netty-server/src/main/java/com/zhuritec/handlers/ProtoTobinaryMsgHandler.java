package com.zhuritec.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProtoTobinaryMsgHandler extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if( msg instanceof TextWebSocketFrame){
            log.info("收到客户端消息:{}",((TextWebSocketFrame) msg).text());
        } else if (msg instanceof BinaryWebSocketFrame) {
            ByteBuf content = ((BinaryWebSocketFrame) msg).content();
            out.add(content);

            //增加引用计数，确保ByteBuf在传递到下一个handler时不会被释放
            content.retain();

        }

    }
}
