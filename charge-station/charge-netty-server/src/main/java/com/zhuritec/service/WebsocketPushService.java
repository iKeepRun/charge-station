package com.zhuritec.service;

import com.zhuritec.push.WebsoketChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebsocketPushService {

    public void pushAll(String message) {
        try {
            ChannelGroup channels = WebsoketChannelManager.getChannelGroup();
            if (channels != null){
                channels.writeAndFlush(new TextWebSocketFrame(message));
                log.info(">>>>>>>>>向{}个客户端群发消息：{}",channels.size(), message);
            }else{
                log.info(">>>>>>>>>没有用户在线");
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>群发消息失败：{}",e.getMessage());
        }
    }

    public void pushById(String message, String id) {
        try {
            Channel channel = WebsoketChannelManager.getChannelById(id);
            if (channel == null){
                log.info(">>>>>>>>>没有用户在线");
                return;
            }else{
                channel.writeAndFlush(new TextWebSocketFrame(message));
                log.info(">>>>>>>>>向用户{}发送消息：{}",id, message);
            }
        } catch (Exception e) {
           log.error(">>>>>>>>>向用户{}发送消息失败：{}",id,e.getMessage());
        }
    }


}
