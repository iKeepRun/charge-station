package com.zhuritec.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class ProtoTobinaryMsgHandler extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        try {
            if( msg instanceof TextWebSocketFrame){
                log.info("收到客户端消息xxxxxx:{}",((TextWebSocketFrame) msg).text());
                out.add(msg);
            } else if (msg instanceof BinaryWebSocketFrame) {
                log.info("收到二进制消息xxxxxx:{}", msg.content());
                ByteBuf content =  msg.content();
                msg.retain();
                //增加引用计数，确保ByteBuf在传递到下一个handler时不会被释放
                //需要添加列表前就增加引用计数
                content.retain();
//              log.info(" 增加引用计数器之后 - ByteBuf.content.refCnt: {}", content.refCnt());
                out.add(content);

            }else {
                log.info("收到其他消息xxxxxx:{}",msg.getClass().getSimpleName());
                out.add(msg);
            }
        } catch (Exception e) {
            log.error("消息转换失败",e);
//            throw new RuntimeException(e);
            //确保消息被正确释放
            ReferenceCountUtil.release(msg);
        }

    }
}
