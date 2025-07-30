package com.zhuritec.service;


import com.zhuritec.model.MqttConstant;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * messagingGateway注解的作用：将消息发送到指定的通道里面
 */
@IntegrationComponentScan
@MessagingGateway(defaultRequestChannel = MqttConstant.OUTBOUND_CHANNEL_NAME)
public interface MqttService {

    void sendToMqtt(@Header(MqttHeaders.TOPIC)String topic, String message);

}
