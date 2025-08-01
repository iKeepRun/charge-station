package com.zhuritec.netty.handler;

import com.zhuritec.protobuf.UserProtobuf;
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
public class MySimpleClientProtoHandler extends SimpleChannelInboundHandler<UserProtobuf.User> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserProtobuf.User msg) throws Exception {
        log.info("客户端收到消息: "+msg);
        //向下传递
        ctx.fireChannelRead(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端channel已经就绪");
//        log.info("客户端的id:",ctx.channel().id().asLongText());

//        String msg="hello world$_$";
        for (int i = 1; i <=100; i++) {
            UserProtobuf.User user = UserProtobuf.User.newBuilder().setName("张三" + i).build();
            log.info("客户端第{}次发送protobuf消息:{}",i,user.getName());
            ctx.writeAndFlush(user);
        }
//        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes());

    }
}
