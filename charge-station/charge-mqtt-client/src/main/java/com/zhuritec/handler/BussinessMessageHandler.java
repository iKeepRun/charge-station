package com.zhuritec.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zhuritec.micro.WebsocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        //将message字符串转换成json对象
//        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONObject jsonObject = JSON.parseObject(payload);
//            JsonNode jsonNode = objectMapper.readTree(payload);
            String userid = jsonObject.getString("userid");
            log.info("收到用户id:{}",userid);
            if(StringUtils.hasText(userid)){
                websocketClient.pushById(payload, userid);
            }else {
                log.info("mqtt客户端收到主题“charge/status的消息");
                websocketClient.pushAll(payload);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
