package com.zhuritec.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuritec.push.WebsoketChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyWebSocketInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
//    private final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<String, Channel>();

    private  String userId="";
    private String token="";
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info(">>>>>>>>>有新的客户端连接了：", channel.id().asLongText());

        WebsoketChannelManager.addChannel(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        concurrentHashMap.remove(channel.id().asLongText());
        WebsoketChannelManager.removeChannel(channel);
        WebsoketChannelManager.removeChannelByUserId(userId);
        log.info(">>>>>>>>>客户端断开了连接：", channel.id().asLongText());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info(">>>>>>>>>客户端已经就绪：");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {
        log.info("=============入站处理器收到消息：{}", webSocketFrame.content());
        //判断数据帧的内容是否文本类型
        if (webSocketFrame instanceof TextWebSocketFrame) {
            String msg = ((TextWebSocketFrame) webSocketFrame).text();
            log.info(">>>>>>>>>自定义入站处理器收到消息：{}", msg);
            try {
                // 解析JSON消息
                JSONObject jsonMsg = JSON.parseObject(msg);
                String type = jsonMsg.getString("type");

                if ("auth".equals(type)) {
                    handleAuthMessage(ctx, jsonMsg);
                } else {
                    // 处理其他业务消息
//                    handleBusinessMessage(ctx, jsonMsg);
                }
            } catch (Exception e) {
                log.error("解析消息失败: {}", msg, e);
            }
        }else{
            log.info(">>>>>>>>>入站处理器收到的消息不是文本类型：{}", webSocketFrame.content());
            //向下传递
            ctx.fireChannelRead(webSocketFrame);
        }
    }

    private void handleAuthMessage(ChannelHandlerContext ctx, JSONObject authMsg) {
         userId = authMsg.getString("userid");
         token = authMsg.getString("token");

        // 验证token（这里简化处理）
        if (isValidToken(userId, token)) {
            Channel channel = ctx.channel();
            channel.attr(AttributeKey.valueOf("userid")).set(userId);
            WebsoketChannelManager.addChannelByUserId(userId, channel);

            // 发送认证成功响应
            JSONObject response = new JSONObject();
            response.put("type", "auth_result");
            response.put("success", true);
            channel.writeAndFlush(new TextWebSocketFrame(response.toJSONString()));

            log.info("用户 {} 认证成功", userId);
        } else {
            // 认证失败，关闭连接
            ctx.close();
            log.warn("用户认证失败: {}", userId);
        }
    }

    private boolean isValidToken(String userId, String token) {
        // 实现token验证逻辑
        // 可以调用认证服务验证token有效性
        return token != null && !token.isEmpty(); // 简化示例
    }
}
