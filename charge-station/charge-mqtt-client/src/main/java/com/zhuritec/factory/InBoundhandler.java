package com.zhuritec.factory;

import com.zhuritec.handler.TopicHandler;
import com.zhuritec.handler.TopicHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InBoundhandler implements MessageHandler {
    private TopicHandlerFactory topicHandlerFactory;

    public InBoundhandler(TopicHandlerFactory topicHandlerFactory) {
        this.topicHandlerFactory = topicHandlerFactory;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        MessageHeaders headers = message.getHeaders();
        Set<Map.Entry<String, Object>> entries = headers.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        String  topic = String.valueOf(headers.get("mqtt_receivedTopic"));
        log.info("收到主题:{}的消息", topic);
        TopicHandler handler = topicHandlerFactory.getHandler(topic);
        if (handler != null) {
            handler.handle(message);
        }else {
            log.info("没有找到主题:{}的处理器", topic);
        }

    }
}
