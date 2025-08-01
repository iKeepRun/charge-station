package com.zhuritec.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    log.info(">>>已经10sm没有接收到数据了");
                    //关闭连接
                    ctx.channel().close();
                    break;
                case WRITER_IDLE:
                    log.info("写空闲");
//                    ctx.channel().close();
                    break;
                case ALL_IDLE:
                    log.info("读写空闲");
                    break;
            }
        }
    }
}
