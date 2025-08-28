package com.zhuritec.handler;

import com.zhuritec.micro.WebsocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BussinessMessageHandler implements TopicHandler{
    private WebsocketClient websocketClient;
    public BussinessMessageHandler(WebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
    }
    @Override
    public boolean supports(String topic) {
        return topic.startsWith("charge/status");
    }

    @Override
    public void handle(Message<?> message) {
          //TODO 具体的消息逻辑
        System.out.println("收到充电桩推送的状态数据");
        String payload = message.getPayload().toString();
        log.info("收到充电桩推送的状态数据:{}",payload);

        websocketClient.pushAll(payload);

    }
}
