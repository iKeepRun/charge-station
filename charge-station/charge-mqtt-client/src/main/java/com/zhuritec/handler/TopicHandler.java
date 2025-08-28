package com.zhuritec.handler;

import org.springframework.messaging.Message;

public interface TopicHandler {
    boolean supports(String topic);
    void handle(Message<?> message);
}
