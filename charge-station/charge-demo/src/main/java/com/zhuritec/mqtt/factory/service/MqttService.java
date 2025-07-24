package com.zhuritec.mqtt.factory.service;

import com.zhuritec.mqtt.factory.MqttConstant;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * messagingGateway注解的作用：将消息发送到指定的通道里面
 */
@IntegrationComponentScan
@MessagingGateway(defaultRequestChannel = MqttConstant.OUTBOUND_CHANNEL_NAME)
public interface MqttService {

    void sendToMqtt(String topic,String payload);

}
