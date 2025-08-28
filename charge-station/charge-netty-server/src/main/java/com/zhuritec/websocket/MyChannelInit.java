package com.zhuritec.websocket;

import com.zhuritec.handlers.MyProtoBufMsgHandler;
import com.zhuritec.handlers.MyServerHeartBeatHandler;
import com.zhuritec.handlers.MyWebSocketInboundHandler;
import com.zhuritec.handlers.ProtoTobinaryMsgHandler;
import com.zhuritec.protobuf.ChargingCmdProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
public class MyChannelInit extends ChannelInitializer {
    private MyProtoBufMsgHandler myProtoBufMsgHandler;

    public MyChannelInit(MyProtoBufMsgHandler myProtoBufMsgHandler) {
        this.myProtoBufMsgHandler = myProtoBufMsgHandler;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline
                .addLast(new IdleStateHandler(60, 60, 0, TimeUnit.SECONDS))
                .addLast(new MyServerHeartBeatHandler())
                /**
                 * HttpServerCodec只能解析URL参数，不能解析请求体
                 *
                 * HttpObjectAggregator能将HTTPMessage和HTTPConnect
                 *  合并成为FullHttpRequest/FullHttpResponse
                 *
                 */
                 // 处理http请求
                .addLast(new HttpServerCodec())
                 // 处理HTTP POST请求
                .addLast(new HttpObjectAggregator(65536))
                 // 处理websocket协议的处理器
                .addLast(new WebSocketServerProtocolHandler("/ws"))
                // 将websocket请求的frame转换为ByteBuf
                .addLast(new ProtoTobinaryMsgHandler())
                // 将bytebuf转换为protobuf对象
                .addLast(new ProtobufDecoder(ChargingCmdProto.ChargingCmd.getDefaultInstance()))

                // 自定义处理器
                .addLast(new MyWebSocketInboundHandler())
                //处理反序列化之后的充电指令消息
                .addLast(myProtoBufMsgHandler)
        ;
    }
}
