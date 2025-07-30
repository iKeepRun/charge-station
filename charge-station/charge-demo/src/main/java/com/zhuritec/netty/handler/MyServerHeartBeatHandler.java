package com.zhuritec.netty.handler;

import com.zhuritec.netty.util.NettyClientUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 自定义服务器端消息（入站）处理器， 一般不直接实现ChannelInboundHandler接口，而是继承SimpleChannelInboundHandler
 * 1.继承SimpleChannelInboundHandler   带泛型的类，无需类型转换
 * 2.继承ChannelInboundHandlerAdapter，需要手动处理消息转换
 *
 */

@Slf4j
public class MyServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if(evt instanceof IdleStateEvent){
          IdleStateEvent stateEvent = (IdleStateEvent) evt;
          IdleState state = stateEvent.state();
          switch(state){
              case READER_IDLE:
                  //客户端长时间没有发送数据
                  log.info("客户端{}读空闲，已断开  ",ctx.channel().remoteAddress());
                  //可以断开连接或者发送心跳
                  ctx.channel().close();
                  break;
              case WRITER_IDLE:
                  //服务端长时间没有发送数据
                  log.info("服务端channel {} 写空闲",ctx.channel().id());
                   break;

              case ALL_IDLE:
//                  ctx.channel().close();
                  break;
          }
      }
    }
}
