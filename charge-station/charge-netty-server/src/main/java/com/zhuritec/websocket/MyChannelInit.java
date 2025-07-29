package com.zhuritec.websocket;

import com.zhuritec.handlers.MyWebSocketInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class MyChannelInit extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline
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
                // 自定义处理器
                .addLast(new MyWebSocketInboundHandler())
        ;
    }
}
