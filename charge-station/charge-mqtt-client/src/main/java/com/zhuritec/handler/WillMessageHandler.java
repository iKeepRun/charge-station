package com.zhuritec.handler;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class WillMessageHandler implements TopicHandler{
    @Override
    public boolean supports(String topic) {
        return topic.startsWith("mqtt/willMsg");
    }

    @Override
    public void handle(Message<?> message) {
          //TODO 具体的消息逻辑
        System.out.println("收到遗嘱消息");
    }
}
